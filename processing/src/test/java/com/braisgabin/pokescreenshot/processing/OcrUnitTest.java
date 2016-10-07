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
  private Ocr ocr;

  @Before
  public void setUp() throws Exception {
    this.tess = mock(Tess.class);
    when(tess.cp(any(Rect.class))).thenReturn(100);
    when(tess.name(anyInt())).thenReturn("Pika");
    when(tess.hp(anyInt())).thenReturn(50);
    when(tess.candy(anyInt())).thenReturn("PIKACHU");
    when(tess.stardust(anyInt())).thenReturn(200);
    
    this.ocr = new Ocr(tess);
  }

  @Test
  public void testCp() throws Exception {
    assertThat(ocr.cp(), is(100));
    assertThat(ocr.cp(), is(100));
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testName() throws Exception {
    assertThat(ocr.name(), is("Pika"));
    assertThat(ocr.name(), is("Pika"));
    verify(tess, times(1)).name(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testHp() throws Exception {
    assertThat(ocr.hp(), is(50));
    assertThat(ocr.hp(), is(50));
    verify(tess, times(1)).hp(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testCandy() throws Exception {
    assertThat(ocr.candy(), is("PIKACHU"));
    assertThat(ocr.candy(), is("PIKACHU"));
    verify(tess, times(1)).candy(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testStardust() throws Exception {
    assertThat(ocr.stardust(), is(200));
    assertThat(ocr.stardust(), is(200));
    verify(tess, times(1)).stardust(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }
}
