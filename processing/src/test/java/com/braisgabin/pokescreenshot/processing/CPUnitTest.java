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
  public void testCPM2Lvl() {
    assertThat(CP.CPM2Lvl(CP.CPM[4] - 0.001f), is(3f));
    assertThat(CP.CPM2Lvl(CP.CPM[5] + 0.001f), is(3.5f));
  }

  @Test
  public void testRadian2Lvl() {
    assertThat(radian2Lvl(22, Math.toRadians(270 - 103.2647f)), is(1.5f));
  }

  @Test
  public void testLvl2Radian() {
    assertThat(lvl2Radian(22, 1.5f), is(closeTo(Math.toRadians(270 - 103.2647f), 0.00001)));
  }
}
