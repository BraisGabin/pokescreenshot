package com.braisgabin.pokescreenshot.processing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuesserUnitTest {

  @Test
  public void testCalculateCp() {
    Pokemon pokemon = Pokemon.create(77, "Ponyta", 168, 138, 100, "PONYTA");
    assertThat(Guesser.calculateCp(pokemon, 17, 14, 15, 15), is(732));
  }

  @Test
  public void testCalculateCp_min10() {
    Pokemon pokemon = Pokemon.create(1, "", 100, 100, 100, "");
    assertThat(Guesser.calculateCp(pokemon, 1, 0, 0, 0), is(10));
  }
}
