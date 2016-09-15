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

import com.braisgabin.fileobservable.FileObservable;
import com.braisgabin.pokescreenshot.model.Pokemon;
import com.braisgabin.pokescreenshot.processing.CP;
import com.braisgabin.pokescreenshot.processing.Guesser;
import com.braisgabin.pokescreenshot.processing.Ocr;
import com.braisgabin.pokescreenshot.processing.ProcessingException;
import com.f2prateek.rx.preferences.Preference;

import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

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

  @Inject
  Preference<String> trainerLvl;

  private File screenshotsDir;
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
    startForeground(1, notification(Integer.parseInt(trainerLvl.get())));

    registerReceiver(broadcastReceiver, new IntentFilter(ACTION_STOP));

    if (subscription == null) {
      subscription = FileObservable.newFiles(screenshotsDir)
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
          .map(new Func1<Bitmap, List<int[]>>() {
            @Override
            public List<int[]> call(Bitmap bitmap) {
              final ScreenshotComponent c = component.plus(new ScreenshotModule(bitmap));
              try {
                final float pokemonLvl = CP.radian2Lvl(Integer.parseInt(trainerLvl.get()), c.angle().radian());
                Timber.d("lvl: %.1f", pokemonLvl);
                final Ocr.Pokemon ocrData = c.ocr().ocr();
                final List<Pokemon> pokemonList = Pokemon.selectByCandy(database, ocrData.getCandy());
                final Pokemon pokemon = Guesser.getPokemon(pokemonList, ocrData.getCp(), ocrData.getHp(), pokemonLvl);
                return Guesser.iv(pokemon, ocrData.getCp(), ocrData.getHp(), pokemonLvl);
              } catch (ProcessingException e) {
                throw new RuntimeException(e);
              }
            }
          })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              new Action1<List<int[]>>() {
                @Override
                public void call(List<int[]> ivs) {
                  final float[] ivRange = calculateIvRange(ivs);
                  final String s = String.format(Locale.getDefault(),
                      "(%.2f%%, %.2f%%) %.2f%%", ivRange[0] * 100, ivRange[2] * 100, ivRange[1] * 100);
                  Timber.d(s);
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

  private float[] calculateIvRange(List<int[]> ivs) {
    final float[] values = {45, 0, 0};
    for (int[] iv : ivs) {
      final int value = iv[0] + iv[1] + iv[2];
      values[0] = min(values[0], value);
      values[1] += value;
      values[2] = max(values[2], value);
    }
    values[0] = values[0] / 45;
    values[1] = values[1] / (ivs.size() * 45);
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

  private Notification notification(int trainerLvl) {
    final Intent stopIntent = getStopActionIntent();
    final PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    final Intent settingsIntent = SettingsActivity.getCallingIntent(this);
    final PendingIntent settingsPendingIntent = PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setContentText("Trainer lvl " + trainerLvl)
        .setOngoing(true)
        .addAction(0, getString(R.string.stop_service), stopPendingIntent)
        .addAction(0, getString(R.string.settings), settingsPendingIntent)
        .build();
  }
}
