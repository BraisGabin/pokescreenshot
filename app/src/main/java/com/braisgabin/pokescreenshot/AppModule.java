package com.braisgabin.pokescreenshot;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.braisgabin.pokescreenshot.model.MySQLiteOpenHelper;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.braisgabin.pokescreenshot.processing.Utils.copyRecursive;

@Module
class AppModule {
  private final static String DIR_NAME = "tesseract";
  private final App app;

  AppModule(App app) {
    this.app = app;
  }

  @Provides
  App appProvider() {
    return app;
  }

  @Singleton
  @Provides
  AssetManager assetManagerProvider() {
    return app.getAssets();
  }

  @Singleton
  @Provides
  TessBaseAPI tessBaseAPIProvider(AssetManager assets) {
    final File root = app.getCacheDir();
    try {
      if (!new File(root, DIR_NAME).exists()) {
        copyRecursive(assets, DIR_NAME, root);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final TessBaseAPI tess = new TessBaseAPI();
    tess.init(root.getAbsolutePath() + "/" + DIR_NAME + "/", "eng");
    tess.readConfigFile("pokemon");

    return tess;
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

  @Singleton
  @Provides
  SharedPreferences sharedPreferencesProvider() {
    return PreferenceManager.getDefaultSharedPreferences(app);
  }

  @Singleton
  @Provides
  RxSharedPreferences rxSharedPreferencesProvider(SharedPreferences sharedPreferences) {
    return RxSharedPreferences.create(sharedPreferences);
  }

  @Singleton
  @Provides
  Preference<String> trainerLvlProvider(RxSharedPreferences rxSharedPreferences) {
    return rxSharedPreferences.getString("trainer_lvl", "1");
  }
}
