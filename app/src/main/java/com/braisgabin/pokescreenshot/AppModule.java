package com.braisgabin.pokescreenshot;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.braisgabin.pokescreenshot.model.MySQLiteOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {
  private final App app;

  AppModule(App app) {
    this.app = app;
  }

  @Singleton
  @Provides
  SQLiteOpenHelper sqliteOpenHelperProvider() {
    return new MySQLiteOpenHelper(app);
  }

  @Singleton
  @Provides
  SQLiteDatabase sqLiteDatabaseProvider(SQLiteOpenHelper sqLiteOpenHelper) {
    return sqLiteOpenHelper.getReadableDatabase();
  }
}
