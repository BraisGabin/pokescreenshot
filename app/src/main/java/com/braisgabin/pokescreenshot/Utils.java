package com.braisgabin.pokescreenshot;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.List;

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

  public static <T> List<T> list(SQLiteDatabase database, RowMapper<T> mapper, String sql, String... selectionArgs) {
    final Cursor cursor = database.rawQuery(sql, selectionArgs);
    List<T> list = new ArrayList<>(cursor.getCount());
    while (cursor.moveToNext()) {
      list.add(mapper.map(cursor));
    }
    return list;
  }
}
