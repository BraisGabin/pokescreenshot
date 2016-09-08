package com.braisgabin.pokescreenshot.processing;

import java.util.ArrayList;
import java.util.List;

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

  static Pokemon[] getPokemon(Pokemon[] pokemonList, int cp, float lvl) {
    List<Pokemon> pokemon = new ArrayList<>();
    for (Pokemon p : pokemonList) {
      final int minCp = calculateCp(p, lvl, 0, 0, 0);
      final int maxCp = calculateCp(p, lvl, 15, 15, 15);
      if (minCp <= cp && maxCp >= cp) {
        pokemon.add(p);
      }
    }
    return pokemon.toArray(new Pokemon[pokemon.size()]);
  }
}
