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
  private BigDecimal density;

  @CsvField(pos = 3)
  private int initPointX;

  @CsvField(pos = 4)
  private int initPointY;

  @CsvField(pos = 5)
  private BigDecimal degree;

  @CsvField(pos = 6)
  private int pc;

  @CsvField(pos = 7)
  private int ps;

  @CsvField(pos = 8)
  private int stardus;

  @CsvField(pos = 9)
  private String candyType;

  @CsvField(pos = 10)
  private String name;

  public String file() {
    return file;
  }

  public float density() {
    return density.floatValue();
  }

  public Point initialPoint() {
    return new Point(initPointX, initPointY);
  }

  public double radian() {
    return Math.toRadians(degree.doubleValue());
  }

  public int getPc() {
    return pc;
  }

  public int getPs() {
    return ps;
  }

  public int getStardus() {
    return stardus;
  }

  public String getCandyName() {
    return candyType;
  }

  public String getName() {
    return name;
  }
}
