package com.braisgabin.pokescreenshot.processing;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;

import com.google.auto.value.AutoValue;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static java.lang.Math.PI;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

@RunWith(value = Parameterized.class)
public class AngleTest {
  @Parameterized.Parameters
  public static Collection<Screenshot> data() {
    return Arrays.asList(
        Screenshot.create("140_kabuto.png", 174, 854, 128.4),
        Screenshot.create("136_flareon.png", 174, 854, 23.3)
    );
  }

  private final Screenshot screenshot;
  private final Bitmap bitmap;

  public AngleTest(Screenshot screenshot) throws Exception {
    this.screenshot = screenshot;
    final AssetManager assets = InstrumentationRegistry.getContext().getAssets();
    this.bitmap = BitmapFactory.decodeStream(assets.open(screenshot.file()));
  }

  @Test
  public void testInitialTest() {
    Point point = Angle.initialPoint(bitmap);
    assertThat(point, is(screenshot.initialPoint()));
  }

  @Test
  public void testCenter() {
    final Point center = Angle.center(new Point(5, 7), 50);
    assertThat(center.x, is(25));
    assertThat(center.y, is(7));
  }

  @Test
  public void testRadius() {
    final int radius = Angle.radius(new Point(5, 7), new Point(25, 7));
    assertThat(radius, is(20));
  }

  @Test
  public void testRadian() {
    final double radian = Angle.radian(new Point(720, 854), 545, bitmap);
    assertThat(radian, moreLess(screenshot.radian()));
  }

  private Matcher<? super Double> moreLess(double radian) {
    final double error = .25 * PI / 180;
    return is(both(greaterThan(radian - error)).and(lessThan(radian + error)));
  }

  @AutoValue
  static abstract class Screenshot {
    static Screenshot create(String file, int x, int y, double degree) {
      return new AutoValue_AngleTest_Screenshot(file, new Point(x, y), degree * PI / 180.);
    }

    abstract String file();

    abstract Point initialPoint();

    abstract double radian();
  }
}
