package com.braisgabin.pokescreenshot.processing;

import static java.lang.Math.PI;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

public class CP {
  // Generated with: https://gist.github.com/BraisGabin/42ed1f2be736831323eb3cafcd4cdafd
  final static float[] CPM = {
      0.094f, // lvl 1
      0.13513744f, // lvl 1.5
      0.16639787f, // lvl 2
      0.19265091f, // lvl 2.5
      0.21573247f, // lvl 3
      0.23657265f, // lvl 3.5
      0.25572005f, // lvl 4
      0.27353036f, // lvl 4.5
      0.29024988f, // lvl 5
      0.3060574f, // lvl 5.5
      0.3210876f, // lvl 6
      0.33544505f, // lvl 6.5
      0.34921268f, // lvl 7
      0.36245772f, // lvl 7.5
      0.3752356f, // lvl 8
      0.3875924f, // lvl 8.5
      0.39956728f, // lvl 9
      0.41119355f, // lvl 9.5
      0.4225f, // lvl 10
      0.43292642f, // lvl 10.5
      0.44310755f, // lvl 11
      0.45305994f, // lvl 11.5
      0.4627984f, // lvl 12
      0.47233608f, // lvl 12.5
      0.48168495f, // lvl 13
      0.4908558f, // lvl 13.5
      0.49985844f, // lvl 14
      0.50870174f, // lvl 14.5
      0.51739395f, // lvl 15
      0.5259425f, // lvl 15.5
      0.5343543f, // lvl 16
      0.54263574f, // lvl 16.5
      0.5507927f, // lvl 17
      0.55883056f, // lvl 17.5
      0.5667545f, // lvl 18
      0.5745691f, // lvl 18.5
      0.5822789f, // lvl 19
      0.5898879f, // lvl 19.5
      0.5974f, // lvl 20
      0.60482365f, // lvl 20.5
      0.6121573f, // lvl 21
      0.61940414f, // lvl 21.5
      0.6265671f, // lvl 22
      0.6336492f, // lvl 22.5
      0.64065295f, // lvl 23
      0.647581f, // lvl 23.5
      0.65443563f, // lvl 24
      0.66121924f, // lvl 24.5
      0.667934f, // lvl 25
      0.6745819f, // lvl 25.5
      0.6811649f, // lvl 26
      0.6876849f, // lvl 26.5
      0.69414365f, // lvl 27
      0.7005429f, // lvl 27.5
      0.7068842f, // lvl 28
      0.7131691f, // lvl 28.5
      0.7193991f, // lvl 29
      0.72557557f, // lvl 29.5
      0.7317f, // lvl 30
      0.73474103f, // lvl 30.5
      0.7377695f, // lvl 31
      0.7407856f, // lvl 31.5
      0.74378943f, // lvl 32
      0.7467812f, // lvl 32.5
      0.74976104f, // lvl 33
      0.7527291f, // lvl 33.5
      0.7556855f, // lvl 34
      0.7586304f, // lvl 34.5
      0.76156384f, // lvl 35
      0.7644861f, // lvl 35.5
      0.76739717f, // lvl 36
      0.7702973f, // lvl 36.5
      0.7731865f, // lvl 37
      0.77606493f, // lvl 37.5
      0.77893275f, // lvl 38
      0.7817901f, // lvl 38.5
      0.784637f, // lvl 39
      0.7874736f, // lvl 39.5
      0.7903f, // lvl 40
  };

  public static float CPM(float lvl) {
    return CPM[Math.round(lvl * 2) - 2];
  }

  static float CPM2Lvl(double cpm) {
    double delta = Double.MAX_VALUE;
    int index = 0;
    for (int i = 0; i < CPM.length; i++) {
      double d = Math.abs(cpm - CPM[i]);
      if (d < delta) {
        delta = d;
        index = i;
      } else {
        break;
      }
    }
    return (index + 2) / 2f;
  }

  public static double lvl2Radian(int trainerLvl, float pokemonLvl) {
    // Formula extracted from:
    // https://www.reddit.com/r/pokemongodev/comments/50z4sx/arc_angle_to_pokemon_level_formula_used_by/
    if (pokemonLvl == 1) {
      // This is a hack. We can't calculate the correct initial point of lvl 1.
      return toRadians(180 - 2.1);
    }
    final double v = (CPM(pokemonLvl) - (double) CPM(1)) / (CPM(min(40, trainerLvl + 2)) - (double) CPM(1));
    return PI - v * PI;
  }
}
