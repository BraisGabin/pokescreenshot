package com.braisgabin.pokescreenshot;

import android.app.Application;
import android.content.Context;

public class App extends Application {
  private AppComponent component;

  static App get(Context context) {
    return (App) context.getApplicationContext();
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
}
