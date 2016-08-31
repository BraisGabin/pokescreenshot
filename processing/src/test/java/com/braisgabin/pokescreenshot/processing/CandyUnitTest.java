package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CandyUnitTest {

  @Test
  public void testCandyType() {
    assertThat(Candy.candyType("EEVEE CANDY"), is("EEVEE"));
    assertThat(Candy.candyType("CARAMELO EEVEE"), is("EEVEE"));
    assertThat(Candy.candyType("CARAMELO ASDF"), is(nullValue()));
  }
}