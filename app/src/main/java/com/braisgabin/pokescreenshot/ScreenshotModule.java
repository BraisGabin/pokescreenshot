package com.braisgabin.pokescreenshot;

import android.graphics.Bitmap;

import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.Ocr;
import com.braisgabin.pokescreenshot.processing.ScreenshotReader;
import com.braisgabin.pokescreenshot.processing.Tess;
import com.googlecode.tesseract.android.TessBaseAPI;

import dagger.Module;
import dagger.Provides;

@Module
public class ScreenshotModule {
  private final Bitmap bitmap;

  public ScreenshotModule(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  @Provides
  Tess tessProvider(App app, TessBaseAPI tess) {
    return Tess.create(tess, app, bitmap, null);
  }

  @Provides
  ScreenshotReader ocrProvider(Tess tess) {
    return new Ocr(tess);
  }

  @Provides
  Angle angleProvider() {
    return new Angle(bitmap);
  }
}
