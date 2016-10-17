package com.braisgabin.pokescreenshot.processing;

import android.graphics.Point;

import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

import java.math.BigDecimal;

@CsvDataType
class Screenshot {
  @CsvField(pos = 1)
  private String file;

  @CsvField(pos = 2)
  private int initPointX;

  @CsvField(pos = 3)
  private int initPointY;

  @CsvField(pos = 4)
  private int trainerLvl;

  @CsvField(pos = 5)
  private BigDecimal lvl;

  @CsvField(pos = 6)
  private int cp;

  @CsvField(pos = 7)
  private int hp;

  @CsvField(pos = 8)
  private int stardust;

  @CsvField(pos = 9)
  private String candyType;

  @CsvField(pos = 10)
  private Integer evolveCandy;

  @CsvField(pos = 11)
  private String name;

  public String file() {
    return "screenshots/" + file;
  }

  public Point initialPoint() {
    return new Point(initPointX, initPointY);
  }

  public int getTrainerLvl() {
    return trainerLvl;
  }

  public float getLvl() {
    return lvl.floatValue();
  }

  public int getCp() {
    return cp;
  }

  public int getHp() {
    return hp;
  }

  public int getStardust() {
    return stardust;
  }

  public String getCandy() {
    return candyType;
  }

  public int getEvolveCandy() {
    return evolveCandy == null ? 0 : evolveCandy;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return file().substring("screenshots/".length());
  }
}
