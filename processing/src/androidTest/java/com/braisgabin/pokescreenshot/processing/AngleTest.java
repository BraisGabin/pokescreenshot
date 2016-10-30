package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

@RunWith(value = Parameterized.class)
public class AngleTest {

  @Parameterized.Parameters(name = "{0}[{index}")
  public static Collection<Screenshot> data() throws IOException {
    final Context context = InstrumentationRegistry.getContext();
    final AssetManager assets = context.getAssets();
    return Screenshot.loadScreenshots(assets);
  }

  private final Screenshot screenshot;
  private Angle angle;
  private int width;

  public AngleTest(Screenshot screenshot) {
    this.screenshot = screenshot;
  }

  @Before
  public void setUp() throws IOException {
    final AssetManager assets = InstrumentationRegistry.getContext().getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open(screenshot.file()));
    this.angle = new Angle(bitmap);
    this.width = bitmap.getWidth();
  }

  @Test
  public void testInitialTest() throws Exception {
    Point point = angle.initialPoint();
    assertThat(point, is(screenshot.initialPoint()));
  }

  @Test
  public void testIsBall() throws Exception {
    final Point initialPoint = screenshot.initialPoint();
    final Point center = Angle.center(screenshot.initialPoint(), width);
    final int radius = Angle.radius(initialPoint, center);
    final int trainerLvl = screenshot.getTrainerLvl();
    final float pokemonLvl = screenshot.getLvl();
    final float[] lvls = Stardust.stardust2Lvl(screenshot.getStardust());
    for (float lvl : lvls) {
      final double radian = CP.lvl2Radian(trainerLvl, lvl);
      final Matcher<Double> matcher = lvl == pokemonLvl ? greaterThan(0.5) : lessThan(0.4);
      final double perfection = angle.isBall(radian, center, radius);
      assertThat(perfection, is(matcher));
    }
  }
}
