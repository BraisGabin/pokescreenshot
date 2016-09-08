package com.braisgabin.pokescreenshot;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.FileFilter;

public class ScreenshotService extends Service {
  public static Intent getCallingIntent(Context context) {
    final Intent intent = new Intent(context, ScreenshotService.class);

    return intent;
  }

  private File screenshotsDir;
  private Runnable runnable;
  private Handler handler;
  private long time;
  private Ring<File> ring;
  private FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(File file) {
      return !ring.contains(file) && file.lastModified() / 1000 >= time;
    }
  };

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
    this.time = Long.MAX_VALUE;
    this.ring = new Ring<>(5);

    this.handler = new Handler();
    this.runnable = new Runnable() {
      @Override
      public void run() {
        final File[] files = screenshotsDir.listFiles(fileFilter);
        time = System.currentTimeMillis();
        time = time / 1000;
        if (files.length > 0) {
          for (File file : files) {
            ring.add(file);
            System.out.println(file);
          }
        }
        handler.postDelayed(runnable, 1001);
      }
    };
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(1, notification());

    runnable.run();

    return Service.START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    handler.removeCallbacks(runnable);
  }

  private Notification notification() {
    return new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getString(R.string.app_name))
        .setOngoing(true)
        .build();
  }
}
