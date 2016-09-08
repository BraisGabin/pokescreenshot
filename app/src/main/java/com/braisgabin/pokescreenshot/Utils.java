package com.braisgabin.pokescreenshot;

import android.app.ActivityManager;
import android.content.Context;

public class Utils {

  // Extracted from: http://stackoverflow.com/a/5921190/842697
  public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }
}
