package com.braisgabin.pokescreenshot;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {
        AppModule.class
    }
)
public interface AppComponent {
  void inject(MainActivity activity);

  void inject(ScreenshotService service);

  ScreenshotComponent plus(ScreenshotModule module);
}
