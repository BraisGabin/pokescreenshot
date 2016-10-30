package com.braisgabin.pokescreenshot.processing;

import com.google.auto.value.AutoValue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuesserUnitTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testCalculateCp() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.calculateCp(coreStats, 17, 14, 15, 15), is(732));
  }

  @Test
  public void testCalculateCp_min10() {
    final CoreStats coreStats = IvImplementation.create(100, 100, 100);
    assertThat(Guesser.calculateCp(coreStats, 1, 0, 0, 0), is(10));
  }

  @Test
  public void testCalculateHP() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.calculateHp(coreStats, 17, 15), is(63));
  }

  @Test
  public void testCalculateHP_min10() {
    final CoreStats coreStats = IvImplementation.create(100, 100, 100);
    assertThat(Guesser.calculateHp(coreStats, 1, 0), is(10));
  }

  @Test
  public void testGetPokemon_eevee() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(482, 63, 25), 17), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_vaporeon() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(1173, 142, 0), 15), is(coreStats[1]));
  }

  @Test
  public void testGetPokemon_jolteon() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(736, 64, 0), 13), is(coreStats[2]));
  }

  @Test
  public void testGetPokemon_flareon() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(1415, 82, 0), 19), is(coreStats[3]));
  }

  @Test
  public void testGetPokemon_caterpie() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(62, 66, 90, 12),
        Pokemon.create(56, 86, 100, 50),
        Pokemon.create(144, 144, 120, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(150, 54, 12), 16), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_cubone() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(102, 150, 100, 50),
        Pokemon.create(140, 202, 120, 0),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(36, 16, 50), 2), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_none() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    thrown.expect(Guesser.UnknownPokemonException.class);
    thrown.expectMessage("Unknown Pókemon with CP: 50, HP: 82, lvl: 19");
    Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(50, 82, 25), 19);
  }

  @Test
  public void testGetPokemon_none2() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(114, 128, 110, 25),
        Pokemon.create(186, 168, 260, 0),
        Pokemon.create(192, 174, 130, 0),
        Pokemon.create(238, 178, 130, 0),
    };
    thrown.expect(Guesser.UnknownPokemonException.class);
    thrown.expectMessage("Unknown Pókemon with CP: 50, HP: 82, lvl: 19.5");
    Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(50, 82, 25), 19.5f);
  }

  @Test
  public void testGetPokemon_tooMuch() throws Exception {
    final Pokemon[] coreStats = {
        Pokemon.create(238, 178, 130, 12),
        Pokemon.create(238, 178, 130, 12),
    };
    thrown.expect(Guesser.MultiplePokemonException.class);
    thrown.expectMessage(coreStats[0].toString() + " and " + coreStats[1] + " are possible candidates.");
    Guesser.getPokemon(Arrays.asList(coreStats), Reader.create(1415, 82, 12), 19);
  }

  @Test
  public void testIv_eevee() throws Exception {
    final CoreStats coreStats = IvImplementation.create(114, 128, 110);
    assertThat(Guesser.iv(coreStats, 482, 63, 17), contains(new int[][]{
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
  public void testIv_eeveeWrong() throws Exception {
    final CoreStats coreStats = IvImplementation.create(114, 128, 110);
    thrown.expect(Guesser.NoIvPossibilities.class);
    thrown.expectMessage("There is not any IV combination for " + coreStats + " with cp = 482, hp = 63 and lvl = 15");
    Guesser.iv(coreStats, 482, 63, 15);
  }

  @Test
  public void testIv_ponyta() throws Exception {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.iv(coreStats, 732, 63, 17), contains(new int[][]{
        {14, 15, 15},
    }));
  }

  // Confirm we don't have this bug:
  // https://www.reddit.com/r/PokemonGOIVs/comments/4x2f6v/some_iv_range_calculators_have_been_using_the/
  @Test
  public void testIv_bulbasur() throws Exception {
    final CoreStats coreStats = IvImplementation.create(126, 126, 90);
    assertThat(Guesser.iv(coreStats, 321, 45, 10.5f), contains(new int[][]{
        {15, 15, 15},
    }));
  }

  @Test
  public void testPossiblePokemonLvl_1() throws Exception {
    assertThat(Arrays.asList(Guesser.possiblePokemonLvl(1)), contains(new float[]{1, 1.5f, 2, 2.5f}));
  }

  @Test
  public void testPossiblePokemonLvl_2() throws Exception {
    assertThat(Arrays.asList(Guesser.possiblePokemonLvl(2)), contains(new float[]{1, 1.5f, 2, 2.5f, 3, 3.5f}));
  }

  @Test
  public void testPossiblePokemonLvl_50() throws Exception {
    final float[] lvls = Guesser.possiblePokemonLvl(50);
    assertThat(lvls[0], is(1f));
    assertThat(lvls.length, is(40 * 2 - 1));
    assertThat(lvls[lvls.length - 1], is(40f));
  }

  @Test
  public void testLvl_1() throws Exception {
    final int trainerLvl = 3;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(0.2);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 1))).thenReturn(0.8);

    assertThat(Guesser.lvl(angle, trainerLvl, new float[]{1, 1.5f, 2, 2.5f}), is(1f));
  }

  @Test
  public void testLvl_lvlNotAllowed() throws Exception {
    final int trainerLvl = 2;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(0.2);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 4))).thenReturn(0.8);

    thrown.expect(Guesser.UnknownPokemonLvl.class);
    thrown.expectMessage("Impossible to detect the level bubble.");
    Guesser.lvl(angle, trainerLvl, new float[]{3, 3.5f, 4, 4.5f});
  }

  @Test
  public void testLvl_lvlOutOfRange() throws Exception {
    final int trainerLvl = 2;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(0.2);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 3))).thenReturn(0.8);

    thrown.expect(Guesser.UnknownPokemonLvl.class);
    thrown.expectMessage("Impossible to detect the level bubble.");
    Guesser.lvl(angle, trainerLvl, new float[]{1, 1.5f, 2, 2.5f});
  }

  @AutoValue
  abstract static class IvImplementation implements CoreStats {
    static CoreStats create(int atk, int def, int stam) {
      return new AutoValue_GuesserUnitTest_IvImplementation(atk, def, stam);
    }
  }

  @AutoValue
  abstract static class Reader implements ScreenshotReader {
    static Reader create(int cp, int hp, int evolveCandy) {
      return new AutoValue_GuesserUnitTest_Reader(cp, "", hp, "", 0, evolveCandy);
    }

    @Override
    public abstract int cp();

    @Override
    public abstract String name();

    @Override
    public abstract int hp();

    @Override
    public abstract String candy();

    @Override
    public abstract int stardust();

    @Override
    public abstract int evolveCandy();
  }

  @AutoValue
  abstract static class Pokemon implements CoreStats, EvolveCandy {
    static Pokemon create(int atk, int def, int stam, int evolveCandy) {
      return new AutoValue_GuesserUnitTest_Pokemon(atk, def, stam, evolveCandy);
    }
  }
}
