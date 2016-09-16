package com.braisgabin.pokescreenshot.processing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StardustUnitTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testIsStardustCorrect_true() throws Exception {
    assertThat(Stardust.isStardustCorrect(200), is(true));
  }

  @Test
  public void testIsStardustCorrect_false() throws Exception {
    assertThat(Stardust.isStardustCorrect(9), is(false));
  }

  @Test
  public void testStardust2Lvl_200() throws Exception {
    assertThat(Stardust.stardust2Lvl(200), is(new float[]{1f, 1.5f, 2f, 2.5f}));
  }

  @Test
  public void testStardust2Lvl_1000() throws Exception {
    assertThat(Stardust.stardust2Lvl(1000), is(new float[]{9f, 9.5f, 10f, 10.5f}));
  }

  @Test
  public void testStardust2Lvl_10000() throws Exception {
    assertThat(Stardust.stardust2Lvl(10000), is(new float[]{39f, 39.5f, 40f}));
  }

  @Test
  public void testStardust2Lvl_9() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unknown lvl of stardust 9");
    Stardust.stardust2Lvl(9);
  }
}
