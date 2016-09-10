package com.braisgabin.pokescreenshot.processing;

import java.util.ArrayList;
import java.util.List;

import static com.braisgabin.pokescreenshot.processing.CP.CPM;
import static java.lang.Math.floor;
import static java.lang.Math.max;
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

  public static <T extends CoreStats> T getPokemon(Iterable<T> coreStatsList, int cp, int hp, float lvl) {
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
          throw new RuntimeException("Multiple Pokémon.");
        }
      }
    }
    if (coreStats == null) {
      throw new RuntimeException("Unknown pokémon.");
    }
    return coreStats;
  }

  public static int[][] iv(CoreStats coreStats, int cp, int hp, float lvl) {
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
    return ivs.toArray(new int[ivs.size()][]);
  }
}
