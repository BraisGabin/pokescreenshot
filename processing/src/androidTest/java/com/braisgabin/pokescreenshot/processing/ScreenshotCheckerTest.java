package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScreenshotCheckerTest {

  @Test
  public void testIsPokemonGoScreenshot_480() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/es/16_pidgey.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.pokemon));
  }

  @Test
  @Ignore("No screenshot yet")
  public void testIsPokemonGoScreenshot_360() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/es/123_scyther.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.pokemon));
  }

  @Test
  public void testIsPokemonGoScreenshot_settings() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/settings.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.no_pokemon));
  }

  @Test
  public void testIsPokemonGoScreenshot_googleNow() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/google_now.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.no_pokemon));
  }

  @Test
  public void testIsPokemonGoScreenshot_snackbar() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/pokemon_with_snackbar.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.pokemon));
  }

  @Test
  public void testIsPokemonGoScreenshot_noInternet() throws Exception {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open("screenshots/no_internet.png"));
    assertThat(ScreenshotChecker.getScreenshotType(bitmap), is(ScreenshotChecker.Type.pokemon_with_alert));
  }
}
