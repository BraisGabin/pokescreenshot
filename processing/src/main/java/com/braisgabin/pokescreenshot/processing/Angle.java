package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import static com.braisgabin.pokescreenshot.processing.Utils.navBarHeight;
import static com.braisgabin.pokescreenshot.processing.Utils.proportion;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Angle {
  private static final int X_PERCENTAGE_MIN = 8;
  private static final int X_PERCENTAGE_MAX = 14;
  private static final int Y_PERCENTAGE_MIN = 33;
  private static final int Y_PERCENTAGE_MAX = 37;
  private final Bitmap bitmap;
  private final int width;
  private final int height;
  private final int navBarHeight;
  private final float d;

  private Point initialPoint;
  private Point center;
  private int radius = -1;

  public Angle(Bitmap bitmap) {
    this.bitmap = bitmap;
    this.width = bitmap.getWidth();
    this.height = bitmap.getHeight();
    this.navBarHeight = navBarHeight(bitmap);
    this.d = proportion(bitmap, navBarHeight);
  }

  public double isBall(double radian) throws InitialPointException {
    calculateCenterAndRadiusIfNecessary();
    return isBall(radian, center, radius);
  }

  double isBall(double radian, Point center, int radius) {
    final int x = (int) round(cos(radian) * radius);
    final int y = (int) round(sin(radian) * radius);
    return isCircle(center.x + x, center.y - y, round(3.1f * d));
  }

  private void calculateCenterAndRadiusIfNecessary() throws InitialPointException {
    if (center == null || radius < 0) {
      synchronized (this) {
        if (center == null || radius < 0) {
          calculateCenterAndRadius();
        }
      }
    }
  }

  private void calculateCenterAndRadius() throws InitialPointException {
    this.initialPoint = initialPoint();
    this.center = center(initialPoint, width);
    this.radius = radius(initialPoint, center);
  }

  public void debug(Canvas canvas, double radian) {
    final Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(3 * d);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(0xb0ff0000);

    // Initial Point
    canvas.drawLine(initialPoint.x - 4 * d, initialPoint.y, initialPoint.x + 4 * d, initialPoint.y, paint);

    paint.setColor(0xb000ff00);
    canvas.drawRect(initialPointArea(width, height, navBarHeight), paint);
    paint.setColor(0xb0ff0000);

    // Center
    canvas.drawLine(center.x - 4 * d, center.y, center.x + 4 * d, center.y, paint);
    canvas.drawLine(center.x, center.y - 4 * d, center.x, center.y + 4 * d, paint);

    // Arc
    canvas.drawArc(new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius), 180, 180, false, paint);

    // Radian
    if (radian >= 0) {
      final int x = (int) round(cos(radian) * radius);
      final int y = (int) round(sin(radian) * radius);
      canvas.drawCircle(center.x + x, center.y - y, 6 * d, paint);
    }
  }

  static Rect initialPointArea(int width, int height, int navBarHeight) {
    return new Rect(
        (X_PERCENTAGE_MIN * width) / 100, (Y_PERCENTAGE_MIN * (height - navBarHeight)) / 100,
        (X_PERCENTAGE_MAX * width) / 100, (Y_PERCENTAGE_MAX * (height - navBarHeight)) / 100
    );
  }

  Point initialPoint() throws InitialPointException {
    final Rect area = initialPointArea(width, height, navBarHeight);
    for (int y = area.bottom, countY = area.top; y >= countY; y--) {
      int a = -1;
      int b = -1;
      for (int x = area.left, countX = area.right; x < countX; x++) {
        if (bitmap.getPixel(x, y) == Color.WHITE) {
          if (a < 0) {
            a = x;
          }
          b = x;
        }
      }
      if (a >= 0) {
        return new Point(a + round(0.3f * d + (b - a) / 2f), y + round(2.6f * d));
      }
    }
    throw new InitialPointException("Impossible to detect the initial point of the circumference.");
  }

  static Point center(Point initialPoint, int width) {
    return new Point(width / 2, initialPoint.y);
  }

  static int radius(Point initialPoint, Point center) {
    return center.x - initialPoint.x;
  }

  private double isCircle(int x, int y, int range) {
    final double sin45 = sqrt(2) / 2;
    int count = 0;
    for (int i = max(range - 2, 0); i <= range; i++) {
      final int j = (int) round(i * sin45);
      if (bitmap.getPixel(x + i, y) == Color.WHITE) count++;
      if (bitmap.getPixel(x - i, y) == Color.WHITE) count++;
      if (bitmap.getPixel(x, y + i) == Color.WHITE) count++;
      if (bitmap.getPixel(x, y - i) == Color.WHITE) count++;
      if (bitmap.getPixel(x + j, y + j) == Color.WHITE) count++;
      if (bitmap.getPixel(x + j, y - j) == Color.WHITE) count++;
      if (bitmap.getPixel(x - j, y + j) == Color.WHITE) count++;
      if (bitmap.getPixel(x - j, y - j) == Color.WHITE) count++;
    }
    return count / (double) (8 * 3);
  }

  public static abstract class Exception extends ProcessingException {
    public Exception(String message) {
      super(message);
    }
  }

  public static class InitialPointException extends Exception {
    public InitialPointException(String message) {
      super(message);
    }
  }
}
