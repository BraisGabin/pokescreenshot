package com.braisgabin.pokescreenshot.processing;

public class Stardust {
  static final int[] STARDUST = {
      200, // lvl 1
      200, // lvl 2
      400, // lvl 3
      400, // lvl 4
      600, // lvl 5
      600, // lvl 6
      800, // lvl 7
      800, // lvl 8
      1000, // lvl 9
      1000, // lvl 10
      1300, // lvl 11
      1300, // lvl 12
      1600, // lvl 13
      1600, // lvl 14
      1900, // lvl 15
      1900, // lvl 16
      2200, // lvl 17
      2200, // lvl 18
      2500, // lvl 19
      2500, // lvl 20
      3000, // lvl 21
      3000, // lvl 22
      3500, // lvl 23
      3500, // lvl 24
      4000, // lvl 25
      4000, // lvl 26
      4500, // lvl 27
      4500, // lvl 28
      5000, // lvl 29
      5000, // lvl 30
      6000, // lvl 31
      6000, // lvl 32
      7000, // lvl 33
      7000, // lvl 34
      8000, // lvl 35
      8000, // lvl 36
      9000, // lvl 37
      9000, // lvl 38
      10000, // lvl 39
      10000, // lvl 40
  };

  public static boolean isStardustCorrect(int stardust) {
    for (int i = 0; i < STARDUST.length; i += 2) {
      if (stardust == STARDUST[i]) {
        return true;
      }
    }
    return false;
  }

  public static float[] stardust2Lvl(int stardust) {
    for (int i = 1; i < STARDUST.length; i += 2) {
      if (stardust == STARDUST[i - 1]) {
        if (i == STARDUST.length - 1) {
          return new float[]{
              i,
              i + 0.5f,
              i + 1.0f,
          };
        } else {
          return new float[]{
              i,
              i + 0.5f,
              i + 1.0f,
              i + 1.5f,
          };
        }
      }
    }
    throw new IllegalArgumentException("Unknown lvl of stardust " + stardust);
  }
}
