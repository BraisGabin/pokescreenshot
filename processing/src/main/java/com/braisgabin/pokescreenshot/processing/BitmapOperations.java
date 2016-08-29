package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;
import android.graphics.Color;

class BitmapOperations {

  static void filter(Bitmap bitmap, int min, int max) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int g = range(grey(bitmap.getPixel(x, y)), min, max);
        bitmap.setPixel(x, y, Color.rgb(g, g, g));
      }
    }
  }

  static int range(int grey, int min, int max) {
    final int g;
    if (grey <= min) {
      g = 0;
    } else if (grey >= max) {
      g = 255;
    } else {
      g = Math.round(((grey - min) / (float) (max - min)) * 255);
    }
    return g;
  }

  static int grey(int color) {
    return Math.round((((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + (color & 0xFF)) / 3f);
  }
}
