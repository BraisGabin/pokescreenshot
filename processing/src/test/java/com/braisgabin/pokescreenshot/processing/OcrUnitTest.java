package com.braisgabin.pokescreenshot.processing;

import android.graphics.Rect;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OcrUnitTest {
  private Tess tess;

  @Before
  public void setUp() {
    this.tess = mock(Tess.class);
  }

  @Test
  public void testCp() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.cp(), is(100));
  }

  @Test
  public void testCp_twice() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    ocr.cp();
    ocr.cp();
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testName() throws Exception {
    when(tess.name(anyInt())).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.name(), is("Pika"));
  }

  @Test
  public void testName_twice() throws Exception {
    when(tess.name(anyInt())).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    ocr.name();
    ocr.name();
    verify(tess, times(1)).name(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testHp() throws Exception {
    when(tess.hp(anyInt())).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.hp(), is(100));
  }

  @Test
  public void testHp_twice() throws Exception {
    when(tess.hp(anyInt())).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    ocr.hp();
    ocr.hp();
    verify(tess, times(1)).hp(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testCandy() throws Exception {
    when(tess.candy(anyInt())).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.candy(), is("Pika"));
  }

  @Test
  public void testCandy_twice() throws Exception {
    when(tess.candy(anyInt())).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    ocr.candy();
    ocr.candy();
    verify(tess, times(1)).candy(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testStardust() throws Exception {
    when(tess.stardust(anyInt())).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.stardust(), is(100));
  }

  @Test
  public void testStardust_twice() throws Exception {
    when(tess.stardust(anyInt())).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    ocr.stardust();
    ocr.stardust();
    verify(tess, times(1)).stardust(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }
}
