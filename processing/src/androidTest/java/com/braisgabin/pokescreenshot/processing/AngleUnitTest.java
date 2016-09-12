package com.braisgabin.pokescreenshot.processing;

import android.graphics.Point;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AngleUnitTest {

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
}
