package com.braisgabin.pokescreenshot.processing;

import static com.braisgabin.pokescreenshot.processing.CP.CPM;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Guesser {
  static int calculateCp(Pokemon pokemon, float lvl, int atk, int def, int stam) {
    atk += pokemon.atk();
    def += pokemon.def();
    stam += pokemon.stam();
    float CPM = CPM(lvl);
    // Formula extracted from: https://pokemongo.gamepress.gg/pokemon-stats-advanced
    return max(10, (int) round(floor(((atk) * sqrt(def) * sqrt(stam) * CPM * CPM) / 10)));
  }

  static int calculateHp(Pokemon pokemon, float lvl, int stam) {
    return max(10, (int) floor(CPM(lvl) * (pokemon.stam() + stam)));
  }

  static Pokemon getPokemon(Pokemon[] pokemonList, int cp, int hp, float lvl) {
    Pokemon pokemon = null;
    for (Pokemon p : pokemonList) {
      final int minCp = calculateCp(p, lvl, 0, 0, 0);
      final int maxCp = calculateCp(p, lvl, 15, 15, 15);
      final int minHp = calculateHp(p, lvl, 0);
      final int maxHp = calculateHp(p, lvl, 15);
      if (minCp <= cp && maxCp >= cp && minHp <= hp && maxHp >= hp) {
        if (pokemon == null) {
          pokemon = p;
        } else {
          throw new RuntimeException("Multiple Pokémon.");
        }
      }
    }
    if (pokemon == null) {
      throw new RuntimeException("Unknown pokémon.");
    }
    return pokemon;
  }
}