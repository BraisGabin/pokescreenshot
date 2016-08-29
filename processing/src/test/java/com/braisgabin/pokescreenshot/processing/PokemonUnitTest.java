package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PokemonUnitTest {

  @Test
  public void testLevel() {
    final float lvl = Pokemon.level(22, (float) Math.toRadians(270 - 103.2647f));
    assertThat(lvl, is(1.5f));
  }
}
