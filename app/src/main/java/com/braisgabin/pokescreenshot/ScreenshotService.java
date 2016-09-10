package com.braisgabin.pokescreenshot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.braisgabin.pokescreenshot.model.Pokemon;
import com.braisgabin.pokescreenshot.processing.CP;
import com.braisgabin.pokescreenshot.processing.Guesser;
import com.braisgabin.pokescreenshot.processing.Ocr;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ScreenshotService extends Service {
  private final static String ACTION_STOP = "com.braisgabin.pokescreenshot.ACTION_STOP";

  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, ScreenshotService.class);

    return intent;
  }

  private static Intent getStopActionIntent() {
    final Intent intent = new Intent(ACTION_STOP);

    return intent;
  }

  @Inject
  AppComponent component;

  @Inject
  SQLiteDatabase database;

  private File screenshotsDir;
  private FF fileFilter = new FF();
  private Subscription subscription;
  private BroadcastReceiver broadcastReceiver;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    // https://github.com/android/platform_frameworks_base/blob/master/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java#L98
    final File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    this.screenshotsDir = new File(root, "Screenshots");

    App.component(this)
        .inject(this);

    broadcastReceiver = new BroadcastReceiver() {

      @Override
      public void onReceive(Context context, Intent intent) {
        stopSelf();
      }
    };
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, notification());

    registerReceiver(broadcastReceiver, new IntentFilter(ACTION_STOP));

    if (subscription == null) {
      subscription = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
          .subscribeOn(Schedulers.io())
          .flatMap(new Func1<Long, Observable<File>>() {
            @Override
            public Observable<File> call(Long aLong) {
              final File[] files = screenshotsDir.listFiles(fileFilter);
              fileFilter.time = System.currentTimeMillis() / 1000;
              return Observable.from(files);
            }
          })
          .doOnNext(new Action1<File>() {
            @Override
            public void call(File file) {
              fileFilter.ring.add(file);
            }
          })
          .map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
              final BitmapFactory.Options options = new BitmapFactory.Options();
              options.inMutable = true;
              long sleep = 100;
              Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
              while (bitmap == null) {
                try {
                  Thread.sleep(sleep);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                sleep = sleep << 1;
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
              }
              return bitmap;
            }
          })
          .map(new Func1<Bitmap, int[][]>() {
            @Override
            public int[][] call(Bitmap bitmap) {
              final ScreenshotComponent c = component.plus(new ScreenshotModule(bitmap));
              final float pokemonLvl = CP.radian2Lvl(22, c.angle().radian()); // FIXME Hardcode
              final Ocr.Pokemon ocrData = c.ocr().ocr();
              final List<Pokemon> pokemonList = Pokemon.selectByCandy(database, ocrData.getCandy());
              final Pokemon pokemon = Guesser.getPokemon(pokemonList, ocrData.getCp(), ocrData.getHp(), pokemonLvl);
              return Guesser.iv(pokemon, ocrData.getCp(), ocrData.getHp(), pokemonLvl);
            }
          })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              new Action1<int[][]>() {
                @Override
                public void call(int[][] ivs) {
                  float[] ivRange = calculateIvRange(ivs);
                  String s = String.format(Locale.getDefault(),
                      "(%.2f%%, %.2f%%) %.2f%%", ivRange[0] * 100, ivRange[2] * 100, ivRange[1] * 100);
                  System.out.println(s);
                  Toast.makeText(ScreenshotService.this, s, Toast.LENGTH_SHORT).show();
                }
              },
              new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                  throw new RuntimeException(throwable);
                }
              });
    }

    return Service.START_STICKY;
  }

  private float[] calculateIvRange(int[][] ivs) {
    final float[] values = {45, 0, 0};
    for (int[] iv : ivs) {
      final int value = iv[0] + iv[1] + iv[2];
      values[0] = min(values[0], value);
      values[1] += value;
      values[2] = max(values[2], value);
    }
    values[0] = values[0] / 45;
    values[1] = values[1] / (ivs.length * 45);
    values[2] = values[2] / 45;
    return values;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (subscription != null) {
      subscription.unsubscribe();
      subscription = null;
    }
    unregisterReceiver(broadcastReceiver);
  }

  private Notification notification() {
    final Intent stopIntent = getStopActionIntent();
    final PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setOngoing(true)
        .addAction(0, getString(R.string.stop_service), stopPendingIntent)
        .build();
  }

  class FF implements FileFilter {
    long time = Long.MAX_VALUE;
    final Ring<File> ring = new Ring<>(5);

    @Override
    public boolean accept(File file) {
      return !ring.contains(file) && file.lastModified() / 1000 >= time;
    }
  }
}
