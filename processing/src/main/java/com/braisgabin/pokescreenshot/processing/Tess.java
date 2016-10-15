package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.TessBaseAPI;

import static com.braisgabin.pokescreenshot.processing.Utils.proportion;

public class Tess {
  private static final int HEIGHT_CP = 85;

  public static Tess create(TessBaseAPI tess, Context context, Bitmap bitmap, Canvas canvas) {
    final float d = proportion(bitmap);
    preprocessingImage(context, bitmap, d);
    tess.setImage(bitmap);
    return new Tess(tess, bitmap.getWidth(), d, canvas);
  }

  public static void preprocessingImage(Context context, Bitmap bitmap, float d) {
    final int valueCp = 245;
    final int valueNoCp = 205;
    BitmapOperations.filter(context, bitmap, Math.round(HEIGHT_CP * d), valueCp, valueNoCp);
  }

  private final TessBaseAPI tess;
  private final int width;
  private final float d;
  private final Canvas canvas;
  private final Paint paint;

  private Tess(TessBaseAPI tess, int width, float d, Canvas canvas) {
    this.tess = tess;
    this.width = width;
    this.d = d;
    this.canvas = canvas;
    if (canvas != null) {
      this.paint = new Paint();
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(2 * d);
    } else {
      paint = null;
    }
  }

  String cp(Rect cpRegion) {
    final String text;

    final Rect ocrRect = cpRect();
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();

      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);
      final int rectWidth = -(width / 2 - regionRect.right) * 2 + Math.round(2 * d);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.left = regionRect.right - rectWidth;

      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789");
      tess.setRectangle(ocrRect2);
      text = tess.getUTF8Text();
      regionRect2 = getRegionBox(tess);
      regionRect2.offset(ocrRect2.left, ocrRect2.top);
      cpRegion.set(regionRect2);
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.BLUE);
      canvas.drawRect(regionRect, paint);
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect2, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect2, paint);
    }

    return text;
  }

  private Rect cpRect() {
    Rect rect = new Rect(0, 0, Math.round(160 * d), Math.round((HEIGHT_CP - 28) * d));
    rect.offset(width / 2 - rect.width() / 2, Math.round(28 * d));
    rect.right -= Math.round(30 * d);
    return rect;
  }

  String name(int cpHeight) {
    final String name;

    final Rect ocrRect = nameRect(cpHeight);
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();

      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);
      final int rectWidth = (width / 2 - regionRect.left) * 2 + Math.round(2 * d);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.right = regionRect.left + rectWidth;

      tess.setRectangle(ocrRect2);
      name = tess.getUTF8Text();
      regionRect2 = getRegionBox(tess);
      regionRect2.offset(ocrRect2.left, ocrRect2.top);
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.BLUE);
      canvas.drawRect(regionRect, paint);
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect2, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect2, paint);
    }

    return name;
  }

  private Rect nameRect(int cpHeight) {
    Rect rect = new Rect(0, 0, Math.round(400 * d), Math.round(44 * d));
    rect.offset(width / 2 - rect.width() / 2, cpHeight + Math.round(275 * d));
    return rect;
  }

  String hp(int cpHeight) {
    final String text;

    final Rect ocrRect = hpRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "é");
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = (getRegionBox(tess));
      regionRect.offset(ocrRect.left, ocrRect.top);
      tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "");
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return text;
  }

  private Rect hpRect(int cpHeight) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(28 * d));
    rect.offset(width / 2 - rect.width() / 2, cpHeight + Math.round(335 * d));
    return rect;
  }

  String candy(int cpHeight) {
    final String text;

    final Rect ocrRect = candyRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, " .ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return text;
  }

  private Rect candyRect(int cpHeight) {
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(24 * d));
    rect.offset(width / 2, cpHeight + Math.round((496) * d));
    return rect;
  }

  String stardust(int cpHeight) {
    final String text;

    final Rect ocrRect = stardustRect(cpHeight);
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();
      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.left = regionRect.left + Math.round(7 * d);

      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789");
      tess.setRectangle(ocrRect2);
      text = tess.getUTF8Text();
      regionRect2 = getRegionBox(tess);
      regionRect2.offset(ocrRect2.left, ocrRect2.top);
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.BLUE);
      canvas.drawRect(regionRect, paint);
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect2, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect2, paint);
    }

    return text;
  }

  private Rect stardustRect(int cpHeight) {
    Rect rect = new Rect(0, 0, Math.round(80 * d), Math.round(28 * d));
    rect.offset(width / 2, cpHeight + Math.round(547 * d));
    return rect;
  }

  private Rect getRegionBox(TessBaseAPI tess) {
    final Pixa regions = tess.getRegions();
    final Rect regionRect = regions.getBoxRect(0);
    regions.recycle();
    return regionRect;
  }
}
