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

  @Test
  public void testGetPokemon_eevee() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 482, 17), is(new Pokemon[]{pokemon[0]}));
  }

  @Test
  public void testGetPokemon_vaporeon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 1173, 15), is(new Pokemon[]{pokemon[1]}));
  }

  @Test
  public void testGetPokemon_jolteon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 736, 13), is(new Pokemon[]{pokemon[2]}));
  }

  @Test
  public void testGetPokemon_flareon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 1415, 19), is(new Pokemon[]{pokemon[1], pokemon[3]}));
  }

  @Test
  public void testGetPokemon_none() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 50, 19), is(new Pokemon[]{}));
  }
}
