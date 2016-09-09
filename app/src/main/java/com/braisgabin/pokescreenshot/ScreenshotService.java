package com.braisgabin.pokescreenshot;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ScreenshotService extends Service {
  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, ScreenshotService.class);

    return intent;
  }

  @Inject
  SQLiteDatabase database;

  private File screenshotsDir;
  private FF fileFilter = new FF();
  private Subscription subscription;

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
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, notification());

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
          .subscribe(
              new Action1<File>() {
                @Override
                public void call(File file) {
                  System.out.println(file);
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

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (subscription != null) {
      subscription.unsubscribe();
      subscription = null;
    }
  }

  private Notification notification() {
    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setOngoing(true)
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
