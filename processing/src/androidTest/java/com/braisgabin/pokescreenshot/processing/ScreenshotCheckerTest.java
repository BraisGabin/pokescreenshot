package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScreenshotCheckerTest {

  @Test
  public void testIsPokemonGoScreenshot_480() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/es/140_kabuto.png"));
    assertThat(ScreenshotChecker.isPokemonGoScreenshot(bitmap), is(true));
  }

  @Test
  public void testIsPokemonGoScreenshot_360() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/es/123_scyther.png"));
    assertThat(ScreenshotChecker.isPokemonGoScreenshot(bitmap), is(true));
  }

  @Test
  public void testIsPokemonGoScreenshot_settings() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/settings.png"));
    assertThat(ScreenshotChecker.isPokemonGoScreenshot(bitmap), is(false));
  }

  @Test
  public void testIsPokemonGoScreenshot_googleNow() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/google_now.png"));
    assertThat(ScreenshotChecker.isPokemonGoScreenshot(bitmap), is(false));
  }
}
