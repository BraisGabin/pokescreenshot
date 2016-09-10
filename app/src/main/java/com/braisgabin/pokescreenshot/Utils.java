package com.braisgabin.pokescreenshot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
}
