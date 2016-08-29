package com.braisgabin.pokescreenshot.processing;

import android.graphics.Color;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BitmapOperationsUnitTest {

  @Test
  public void testRange() throws Exception {
    assertThat(BitmapOperations.range(0, 100, 200), is(0));
    assertThat(BitmapOperations.range(50, 100, 200), is(0));
    assertThat(BitmapOperations.range(100, 100, 200), is(0));
    assertThat(BitmapOperations.range(125, 100, 200), is(64));
    assertThat(BitmapOperations.range(150, 100, 200), is(128));
    assertThat(BitmapOperations.range(200, 100, 200), is(255));
    assertThat(BitmapOperations.range(230, 100, 200), is(255));
    assertThat(BitmapOperations.range(255, 100, 200), is(255));
  }

  @Test
  public void testGrey() throws Exception {
    assertThat(BitmapOperations.grey(Color.RED), is(85));
    assertThat(BitmapOperations.grey(Color.WHITE), is(255));
    assertThat(BitmapOperations.grey(Color.BLACK), is(0));
  }
}
