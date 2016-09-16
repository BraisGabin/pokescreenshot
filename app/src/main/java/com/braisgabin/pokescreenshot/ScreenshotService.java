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
import com.google.auto.value.AutoValue;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import timber.log.Timber;

import static com.braisgabin.pokescreenshot.processing.ScreenshotChecker.isPokemonGoScreenshot;
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
    startForeground(1, notification(trainerLvl(), false));
    final AtomicInteger ref = new AtomicInteger(0);

    registerReceiver(broadcastReceiver, new IntentFilter(ACTION_STOP));

    if (subscription == null) {
      subscription = FileObservable.newFiles(screenshotsDir)
          .publish(new Func1<Observable<File>, Observable<FileBitmap>>() {
            @Override
            public Observable<FileBitmap> call(Observable<File> fileObservable) {
              final BitmapFactory.Options options = new BitmapFactory.Options();
              options.inMutable = true;
              return Observable.zip(
                  fileObservable,
                  fileObservable
                      .doOnNext(new Action1<File>() {
                        @Override
                        public void call(File file) {
                          startForeground(1, notification(trainerLvl(), ref.incrementAndGet() > 0));
                        }
                      })
                      .map(toBitmap(options, 5, 100)),
                  new Func2<File, Bitmap, FileBitmap>() {
                    @Override
                    public FileBitmap call(File file, Bitmap bitmap) {
                      return FileBitmap.create(file, bitmap);
                    }
                  });
            }
          })
          .groupBy(new Func1<FileBitmap, Boolean>() {
            @Override
            public Boolean call(FileBitmap fb) {
              return isPokemonGoScreenshot(fb.bitmap());
            }
          })
          .subscribe(
              new Action1<GroupedObservable<Boolean, FileBitmap>>() {
                @Override
                public void call(GroupedObservable<Boolean, FileBitmap> observable) {
                  if (observable.getKey()) {
                    pokemonGoScreenshot(observable, ref);
                  } else {
                    noPokemonGoScreenshot(observable, ref);
                  }
                }
              },
              new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                  startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
                  throw new RuntimeException(throwable);
                }
              }
          );
    }

    return Service.START_STICKY;
  }

  private Subscription pokemonGoScreenshot(Observable<FileBitmap> observable, final AtomicInteger ref) {
    return observable
        .map(new Func1<FileBitmap, List<int[]>>() {
          @Override
          public List<int[]> call(FileBitmap fb) {
            final ScreenshotComponent c = component.plus(new ScreenshotModule(fb.bitmap()));
            try {
              final float pokemonLvl = CP.radian2Lvl(trainerLvl(), c.angle().radian());
              Timber.d("lvl: %.1f", pokemonLvl);
              final Ocr ocr = c.ocr();
              final List<Pokemon> pokemonList = Pokemon.selectByCandy(database, ocr.candy());
              final int cp = ocr.cp();
              final int hp = ocr.hp();
              final Pokemon pokemon = Guesser.getPokemon(pokemonList, cp, hp, pokemonLvl);
              return Guesser.iv(pokemon, cp, hp, pokemonLvl);
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
                startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
              }
            });
  }

  private void noPokemonGoScreenshot(Observable<FileBitmap> observable, final AtomicInteger ref) {
    observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<FileBitmap>() {
          @Override
          public void call(FileBitmap fileBitmap) {
            startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
            final String text = getString(R.string.no_pokemon_go_screenshot);
            Toast.makeText(ScreenshotService.this, text, Toast.LENGTH_LONG).show();
          }
        });
  }

  private int trainerLvl() {
    return Integer.parseInt(trainerLvl.get());
  }

  private Func1<? super File, Bitmap> toBitmap(final BitmapFactory.Options options, final int retryTimes, final long initialWait) {
    return new Func1<File, Bitmap>() {
      @Override
      public Bitmap call(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        for (int i = 0; i < retryTimes && bitmap == null; i++) {
          try {
            final long wait = initialWait << i;
            Timber.d("Error decoding image, waiting to retry %dms", wait);
            Thread.sleep(wait);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
          } catch (InterruptedException e) {
            throw Exceptions.propagate(e);
          }
        }
        if (bitmap == null) {
          throw new IllegalStateException("Impossible to decode the file.");
        }
        return bitmap;
      }
    };
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

  private Notification notification(int trainerLvl, boolean working) {
    final Intent stopIntent = getStopActionIntent();
    final PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    final Intent settingsIntent = SettingsActivity.getCallingIntent(this);
    final PendingIntent settingsPendingIntent = PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    final String contentText = getString(working ? R.string.working : R.string.idle);
    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(contentText)
        .setContentInfo(getString(R.string.trainer_lvl_d, trainerLvl))
        .setOngoing(true)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .addAction(0, getString(R.string.stop_service), stopPendingIntent)
        .addAction(0, getString(R.string.settings), settingsPendingIntent)
        .build();
  }

  @AutoValue
  static abstract class FileBitmap {
    public static FileBitmap create(File file, Bitmap bitmap) {
      return new AutoValue_ScreenshotService_FileBitmap(file, bitmap);
    }

    abstract File file();

    abstract Bitmap bitmap();
  }
}
