package com.braisgabin.pokescreenshot;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import timber.log.Timber;

public class App extends Application {
  private AppComponent component;

  static App get(Context context) {
    return (App) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Timber.plant(new ProductionTree());
    }
  }

  public static AppComponent component(Context context) {
    return ((App) context.getApplicationContext()).getComponent();
  }

  public synchronized AppComponent getComponent() {
    if (component == null) {
      component = DaggerAppComponent.builder()
          .appModule(new AppModule(this))
          .build();
    }
    return component;
  }

  private static class ProductionTree extends Timber.DebugTree {
    @Override protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }

      super.log(priority, tag, message, t);
    }
  }
}
