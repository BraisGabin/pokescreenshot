package com.braisgabin.pokescreenshot.processing;

import android.graphics.Rect;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OcrUnitTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Tess tess;
  private Ocr ocr;

  @Before
  public void setUp() throws Exception {
    this.tess = mock(Tess.class);
    this.ocr = new Ocr(tess);
  }

  @Test
  public void testCp() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");

    assertThat(ocr.cp(), is(100));
    assertThat(ocr.cp(), is(100));
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testCp_error() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("asdf");

    thrown.expect(ScreenshotReader.CpException.class);
    thrown.expectMessage("Error parsing CP:\nasdf");
    ocr.cp();
  }

  @Test
  public void testName() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.name(anyInt())).thenReturn("Pika");

    assertThat(ocr.name(), is("Pika"));
    assertThat(ocr.name(), is("Pika"));
    verify(tess, times(1)).name(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testName_error() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.name(anyInt())).thenReturn("");

    thrown.expect(ScreenshotReader.NameException.class);
    thrown.expectMessage("Error parsing name: No name founded");
    ocr.name();
  }

  @Test
  public void testHp() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.hp(anyInt())).thenReturn("1/50");

    assertThat(ocr.hp(), is(50));
    assertThat(ocr.hp(), is(50));
    verify(tess, times(1)).hp(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testHp_errorNoSlash() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.hp(anyInt())).thenReturn("50");

    thrown.expect(ScreenshotReader.HpException.class);
    thrown.expectMessage("Error parsing HP:\n50");
    ocr.hp();
  }

  @Test
  public void testHp_errorNoNumber() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.hp(anyInt())).thenReturn("50/ab");

    thrown.expect(ScreenshotReader.HpException.class);
    thrown.expectMessage("Error parsing HP:\n50/ab");
    ocr.hp();
  }

  @Test
  public void testWeight_noDecimal() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.weight(anyInt())).thenReturn("Poison 2 kg 1.3 m");

    final Ocr ocr = new Ocr(tess);
    assertThat(ocr.weight(), is(2f));
    assertThat(ocr.weight(), is(2f));
    verify(tess, times(1)).weight(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testWeight_oneDecimal() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.weight(anyInt())).thenReturn("Poison 2.5 kg 1.3 m");

    assertThat(ocr.weight(), is(2.5f));
    assertThat(ocr.weight(), is(2.5f));
    verify(tess, times(1)).weight(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testWeight_twoDecimals() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.weight(anyInt())).thenReturn("Poison 2.51 kg 1.3 m");

    assertThat(ocr.weight(), is(2.51f));
    assertThat(ocr.weight(), is(2.51f));
    verify(tess, times(1)).weight(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testWeight_error() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.weight(anyInt())).thenReturn("asdf");

    thrown.expect(ScreenshotReader.WeightException.class);
    thrown.expectMessage("Error parsing weight:\nasdf");
    ocr.weight();
  }

  @Test
  public void testCandy() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.candy(anyInt())).thenReturn("PIKACHU");

    assertThat(ocr.candy(), is("PIKACHU"));
    assertThat(ocr.candy(), is("PIKACHU"));
    verify(tess, times(1)).candy(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testCandy_error() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.candy(anyInt())).thenReturn("Darth Vader");

    thrown.expect(ScreenshotReader.CandyException.class);
    thrown.expectMessage("Error parsing candy:\nDarth Vader");
    ocr.candy();
  }

  @Test
  public void testStardust() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.stardust(anyInt())).thenReturn("200");

    assertThat(ocr.stardust(), is(200));
    assertThat(ocr.stardust(), is(200));
    verify(tess, times(1)).stardust(anyInt());
    verify(tess, times(1)).cp(any(Rect.class));
  }

  @Test
  public void testStardust_errorNoNumber() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.stardust(anyInt())).thenReturn("asdf");

    thrown.expect(ScreenshotReader.StardustException.class);
    thrown.expectMessage("Error parsing stardust:\nasdf");
    ocr.stardust();
  }

  @Test
  public void testStardust_errorNoCorrectNumber() throws Exception {
    when(tess.cp(any(Rect.class))).thenReturn("100");
    when(tess.stardust(anyInt())).thenReturn("1");

    thrown.expect(ScreenshotReader.StardustException.class);
    thrown.expectMessage("Error parsing stardust:\n1");
    ocr.stardust();
  }
}
