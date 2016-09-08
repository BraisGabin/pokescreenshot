package com.braisgabin.pokescreenshot;

import android.app.Application;
import android.content.Context;

public class App extends Application {
  static App get(Context context) {
    return (App) context.getApplicationContext();
  }
}
