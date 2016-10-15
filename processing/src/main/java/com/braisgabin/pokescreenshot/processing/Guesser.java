package com.braisgabin.pokescreenshot.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.braisgabin.pokescreenshot.processing.CP.CPM;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Guesser {
  static int calculateCp(CoreStats coreStats, float lvl, int atk, int def, int stam) {
    atk += coreStats.atk();
    def += coreStats.def();
    stam += coreStats.stam();
    float CPM = CPM(lvl);
    // Formula extracted from: https://pokemongo.gamepress.gg/pokemon-stats-advanced
    return max(10, (int) round(floor(((atk) * sqrt(def) * sqrt(stam) * CPM * CPM) / 10)));
  }

  static int calculateHp(CoreStats pokemon, float lvl, int stam) {
    return max(10, (int) floor(CPM(lvl) * (pokemon.stam() + stam)));
  }

  public static <T extends CoreStats> T getPokemon(Iterable<T> coreStatsList, int cp, int hp, float lvl) throws UnknownPokemonException, MultiplePokemonException {
    T coreStats = null;
    for (T cs : coreStatsList) {
      final int minCp = calculateCp(cs, lvl, 0, 0, 0);
      final int maxCp = calculateCp(cs, lvl, 15, 15, 15);
      final int minHp = calculateHp(cs, lvl, 0);
      final int maxHp = calculateHp(cs, lvl, 15);
      if (minCp <= cp && maxCp >= cp && minHp <= hp && maxHp >= hp) {
        if (coreStats == null) {
          coreStats = cs;
        } else {
          throw new MultiplePokemonException(String.format(Locale.US, "%s and %s are possible candidates.", coreStats.toString(), cs.toString()));
        }
      }
    }
    if (coreStats == null) {
      throw new UnknownPokemonException(String.format(Locale.US, "Unknown Pókemon with CP: %d, HP: %d, lvl: %.1f", cp, hp, lvl));
    }
    return coreStats;
  }

  public static List<int[]> iv(CoreStats coreStats, int cp, int hp, float lvl) {
    final List<int[]> ivs = new ArrayList<>();
    for (int stam = 0; stam < 16; stam++) {
      if (hp == calculateHp(coreStats, lvl, stam)) {
        for (int atk = 0; atk < 16; atk++) {
          for (int def = 0; def < 16; def++) {
            if (cp == calculateCp(coreStats, lvl, atk, def, stam)) {
              ivs.add(new int[]{atk, def, stam});
            }
          }
        }
      }
    }
    return ivs;
  }

  // FIXME this method doesn't have tests.
  public static float lvl(Angle angle, ScreenshotReader screenshotReader, int trainerLvl) throws ScreenshotReader.CpException, Angle.InitialPointException, UnknownPokemonLvl {
    float[] lvls;
    try {
      lvls = Stardust.stardust2Lvl(screenshotReader.stardust());
    } catch (ScreenshotReader.StardustException e) {
      lvls = possiblePokemonLvl(trainerLvl);
    }
    return lvl(angle, trainerLvl, lvls);
  }

  static float[] possiblePokemonLvl(int trainerLvl) {
    final float[] lvls = new float[min((trainerLvl + 1) * 2, 40 * 2 - 1)];
    for (int i = 0; i < lvls.length; i++) {
      lvls[i] = 0.5f * i + 1f;
    }
    return lvls;
  }

  static float lvl(Angle angle, int trainerLvl, float[] lvls) throws Angle.InitialPointException, UnknownPokemonLvl {
    float pokemonLvl = 0;
    double perfectionLvl = 0;
    for (float lvl : lvls) {
      if (lvl <= trainerLvl + 1.5f) {
        final double radian = CP.lvl2Radian(trainerLvl, lvl);
        final double perfection = angle.isBall(radian);
        if (perfection == 1) {
          return lvl;
        }
        if (perfection > perfectionLvl) {
          pokemonLvl = lvl;
          perfectionLvl = perfection;
        }
      }
    }
    if (perfectionLvl >= 0.5) {
      return pokemonLvl;
    }
    throw new UnknownPokemonLvl("Impossible to detect the level bubble.");
  }

  public static abstract class Exception extends ProcessingException {

    public Exception(String message) {
      super(message);
    }
  }

  public static class UnknownPokemonException extends Exception {
    public UnknownPokemonException(String message) {
      super(message);
    }
  }

  public static class MultiplePokemonException extends Exception {
    public MultiplePokemonException(String message) {
      super(message);
    }
  }

  public static class UnknownPokemonLvl extends Exception {
    public UnknownPokemonLvl(String message) {
      super(message);
    }
  }
}
