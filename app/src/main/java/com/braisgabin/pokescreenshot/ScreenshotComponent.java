package com.braisgabin.pokescreenshot;

import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.ScreenshotReader;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Singleton
@Subcomponent(
    modules = {
        ScreenshotModule.class
    }
)
public interface ScreenshotComponent {
  ScreenshotReader screenshotReader();

  Angle angle();
}
