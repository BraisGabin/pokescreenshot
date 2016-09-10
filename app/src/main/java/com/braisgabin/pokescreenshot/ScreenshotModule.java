package com.braisgabin.pokescreenshot;

import android.graphics.Bitmap;

import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.Ocr;
import com.googlecode.tesseract.android.TessBaseAPI;

import dagger.Module;
import dagger.Provides;

@Module
public class ScreenshotModule {
  final Bitmap bitmap;

  public ScreenshotModule(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  @Provides
  Ocr ocrProvider(App app, TessBaseAPI tess) {
    return Ocr.create(tess, app, bitmap, null);
  }

  @Provides
  Angle angleProvider() {
    return new Angle(bitmap);
  }
}
