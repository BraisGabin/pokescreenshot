package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class Angle {

  public static double radian(Bitmap bitmap) {
    return radian(bitmap, false);
  }

  public static double radian(Bitmap bitmap, boolean debug) {
    final Point initialPoint = initialPoint(bitmap);
    final Point center = center(initialPoint, bitmap.getWidth());
    final int radius = radius(initialPoint, center);
    final double radian = radian(center, radius, bitmap);
    if (debug) {
      debug(initialPoint, center, radius, radian, new Canvas(bitmap));
    }
    return radian;
  }

  private static void debug(Point initialPoint, Point center, int radius, double radian, Canvas canvas) {
    final Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(9);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(0xb0ff0000);

    // Initial Point
    canvas.drawLine(initialPoint.x - 10, initialPoint.y, initialPoint.x + 10, initialPoint.y, paint);

    // Center
    canvas.drawLine(center.x - 10, center.y, center.x + 10, center.y, paint);
    canvas.drawLine(center.x, center.y - 10, center.x, center.y + 10, paint);

    // Radius
    canvas.drawCircle(center.x, center.y, radius, paint);

    // Radian
    final int x = (int) Math.round(Math.cos(radian) * radius);
    final int y = (int) Math.round(Math.sin(radian) * radius);
    canvas.drawCircle(center.x + x, center.y - y, 15, paint);
  }

  static Point initialPoint(Bitmap bitmap) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    for (int y = 4 * height / 10; y >= 0; y--) {
      for (int x = width / 10, count = 2 * width / 10; x < count; x++) {
        if (bitmap.getPixel(x, y) == -1) {
          return new Point(x + 4, y + 7);
        }
      }
    }
    throw new RuntimeException();
  }

  static Point center(Point initialPoint, int width) {
    return new Point(width / 2, initialPoint.y);
  }

  static int radius(Point initialPoint, Point center) {
    return center.x - initialPoint.x;
  }

  static double radian(Point center, int radius, Bitmap bitmap) {
    for (int i = 0; i <= 360; i++) {
      final double radians = i * Math.PI / 360;
      final int x = (int) Math.round(Math.cos(radians) * radius);
      final int y = (int) Math.round(Math.sin(radians) * radius);
      if (bitmap.getPixel(center.x + x, center.y - y) == -1) {
        System.out.println(i / 2 + "º");
        return radians + Math.PI / 120.;
      }
    }
    throw new RuntimeException();
  }
}
