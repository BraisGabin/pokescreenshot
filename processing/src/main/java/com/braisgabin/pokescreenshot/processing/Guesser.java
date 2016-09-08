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
}
