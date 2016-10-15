package com.braisgabin.pokescreenshot.processing;

import com.google.auto.value.AutoValue;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
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
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 482, 63, 17), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_vaporeon() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 1173, 142, 15), is(coreStats[1]));
  }

  @Test
  public void testGetPokemon_jolteon() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 736, 64, 13), is(coreStats[2]));
  }

  @Test
  public void testGetPokemon_flareon() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 1415, 82, 19), is(coreStats[3]));
  }

  @Test
  @Ignore("I must find a solution")
  public void testGetPokemon_caterpie() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(62, 66, 90),
        IvImplementation.create(56, 86, 100),
        IvImplementation.create(144, 144, 120),
    };
    assertThat(Guesser.getPokemon(Arrays.asList(coreStats), 150, 54, 16), is(coreStats[0]));
  }

  @Test
  public void testGetPokemon_none() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    thrown.expect(Guesser.UnknownPokemonException.class);
    thrown.expectMessage("Unknown Pókemon with CP: 50, HP: 82, lvl: 19");
    Guesser.getPokemon(Arrays.asList(coreStats), 50, 82, 19);
  }

  @Test
  public void testGetPokemon_none2() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(114, 128, 110),
        IvImplementation.create(186, 168, 260),
        IvImplementation.create(192, 174, 130),
        IvImplementation.create(238, 178, 130),
    };
    thrown.expect(Guesser.UnknownPokemonException.class);
    thrown.expectMessage("Unknown Pókemon with CP: 50, HP: 82, lvl: 19.5");
    Guesser.getPokemon(Arrays.asList(coreStats), 50, 82, 19.5f);
  }

  @Test
  public void testGetPokemon_tooMuch() throws Exception {
    final CoreStats[] coreStats = {
        IvImplementation.create(238, 178, 130),
        IvImplementation.create(238, 178, 130),
    };
    thrown.expect(Guesser.MultiplePokemonException.class);
    thrown.expectMessage("IvImplementation{atk=238, def=178, stam=130} and IvImplementation{atk=238, def=178, stam=130} are possible candidates.");
    Guesser.getPokemon(Arrays.asList(coreStats), 1415, 82, 19);
  }

  @Test
  public void testIv_eevee() {
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
  public void testIv_eevee_wrong() {
    final CoreStats coreStats = IvImplementation.create(114, 128, 110);
    assertThat(Guesser.iv(coreStats, 482, 63, 15), empty());
  }

  @Test
  public void testIv_ponyta() {
    final CoreStats coreStats = IvImplementation.create(168, 138, 100);
    assertThat(Guesser.iv(coreStats, 732, 63, 17), contains(new int[][]{
        {14, 15, 15},
    }));
  }

  // Confirm we don't have this bug:
  // https://www.reddit.com/r/PokemonGOIVs/comments/4x2f6v/some_iv_range_calculators_have_been_using_the/
  @Test
  public void testIv_bulbasur() {
    final CoreStats coreStats = IvImplementation.create(126, 126, 90);
    assertThat(Guesser.iv(coreStats, 321, 45, 10.5f), contains(new int[][]{
        {15, 15, 15},
    }));
  }

  @Test
  public void testLvl_1() throws Exception {
    final int trainerLvl = 10;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(false);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 1))).thenReturn(true);

    final ScreenshotReader reader = mock(ScreenshotReader.class);
    when(reader.stardust()).thenReturn(200);

    assertThat(Guesser.lvl(angle, reader, trainerLvl), is(1f));
  }

  @Test
  public void testLvl_1NoStardust() throws Exception {
    final int trainerLvl = 10;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(false);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 1))).thenReturn(true);

    final ScreenshotReader reader = mock(ScreenshotReader.class);
    when(reader.stardust()).thenThrow(ScreenshotReader.StardustException.class);

    assertThat(Guesser.lvl(angle, reader, trainerLvl), is(1f));
  }

  @Test
  public void testLvl_lvlNotAllowed() throws Exception {
    final int trainerLvl = 2;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(false);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 4))).thenReturn(true);

    final ScreenshotReader reader = mock(ScreenshotReader.class);
    when(reader.stardust()).thenReturn(400);

    thrown.expect(Guesser.UnknownPokemonLvl.class);
    thrown.expectMessage("Impossible to detect the level bubble.");
    Guesser.lvl(angle, reader, trainerLvl);
  }

  @Test
  public void testLvl_lvlNotAllowedNoStardust() throws Exception {
    final int trainerLvl = 2;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(false);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 4))).thenReturn(true);

    final ScreenshotReader reader = mock(ScreenshotReader.class);
    when(reader.stardust()).thenThrow(ScreenshotReader.StardustException.class);

    thrown.expect(Guesser.UnknownPokemonLvl.class);
    thrown.expectMessage("Impossible to detect the level bubble.");
    Guesser.lvl(angle, reader, trainerLvl);
  }

  @Test
  public void testLvl_lvlOutOfRange() throws Exception {
    final int trainerLvl = 2;
    final Angle angle = mock(Angle.class);
    when(angle.isBall(anyDouble())).thenReturn(false);
    when(angle.isBall(CP.lvl2Radian(trainerLvl, 3))).thenReturn(true);

    final ScreenshotReader reader = mock(ScreenshotReader.class);
    when(reader.stardust()).thenReturn(200);

    thrown.expect(Guesser.UnknownPokemonLvl.class);
    thrown.expectMessage("Impossible to detect the level bubble.");
    Guesser.lvl(angle, reader, trainerLvl);
  }

  @AutoValue
  abstract static class IvImplementation implements CoreStats {
    static CoreStats create(int atk, int def, int stam) {
      return new AutoValue_GuesserUnitTest_IvImplementation(atk, def, stam);
    }

    @Override
    public abstract int atk();

    @Override
    public abstract int def();

    @Override
    public abstract int stam();
  }
}
