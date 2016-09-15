package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;

public class ScreenshotChecker {
  private static final int BACKGROUND_COLOR = 0xfffafafa;
  private static final int BUTTON_COLOR = 0xff1c8796;

  public static boolean isPokemonGoScreenshot(Bitmap bitamp) {
    final int width = bitamp.getWidth();
    final int height = bitamp.getHeight();
    final float d = width / 480f;
    final int x = Math.round(d * 40);
    for (int i = 6; i < 9; i++) {
      final int y = i * height / 10;
      if (bitamp.getPixel(x, y) != BACKGROUND_COLOR || bitamp.getPixel(width - x, y) != BACKGROUND_COLOR) {
        return false;
      }
    }
    return true;
  }
}
