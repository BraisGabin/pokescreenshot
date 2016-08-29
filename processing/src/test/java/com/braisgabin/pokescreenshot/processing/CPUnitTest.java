package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CPUnitTest {

  @Test
  public void testCPM_integer() {
    assertThat(CP.CPM(2), is(CP.CPM[2]));
  }

  @Test
  public void testCPM_float() {
    assertThat(CP.CPM(2.5f), is(CP.CPM[3]));
  }
}
