package com.braisgabin.pokescreenshot;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Settings;

import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static <T> List<T> list(SQLiteDatabase database, RowMapper<T> mapper, String sql, String... selectionArgs) {
    final Cursor cursor = database.rawQuery(sql, selectionArgs);
    List<T> list = new ArrayList<>(cursor.getCount());
    while (cursor.moveToNext()) {
      list.add(mapper.map(cursor));
    }
    return list;
  }

  @TargetApi(Build.VERSION_CODES.M)
  // Extracted from: http://stackoverflow.com/a/33895409/842697
  public static boolean isSystemAlertPermissionGranted(Context context) {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
  }
}
