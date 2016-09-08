package com.braisgabin.pokescreenshot.processing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuesserUnitTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

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
  public void testCalculateHP() {
    Pokemon pokemon = Pokemon.create(77, "Ponyta", 168, 138, 100, "PONYTA");
    assertThat(Guesser.calculateHp(pokemon, 17, 15), is(63));
  }

  @Test
  public void testCalculateHP_min10() {
    Pokemon pokemon = Pokemon.create(1, "", 100, 100, 100, "");
    assertThat(Guesser.calculateHp(pokemon, 1, 0), is(10));
  }

  @Test
  public void testGetPokemon_eevee() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 482, 63, 17), is(pokemon[0]));
  }

  @Test
  public void testGetPokemon_vaporeon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 1173, 142, 15), is(pokemon[1]));
  }

  @Test
  public void testGetPokemon_jolteon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 736, 64, 13), is(pokemon[2]));
  }

  @Test
  public void testGetPokemon_flareon() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    assertThat(Guesser.getPokemon(pokemon, 1415, 82, 19), is(pokemon[3]));
  }

  @Test
  public void testGetPokemon_none() {
    Pokemon[] pokemon = {
        Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE"),
        Pokemon.create(134, "Vaporeon", 186, 168, 260, "EEVEE"),
        Pokemon.create(135, "Jolteon", 192, 174, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("Unknown pokémon.");
    Guesser.getPokemon(pokemon, 50, 82, 19);
  }

  @Test
  public void testGetPokemon_tooMuch() {
    Pokemon[] pokemon = {
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
        Pokemon.create(136, "Flareon", 238, 178, 130, "EEVEE"),
    };
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("Multiple Pokémon.");
    Guesser.getPokemon(pokemon, 1415, 82, 19);
  }

  @Test
  public void testIv_eevee() {
    final Pokemon pokemon = Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE");
    assertThat(Guesser.iv(pokemon, 482, 63, 17), is(new int[][]{
        {10, 15, 5},
        {11, 13, 5},
        {14, 6, 5},
        {15, 4, 5},
        {10, 14, 6},
        {13, 7, 6},
        {14, 5, 6},
        {15, 3, 6},
    }));
  }

  @Test
  public void testIv_eevee_wrong() {
    final Pokemon pokemon = Pokemon.create(133, "Eevee", 114, 128, 110, "EEVEE");
    assertThat(Guesser.iv(pokemon, 482, 63, 15), is(new int[][]{}));
  }

  @Test
  public void testIv_ponyta() {
    final Pokemon pokemon = Pokemon.create(77, "Ponyta", 168, 138, 100, "PONYTA");
    assertThat(Guesser.iv(pokemon, 732, 63, 17), is(new int[][]{
        {14, 15, 15},
    }));
  }
}
