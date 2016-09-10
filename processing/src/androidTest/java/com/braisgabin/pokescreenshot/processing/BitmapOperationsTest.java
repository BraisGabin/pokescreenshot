package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(value = Parameterized.class)
public class BitmapOperationsTest {

  @Parameterized.Parameters
  public static Collection<Integer> data() throws IOException {
    return Arrays.asList(
        Color.WHITE,
        Color.BLACK,
        Color.rgb(50, 50, 50),
        Color.rgb(100, 100, 100),
        Color.rgb(120, 120, 120),
        Color.rgb(150, 150, 150),
        Color.rgb(200, 200, 200),
        Color.rgb(220, 220, 220),
        Color.rgb(200, 0, 100)
    );
  }

  private final int color;
  private Bitmap bitmap1;
  private Bitmap bitmap2;
  private Context context;

  public BitmapOperationsTest(Integer color) {
    this.color = color;
  }

  @Before
  public void setUp() {
    this.bitmap1 = Bitmap.createBitmap(1, 2, Bitmap.Config.ARGB_8888);
    bitmap1.setPixel(0, 0, color);
    bitmap1.setPixel(0, 1, color);
    this.bitmap2 = bitmap1.copy(bitmap1.getConfig(), true);
    this.context = InstrumentationRegistry.getContext();
  }

  @Test
  public void testFilter_black() throws Exception {
    BitmapOperations.filter(context, bitmap1, 0, 150, 200);
    filter(bitmap2, 0, 150, 200);

    assertThat(bitmap1.getPixel(0, 0), is(bitmap2.getPixel(0, 0)));
    assertThat(bitmap1.getPixel(0, 1), is(bitmap2.getPixel(0, 1)));
  }

  static void filter(Bitmap bitmap, int heightCP, int valueCp, int valueNoCp) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int g = range(grey(bitmap.getPixel(x, y)), y <= heightCP ? valueCp : valueNoCp);
        bitmap.setPixel(x, y, Color.rgb(g, g, g));
      }
    }
  }

  static int range(int grey, int value) {
    final int g;
    if (grey <= value) {
      g = 0;
    } else  {
      g = 255;
    }
    return g;
  }

  static int grey(int color) {
    return Math.round((((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + (color & 0xFF)) / 3f);
  }
}
