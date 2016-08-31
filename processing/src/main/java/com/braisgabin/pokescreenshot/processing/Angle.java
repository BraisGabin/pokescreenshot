package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

public class Angle {
  private final Bitmap bitmap;
  private final float d;

  public Angle(Bitmap bitmap, float density) {
    this.bitmap = bitmap;
    this.d = density;
  }

  public double radian() {
    return radian(null);
  }

  public double radian(Canvas canvas) {
    final Point initialPoint = initialPoint();
    final Point center = center(initialPoint, bitmap.getWidth());
    final int radius = radius(initialPoint, center);
    final double radian = radian(center, radius);
    if (canvas != null) {
      debug(initialPoint, center, radius, radian, canvas);
    }
    return radian;
  }

  private void debug(Point initialPoint, Point center, int radius, double radian, Canvas canvas) {
    final Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStrokeWidth(3 * d);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(0xb0ff0000);

    // Initial Point
    canvas.drawLine(initialPoint.x - 4 * d, initialPoint.y, initialPoint.x + 4 * d, initialPoint.y, paint);

    // Center
    canvas.drawLine(center.x - 4 * d, center.y, center.x + 4 * d, center.y, paint);
    canvas.drawLine(center.x, center.y - 4 * d, center.x, center.y + 4 * d, paint);

    // Radius
    canvas.drawArc(new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius), 180, 180, false, paint);

    // Radian
    final int x = (int) Math.round(Math.cos(radian) * radius);
    final int y = (int) Math.round(Math.sin(radian) * radius);
    canvas.drawCircle(center.x + x, center.y - y, 5 * d, paint);
  }

  Point initialPoint() {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    for (int y = 4 * height / 10; y >= 0; y--) {
      for (int x = width / 10, count = 2 * width / 10; x < count; x++) {
        if (bitmap.getPixel(x, y) == -1) {
          return new Point(x + Math.round(d), y + Math.round(2 * d));
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

  double radian(Point center, int radius) {
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
