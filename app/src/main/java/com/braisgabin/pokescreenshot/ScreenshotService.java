package com.braisgabin.pokescreenshot;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.braisgabin.pokescreenshot.model.Pokemon;
import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.Guesser;
import com.braisgabin.pokescreenshot.processing.ScreenshotChecker;
import com.braisgabin.pokescreenshot.processing.ScreenshotReader;
import com.f2prateek.rx.preferences.Preference;
import com.google.auto.value.AutoValue;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
import static android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.braisgabin.pokescreenshot.SettingsActivity.REMOVE_SCREENSHOTS;
import static com.braisgabin.pokescreenshot.SettingsActivity.TRAINER_LVL;
import static com.braisgabin.pokescreenshot.Utils.isSystemAlertPermissionGranted;
import static com.braisgabin.pokescreenshot.processing.ScreenshotChecker.getScreenshotType;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static rx.exceptions.Exceptions.propagate;

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
  WindowManager windowManager;

  @Inject
  @Named(TRAINER_LVL)
  Preference<String> trainerLvl;

  @Inject
  @Named(REMOVE_SCREENSHOTS)
  Preference<Boolean> removeScreenshots;

  @Inject
  NotificationManagerCompat notificationManager;

  private Subscription subscription;
  private BroadcastReceiver broadcastReceiver;
  private ViewGroup viewGroup;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    App.component(this)
        .inject(this);

    broadcastReceiver = new BroadcastReceiver() {

      @Override
      public void onReceive(Context context, Intent intent) {
        stopSelf();
      }
    };

    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);
    params.gravity = Gravity.BOTTOM;

    this.viewGroup = new CoordinatorLayout(new ContextThemeWrapper(this, R.style.AppTheme));

    // Add layout to window manager
    windowManager.addView(viewGroup, params);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, notification(trainerLvl(), false));
    final AtomicInteger ref = new AtomicInteger(0);

    registerReceiver(broadcastReceiver, new IntentFilter(ACTION_STOP));

    if (subscription == null) {
      subscription = ScreenshotObservable.newScreenshot(getContentResolver())
          .doOnNext(new Action1<Object>() {
            @Override
            public void call(Object object) {
              startForeground(1, notification(trainerLvl(), ref.incrementAndGet() > 0));
            }
          })
          .publish(new Func1<Observable<Uri>, Observable<UriBitmap>>() {
            @Override
            public Observable<UriBitmap> call(Observable<Uri> uriObservable) {
              final BitmapFactory.Options options = new BitmapFactory.Options();
              options.inMutable = true;
              return Observable.zip(
                  uriObservable,
                  uriObservable
                      .map(new Func1<Uri, Bitmap>() {
                        @Override
                        public Bitmap call(Uri uri) {
                          try {
                            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            if (bitmap == null) {
                              throw new IllegalStateException("Impossible to generate a Bitmap from the uri: " + uri.toString());
                            }
                            return bitmap;
                          } catch (IOException e) {
                            throw propagate(e);
                          }
                        }
                      }),
                  new Func2<Uri, Bitmap, UriBitmap>() {
                    @Override
                    public UriBitmap call(Uri uri, Bitmap bitmap) {
                      return UriBitmap.create(uri, bitmap);
                    }
                  });
            }
          })
          .groupBy(new Func1<UriBitmap, ScreenshotChecker.Type>() {
            @Override
            public ScreenshotChecker.Type call(UriBitmap ub) {
              return getScreenshotType(ub.bitmap());
            }
          })
          .subscribe(
              new Action1<GroupedObservable<ScreenshotChecker.Type, UriBitmap>>() {
                @Override
                public void call(GroupedObservable<ScreenshotChecker.Type, UriBitmap> observable) {
                  switch (observable.getKey()) {
                    case pokemon:
                      pokemonScreenshot(observable, ref);
                      break;
                    case pokemon_with_alert:
                      pokemonScreenshotWithAlert(observable, ref);
                      break;
                    case no_pokemon:
                      noPokemonScreenshot(observable, ref);
                      break;
                    default:
                      throw new IllegalArgumentException("I don't know how to handle the type: " + observable.getKey());
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

  private Subscription pokemonScreenshot(Observable<UriBitmap> observable, final AtomicInteger ref) {
    return observable
        .map(new Func1<UriBitmap, Result>() {
          @Override
          public Result call(UriBitmap ub) {
            final ScreenshotComponent c = component.plus(new ScreenshotModule(ub.bitmap()));
            try {
              final Angle angle = c.angle();
              final ScreenshotReader reader = c.screenshotReader();
              final int trainerLvl = trainerLvl();
              final float pokemonLvl = Guesser.lvl(angle, reader, trainerLvl);
              Timber.d("lvl: %.1f", pokemonLvl);
              final List<Pokemon> pokemonList = Pokemon.selectByCandy(database, reader.candy());
              final Pokemon pokemon = Guesser.getPokemon(pokemonList, reader, pokemonLvl);
              return Result.create(Guesser.iv(pokemon, reader.cp(), reader.hp(), pokemonLvl), ub.uri());
            } catch (Exception e) {
              Timber.e(e);
              return Result.create(e, ub.uri());
            }
          }
        })
        .doOnNext(new Action1<Result>() {
          @Override
          public void call(Result result) {
            if (removeScreenshots.get() && result.exception() == null) {
              getContentResolver().delete(result.uri(), null, null);
            }
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new Action1<Result>() {
              @Override
              public void call(Result result) {
                startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
                final Exception exception = result.exception();
                if (exception == null) {
                  final List<int[]> ivs = result.ivs();
                  final String s;
                  if (ivs.size() == 1) {
                    final int[] iv = ivs.get(0);
                    s = getString(R.string.snackbar_just_one_iv, iv[0], iv[1], iv[2], (iv[0] + iv[1] + iv[2]) * 100 / 45.);
                  } else {
                    final float[] ivRange = calculateIvRange(ivs);
                    s = getString(R.string.snackbar_multiple_iv, ivRange[0] * 100, ivRange[2] * 100, ivRange[1] * 100);
                  }
                  Timber.d(s);
                  @SuppressWarnings("WrongConstant")
                  final Snackbar snackbar = Snackbar.make(viewGroup, s, 10 * 1000);
                  snackbar.show();
                } else {
                  try {
                    throw exception;
                  } catch (Angle.InitialPointException e) {
                    notifyError(R.string.error_pokemon_lvl_title,
                        R.string.error_pokemon_lvl_arc,
                        result.uri());
                  } catch (ScreenshotReader.CandyException e) {
                    notifyError(R.string.error_ocr_title,
                        R.string.error_ocr_candy,
                        result.uri());
                  } catch (ScreenshotReader.CpException e) {
                    notifyError(R.string.error_ocr_title,
                        R.string.error_ocr_cp,
                        result.uri());
                  } catch (ScreenshotReader.HpException e) {
                    notifyError(R.string.error_ocr_title,
                        R.string.error_ocr_hp,
                        result.uri());
                  } catch (Guesser.MultiplePokemonException e) {
                    notifyError(R.string.error_guessing_pokemon_title,
                        R.string.error_guessing_pokemon_multiple,
                        result.uri());
                  } catch (Guesser.UnknownPokemonException e) {
                    notifyError(R.string.error_guessing_pokemon_title,
                        R.string.error_guessing_pokemon_none,
                        result.uri());
                  } catch (Guesser.NoIvPossibilities e) {
                    notifyError(R.string.error_ocr_title,
                        R.string.error_no_iv,
                        result.uri());
                  } catch (Guesser.UnknownPokemonLvl e) {
                    notifyError(R.string.error_pokemon_lvl_title,
                        R.string.error_pokemon_lvl_circle,
                        result.uri());
                  } catch (Exception e) {
                    notifyError(getString(R.string.error_unknown_title),
                        e.getMessage() + ".",
                        result.uri());
                  }
                }
              }
            });
  }

  private void pokemonScreenshotWithAlert(Observable<UriBitmap> observable, final AtomicInteger ref) {
    observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<UriBitmap>() {
          @Override
          public void call(UriBitmap uriBitmap) {
            startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
            final String text = getString(R.string.pokemon_screenshot_with_alert);
            Toast.makeText(ScreenshotService.this, text, Toast.LENGTH_LONG).show();
          }
        });
  }

  private void noPokemonScreenshot(Observable<UriBitmap> observable, final AtomicInteger ref) {
    observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<UriBitmap>() {
          @Override
          public void call(UriBitmap uriBitmap) {
            startForeground(1, notification(trainerLvl(), ref.decrementAndGet() > 0));
            final String text = getString(R.string.no_pokemon_screenshot);
            Toast.makeText(ScreenshotService.this, text, Toast.LENGTH_LONG).show();
          }
        });
  }

  private int trainerLvl() {
    return Integer.parseInt(trainerLvl.get());
  }

  private void notifyError(int title, int message, Uri uri) {
    notifyError(getString(title), getString(message), uri);
  }

  private void notifyError(final String title, final String message, final Uri uri) {
    if (!isSystemAlertPermissionGranted(this)) {
      final Intent cancelNotificationIntent = NotificationActivity.getCallingIntent(this, 2);
      final Intent notifyErrorIntent = notifyErrorIntent(title, message, uri);
      final Intent[] intents = {
          cancelNotificationIntent,
          notifyErrorIntent,
      };
      final PendingIntent notifyErrorPendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);

      final Notification notification = new NotificationCompat.Builder(this)
          .setSmallIcon(R.drawable.ic_notification01)
          .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
          .setContentTitle(title)
          .setContentText(message)
          .setStyle(new NotificationCompat.BigTextStyle()
              .bigText(message))
          .addAction(0, getString(R.string.action_report), notifyErrorPendingIntent)
          .setAutoCancel(true)
          .setCategory(NotificationCompat.CATEGORY_ERROR)
          .setPriority(NotificationCompat.PRIORITY_MAX)
          .build();

      notificationManager.notify(2, notification);
    } else {
      showDialog(new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
          .setTitle(title)
          .setMessage(message + " " + getString(R.string.do_you_want_to_report))
          .setNegativeButton(android.R.string.no, null)
          .setPositiveButton(R.string.action_report, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
              startActivity(notifyErrorIntent(title, message, uri));
            }
          })
          .create());
    }
  }

  private void showDialog(Dialog dialog) {
    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    dialog.show();
  }

  @SuppressWarnings("deprecation")
  @TargetApi(LOLLIPOP)
  private Intent notifyErrorIntent(final String title, final String message, final Uri uri) {
    final Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("message/rfc822");
    intent.putExtra(Intent.EXTRA_SUBJECT, "[Pokescreenshot] " + title);
    intent.putExtra(Intent.EXTRA_TEXT, message + "\nLocale: " + Locale.getDefault() + "\nTrainer lvl: " + trainerLvl() + "\nPokeScreenshot version: " + App.get(this).versionName());
    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pokescreenshotreport@braisgabin.com"});
    intent.putExtra(Intent.EXTRA_STREAM, uri);
    intent.addFlags(SDK_INT < LOLLIPOP ? FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET : FLAG_ACTIVITY_NEW_DOCUMENT);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
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
    windowManager.removeView(viewGroup);
  }

  private Notification notification(int trainerLvl, boolean working) {
    final Intent stopIntent = getStopActionIntent();
    final PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    final Intent settingsIntent = SettingsActivity.getCallingIntent(this, false);
    final PendingIntent settingsPendingIntent = PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    final int smallIcon = working ? R.drawable.ic_notification_working : R.drawable.ic_notification01;
    final String contentText = getString(working ? R.string.working : R.string.idle);
    return new NotificationCompat.Builder(this)
        .setSmallIcon(smallIcon)
        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setContentTitle(getString(R.string.app_name))
        .setContentText(contentText)
        .setContentInfo(getString(R.string.trainer_lvl_d, trainerLvl))
        .setOngoing(true)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .addAction(R.drawable.ic_stop_24dp, getString(R.string.stop_service), stopPendingIntent)
        .addAction(R.drawable.ic_settings_24dp, getString(R.string.settings), settingsPendingIntent)
        .build();
  }

  @AutoValue
  static abstract class UriBitmap {
    public static UriBitmap create(Uri uri, Bitmap bitmap) {
      return new AutoValue_ScreenshotService_UriBitmap(uri, bitmap);
    }

    abstract Uri uri();

    abstract Bitmap bitmap();
  }

  @AutoValue
  abstract static class Result {
    static Result create(List<int[]> ivs, Uri uri) {
      return new AutoValue_ScreenshotService_Result(ivs, null, uri);
    }

    static Result create(Exception exception, Uri uri) {
      return new AutoValue_ScreenshotService_Result(null, exception, uri);
    }

    @Nullable
    abstract List<int[]> ivs();

    @Nullable
    abstract Exception exception();

    abstract Uri uri();
  }
}
