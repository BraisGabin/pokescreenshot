package com.braisgabin.pokescreenshot;

import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.Ocr;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Singleton
@Subcomponent(
    modules = {
        ScreenshotModule.class
    }
)
public interface ScreenshotComponent {
  Ocr ocr();

  Angle angle();
}
