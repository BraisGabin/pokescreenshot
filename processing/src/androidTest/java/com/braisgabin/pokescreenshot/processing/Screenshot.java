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
  private BigDecimal degree;

  public String file() {
    return file;
  }

  public Point initialPoint() {
    return new Point(initPointX, initPointY);
  }

  public double radian() {
    return Math.toRadians(degree.doubleValue());
  }
}
