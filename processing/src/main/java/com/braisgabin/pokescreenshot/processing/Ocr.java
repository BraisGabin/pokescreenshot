package com.braisgabin.pokescreenshot.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googlecode.tesseract.android.TessBaseAPI.PageIteratorLevel.RIL_WORD;

public class Ocr {
  private final static String TAG = "OCR";

  public static Ocr create(TessBaseAPI tess, Bitmap bitmap, float density, Canvas canvas) {
    BitmapOperations.filter(bitmap, 208, 229);
    tess.setImage(bitmap);
    return new Ocr(tess, bitmap.getWidth(), bitmap.getHeight(), density, canvas);
  }

  private final TessBaseAPI tess;
  private final int width;
  private final int height;
  private final float d;
  private final Canvas canvas;
  private final Paint paint;

  public Ocr(TessBaseAPI tess, int width, int height, float density, Canvas canvas) {
    this.tess = tess;
    this.width = width;
    this.height = height;
    this.d = density;
    this.canvas = canvas;
    if (canvas != null) {
      this.paint = new Paint();
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(2 * density);
    } else {
      paint = null;
    }
  }

  public Pokemon ocr() {
    final Rect rect = new Rect();

    final int pc = pc(pcRect(width), rect);
    Log.d(TAG, "PC: " + pc);

    final String name = name(nameRect(width, rect.bottom));
    Log.d(TAG, "Name: " + name);

    final int ps = ps(psRect(width, rect.bottom));
    Log.d(TAG, "PS: " + ps);

    final String candy = candy(candyRect(width, rect.bottom));
    Log.d(TAG, "Candy: " + candy);

    final int stardust = stardust(stardustRect(width, rect.bottom));
    Log.d(TAG, "Stardust: " + stardust);

    if (canvas != null) {
      paint.setColor(Color.CYAN);
      canvas.drawLine(width / 2, 0, width / 2, height, paint);
    }

    return new Pokemon(pc, ps, stardust, candy, name);
  }

  private int pc(Rect ocrRect, Rect regionRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    int pc = -1;
    final Pattern pattern = Pattern.compile("PC([0-9]+)", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(tess.getRegions().getBoxRect(0));
      regionRect.offset(ocrRect.left, ocrRect.top);
      pc = Integer.parseInt(matcher.group(1));
    } else {
      final ResultIterator iterator = tess.getResultIterator();
      while (iterator.next(RIL_WORD)) {
        final String word = iterator.getUTF8Text(RIL_WORD);
        matcher = pattern.matcher(word);
        if (matcher.matches()) {
          regionRect.set(iterator.getBoundingRect(RIL_WORD));
          pc = Integer.parseInt(matcher.group(1));
          break;
        }
      }
    }

    if (pc <= 0) {
      Log.w(TAG, "Error parsing PC:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return pc;
  }

  private Rect pcRect(int width) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(95 * d));
    rect.offset(width / 2 - rect.width() / 2, 0);
    return rect;
  }

  private String name(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    final Rect boxRect = tess.getRegions().getBoxRect(0);
    boxRect.offset(ocrRect.left, ocrRect.top);
    final int rectWidth = (width / 2 - boxRect.left) * 2 + Math.round(2 * d);
    final Rect ocrRect2 = new Rect(boxRect);
    ocrRect2.right = boxRect.left + rectWidth;

    tess.setRectangle(ocrRect2);
    String name = tess.getUTF8Text();
    final Rect boxRect2 = tess.getRegions().getBoxRect(0);
    boxRect2.offset(ocrRect2.left, ocrRect2.top);

    if (name == null) {
      Log.w(TAG, "Error parsing name: No name");
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.BLUE);
      canvas.drawRect(boxRect, paint);
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect2, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(boxRect2, paint);
    }

    return name;
  }

  private Rect nameRect(int width, int bottom) {
    Rect rect = new Rect(0, 0, Math.round(400 * d), Math.round(40 * d));
    rect.offset(width / 2 - rect.width() / 2, bottom + Math.round(275 * d));
    return rect;
  }

  private int ps(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();
    Rect regionRect = new Rect();

    int ps = -1;
    final Pattern pattern = Pattern.compile("P[S5] [0-9]+/([0-9]+)", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(tess.getRegions().getBoxRect(0));
      regionRect.offset(ocrRect.left, ocrRect.top);
      ps = Integer.parseInt(matcher.group(1));
    }

    if (ps <= 0) {
      Log.w(TAG, "Error parsing PS:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return ps;
  }

  private Rect psRect(int width, int bottom) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(28 * d));
    rect.offset(width / 2 - rect.width() / 2, bottom + Math.round(335 * d));
    return rect;
  }

  private String candy(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    String candy = Candy.candyType(text);

    final Rect boxRect = tess.getRegions().getBoxRect(0);
    boxRect.offset(ocrRect.left, ocrRect.top);

    if (candy == null) {
      Log.w(TAG, "Error parsing candy: " + text);
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(boxRect, paint);
    }

    return candy;
  }

  private Rect candyRect(int width, int bottom) {
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(24 * d));
    rect.offset(width / 2, bottom + Math.round(496 * d));
    return rect;
  }

  private int stardust(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    int stardust = -1;
    Rect regionRect = new Rect();
    final Pattern pattern = Pattern.compile("^i([0-9]+) .*$", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(tess.getRegions().getBoxRect(0));
      regionRect.offset(ocrRect.left, ocrRect.top);
      stardust = Integer.parseInt(matcher.group(1));
    }

    if (stardust < 0) {
      Log.w(TAG, "Error parsing stardust:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return stardust;
  }

  private Rect stardustRect(int width, int bottom) {
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(28 * d));
    rect.offset(width / 2, bottom + Math.round(547 * d));
    return rect;
  }
}
