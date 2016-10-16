package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;

import static com.braisgabin.pokescreenshot.processing.Utils.navBarHeight;
import static com.braisgabin.pokescreenshot.processing.Utils.proportion;

public class ScreenshotChecker {
  private static final int BACKGROUND_COLOR = 0xfffafafa;
  private static final int BACKGROUND_ALERT = 0xfff04b5f;

  public static Type getScreenshotType(Bitmap bitmap) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    final int navBarHeight = navBarHeight(bitmap);
    final float d = proportion(bitmap, navBarHeight);
    final int x = Math.round(d * 40);
    for (int i = 6; i < 9; i++) {
      final int y = i * height / 10;
      if (bitmap.getPixel(x, y) != BACKGROUND_COLOR
          || bitmap.getPixel(width - x, y) != BACKGROUND_COLOR
          || bitmap.getPixel(1, y) == BACKGROUND_COLOR
          || bitmap.getPixel(width - 2, y) == BACKGROUND_COLOR) {
        return Type.no_pokemon;
      }
    }
    final int y = Math.round(d * 40);
    if (bitmap.getPixel(x, y) == BACKGROUND_ALERT && bitmap.getPixel(width - x, y) == BACKGROUND_ALERT) {
      return Type.pokemon_with_alert;
    }
    return Type.pokemon;
  }

  public enum Type {
    pokemon,
    no_pokemon,
    pokemon_with_alert;
  }
}
