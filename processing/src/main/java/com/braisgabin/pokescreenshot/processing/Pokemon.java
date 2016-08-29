package com.braisgabin.pokescreenshot.processing;

import static com.braisgabin.pokescreenshot.processing.CP.CPM;
import static com.braisgabin.pokescreenshot.processing.CP.CPM2Lvl;

public class Pokemon {
  public static float level(int trainerLvl, float radian) {
    // Formula extracted from:
    // https://www.reddit.com/r/TheSilphRoad/comments/4uz4tl/determining_pokemon_level_from_the_semicircle/d5uisb6
    final float degree = (float) (180 - Math.toDegrees(radian));
    final float cpm = degree * CPM(trainerLvl) / 202.04f + CPM(1);
    return CPM2Lvl(cpm);
  }
}
