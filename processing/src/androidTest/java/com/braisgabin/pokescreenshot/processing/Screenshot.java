package com.braisgabin.pokescreenshot.processing;

import android.graphics.Point;

import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

import java.math.BigDecimal;

import static com.braisgabin.pokescreenshot.processing.CP.*;

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
  private int stardus;

  @CsvField(pos = 9)
  private String candyType;

  @CsvField(pos = 10)
  private String name;

  public String file() {
    return "screenshots/" + file;
  }

  public Point initialPoint() {
    return new Point(initPointX, initPointY);
  }

  public double radian() {
    return lvl2Radian(trainerLvl, lvl.floatValue());
  }

  public float lvl() {
    return lvl.floatValue();
  }

  public int getCp() {
    return cp;
  }

  public int getHp() {
    return hp;
  }

  public int getStardus() {
    return stardus;
  }

  public String getCandy() {
    return candyType;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return file().substring("screenshots/".length());
  }
}
