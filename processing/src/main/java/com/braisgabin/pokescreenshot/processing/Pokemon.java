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

  public static float radian(int trainerLvl, float pokemonLvl) {
    // Formula extracted from:
    // https://www.reddit.com/r/TheSilphRoad/comments/4uz4tl/determining_pokemon_level_from_the_semicircle/d5uisb6
    final float degree = (CPM(pokemonLvl) - CPM(1)) * 202.04f / CPM(trainerLvl);
    return (float) Math.toRadians(180 - degree);
  }

  private int pc;
  private int ps;
  private int stardustToPowerUp;
  private String candyName;
  private String name;


  public Pokemon(int pc, int ps, int stardust, String candy, String name) {
    this.pc = pc;
    this.ps = ps;
    this.stardustToPowerUp = stardust;
    this.candyName = candy;
    this.name = name;
  }

  public int getPc() {
    return pc;
  }

  public int getPs() {
    return ps;
  }

  public int getStardust() {
    return stardustToPowerUp;
  }

  public String getCandyName() {
    return candyName;
  }

  public String getName() {
    return name;
  }
}
