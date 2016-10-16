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
  private static final int HEIGHT_ARC = HEIGHT_CP + 220;

  public static Tess create(TessBaseAPI tess, Context context, Bitmap bitmap, Canvas canvas) {
    final float d = proportion(bitmap);
    preprocessingImage(context, bitmap, d);
    tess.setImage(bitmap);
    return new Tess(tess, bitmap.getWidth(), d, canvas);
  }

  public static void preprocessingImage(Context context, Bitmap bitmap, float d) {
    final int valueNoCp = 205;
    BitmapOperations.filter(context, bitmap, Math.round(HEIGHT_CP * d), Math.round(HEIGHT_ARC * d), valueNoCp);
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
      paint.setColor(Color.MAGENTA);
      canvas.drawLine(0, Math.round(HEIGHT_CP * d), canvas.getWidth(), Math.round(HEIGHT_CP * d), paint);
      canvas.drawLine(0, Math.round(HEIGHT_ARC * d), canvas.getWidth(), Math.round(HEIGHT_ARC * d), paint);
    } else {
      paint = null;
    }
  }

  String cp(Rect cpRegion) throws TessException {
    final String text;

    final Rect ocrRect = cpRect();
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();

      regionRect = getRegionBox(tess);
      if (regionRect == null) {
        throw new TessException("Text not found");
      }
      regionRect.offset(ocrRect.left, ocrRect.top);
      cpRegion.set(regionRect);
      final int rectWidth = -(width / 2 - regionRect.right) * 2 + Math.round(2 * d);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.left = regionRect.right - rectWidth;
      if (ocrRect2.left >= ocrRect2.right) {
        throw new TessException("No correct CP found");
      }

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

  private Rect cpRect() {
    Rect rect = new Rect(0, 0, Math.round(160 * d), Math.round((HEIGHT_CP - 28) * d));
    rect.offset(width / 2 - rect.width() / 2, Math.round(28 * d));
    rect.right -= Math.round(30 * d);
    return rect;
  }

  String name(int cpHeight) throws TessException {
    final String name;

    final Rect ocrRect = nameRect(cpHeight);
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();

      regionRect = getRegionBox(tess);
      if (regionRect == null) {
        throw new TessException("Text not found");
      }
      regionRect.offset(ocrRect.left, ocrRect.top);
      final int rectWidth = (width / 2 - regionRect.left) * 2 + Math.round(2 * d);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.right = regionRect.left + rectWidth;
      if (ocrRect2.left >= ocrRect2.right) {
        throw new TessException("No correct HP found");
      }

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

  String hp(int cpHeight) throws TessException {
    final String text;

    final Rect ocrRect = hpRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "é");
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = (getRegionBox(tess));
      if (regionRect == null) {
        throw new TessException("Text not found");
      }
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

  String candy(int cpHeight) throws TessException {
    final String text;

    final Rect ocrRect = candyRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, " -.ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = getRegionBox(tess);
      if (regionRect == null) {
        throw new TessException("Text not found");
      }
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

  String stardust(int cpHeight) throws TessException {
    final String text;

    final Rect ocrRect = stardustRect(cpHeight);
    final Rect regionRect;
    final Rect ocrRect2;
    final Rect regionRect2;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      tess.getUTF8Text();
      regionRect = getRegionBox(tess);
      if (regionRect == null) {
        throw new TessException("Text not found");
      }
      regionRect.offset(ocrRect.left, ocrRect.top);
      ocrRect2 = new Rect(regionRect);
      ocrRect2.left = regionRect.left + Math.round(7 * d);
      if (ocrRect2.left >= ocrRect2.right) {
        throw new TessException("No correct stardust found");
      }

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

  public class TessException extends Exception {
    public TessException(String message) {
      super(message);
    }
  }
}
