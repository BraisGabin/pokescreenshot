package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static com.braisgabin.pokescreenshot.processing.CP.lvl2Radian;
import static com.braisgabin.pokescreenshot.processing.CP.radian2Lvl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class CPUnitTest {

  @Test
  public void testCPM_integer() {
    assertThat(CP.CPM(2), is(CP.CPM[2]));
  }

  @Test
  public void testCPM_float() {
    assertThat(CP.CPM(2.5f), is(CP.CPM[3]));
  }

  @Test
  public void testCPM2Lvl_3() {
    assertThat(CP.CPM2Lvl(CP.CPM[4] - 0.001f), is(3f));
  }

  @Test
  public void testCPM2Lvl_3_5() {
    assertThat(CP.CPM2Lvl(CP.CPM[5] + 0.001f), is(3.5f));
  }

  @Test
  public void testRadian2Lvl_2_2() {
    assertThat(radian2Lvl(2, Math.toRadians(180 - 80.6)), is(2f));
  }

  @Test
  public void testRadian2Lvl_2_2_5() {
    assertThat(radian2Lvl(2, Math.toRadians(180 - 109.8)), is(2.5f));
  }

  @Test
  public void testRadian2Lvl_2_3() {
    assertThat(radian2Lvl(2, Math.toRadians(180 - 135.5)), is(3f));
  }

  @Test
  public void testRadian2Lvl_22_1_5() {
    assertThat(radian2Lvl(22, Math.toRadians(180 - 13.3)), is(1.5f));
  }

  @Test
  public void testLvl2Radian_2_2() {
    assertThat(lvl2Radian(2, 2f), is(closeTo(Math.toRadians(180 - 80.6), Math.toRadians(0.1))));
  }

  @Test
  public void testLvl2Radian_2_2_5() {
    assertThat(lvl2Radian(2, 2.5f), is(closeTo(Math.toRadians(180 - 109.8), Math.toRadians(0.1))));
  }

  @Test
  public void testLvl2Radian_2_3() {
    assertThat(lvl2Radian(2, 3f), is(closeTo(Math.toRadians(180 - 135.5), Math.toRadians(0.1))));
  }

  @Test
  public void testLvl2Radian_22_1_5() {
    assertThat(lvl2Radian(22, 1.5f), is(closeTo(Math.toRadians(180 - 13.3), Math.toRadians(0.1))));
  }
}
