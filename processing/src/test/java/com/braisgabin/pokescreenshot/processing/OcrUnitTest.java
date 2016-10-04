package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OcrUnitTest {

  @Test
  public void testCp() throws Exception {
    final Tess tess = mock(Tess.class);
    when(tess.cp()).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.cp(), is(100));
  }

  @Test
  public void testName() throws Exception {
    final Tess tess = mock(Tess.class);
    when(tess.name()).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.name(), is("Pika"));
  }

  @Test
  public void testHp() throws Exception {
    final Tess tess = mock(Tess.class);
    when(tess.hp()).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.hp(), is(100));
  }

  @Test
  public void testCandy() throws Exception {
    final Tess tess = mock(Tess.class);
    when(tess.candy()).thenReturn("Pika");

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.candy(), is("Pika"));
  }

  @Test
  public void testStardust() throws Exception {
    final Tess tess = mock(Tess.class);
    when(tess.stardust()).thenReturn(100);

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.stardust(), is(100));
  }
}
