package com.braisgabin.pokescreenshot.model;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MySQLiteOpenHelper extends SQLiteAssetHelper {

  private static final String DATABASE_NAME = "pokemon.db";
  private static final int DATABASE_VERSION = 3;

  public MySQLiteOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
}
