package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.auto.value.AutoValue;
import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ocr {
  private final static String TAG = "OCR";
  public static final int HEIGHT_CP = 95;
  public static final int VALUE_CP = 245;
  public static final int VALUE_NO_CP = 219;

  public static Ocr create(TessBaseAPI tess, Context context, Bitmap bitmap, Canvas canvas) {
    final float d = bitmap.getWidth() / (float) 480;
    BitmapOperations.filter(context, bitmap, Math.round(HEIGHT_CP * d), VALUE_CP, VALUE_NO_CP);
    tess.setImage(bitmap);
    return new Ocr(tess, bitmap.getWidth(), bitmap.getHeight(), d, canvas);
  }

  private final TessBaseAPI tess;
  private final int width;
  private final int height;
  private final float d;
  private final Canvas canvas;
  private final Paint paint;

  public Ocr(TessBaseAPI tess, int width, int height, float d, Canvas canvas) {
    this.tess = tess;
    this.width = width;
    this.height = height;
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

  public Pokemon ocr() {
    final Rect rect = new Rect();

    final int cp = cp(cpRect(width), rect);
    Log.d(TAG, "CP: " + cp);

    final String name = name(nameRect(width, rect.bottom));
    Log.d(TAG, "Name: " + name);

    final int hp = hp(hpRect(width, rect.bottom));
    Log.d(TAG, "HP: " + hp);

    final String candy = candy(candyRect(width, rect.bottom));
    Log.d(TAG, "Candy: " + candy);

    final int stardust = stardust(stardustRect(width, rect.bottom));
    Log.d(TAG, "Stardust: " + stardust);

    return Pokemon.create(cp, hp, stardust, candy, name);
  }

  private int cp(Rect ocrRect, Rect regionRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();
    String text2 = text.replace(" ", "");
    text2 = text2.replace('O', '0');
    text2 = text2.replace('o', '0');
    final String[] lines = text2.split("\n");

    int cp = -1;
    final Pattern pattern = Pattern.compile("PC([0-9]+)", Pattern.CASE_INSENSITIVE);

    for (int i = 0, length = lines.length; i < length; i++) {
      final Matcher matcher = pattern.matcher(lines[i]);
      if (matcher.matches()) {
        regionRect.set(getTextline(tess, i));
        regionRect.offset(ocrRect.left, ocrRect.top);
        cp = Integer.parseInt(matcher.group(1));
        break;
      }
    }

    if (cp <= 0) {
      Log.w(TAG, "Error parsing CP:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return cp;
  }

  private Rect cpRect(int width) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(HEIGHT_CP * d));
    rect.offset(width / 2 - rect.width() / 2, 0);
    return rect;
  }

  private String name(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    final Rect boxRect = getRegionBox(tess);
    boxRect.offset(ocrRect.left, ocrRect.top);
    final int rectWidth = (width / 2 - boxRect.left) * 2 + Math.round(2 * d);
    final Rect ocrRect2 = new Rect(boxRect);
    ocrRect2.right = boxRect.left + rectWidth;

    tess.setRectangle(ocrRect2);
    String name = tess.getUTF8Text();
    final Rect boxRect2 = getRegionBox(tess);
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
    Rect rect = new Rect(0, 0, Math.round(400 * d), Math.round(44 * d));
    rect.offset(width / 2 - rect.width() / 2, bottom + Math.round(272 * d));
    return rect;
  }

  private int hp(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    String text = tess.getUTF8Text();
    Rect regionRect = new Rect();

    int hp = -1;
    final Pattern pattern = Pattern.compile("P[S5] ?[0-9]+ ?/ ?([0-9]+)", Pattern.CASE_INSENSITIVE);

    text = text.replace("l", "1");
    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(getRegionBox(tess));
      regionRect.offset(ocrRect.left, ocrRect.top);
      hp = Integer.parseInt(matcher.group(1));
    }

    if (hp <= 0) {
      Log.w(TAG, "Error parsing HP:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return hp;
  }

  private Rect hpRect(int width, int bottom) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(30 * d));
    rect.offset(width / 2 - rect.width() / 2, bottom + Math.round(333 * d));
    return rect;
  }

  private String candy(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    String candy = Candy.candyType(text);

    final Rect boxRect = getRegionBox(tess);
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
    rect.offset(width / 2, bottom + Math.round(490 * d));
    return rect;
  }

  private int stardust(Rect ocrRect) {
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    int stardust = -1;
    Rect regionRect = new Rect();
    final Pattern pattern = Pattern.compile("^[HE@]([0-9]+) .*$", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(getRegionBox(tess));
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
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(34 * d));
    rect.offset(width / 2, bottom + Math.round(540 * d));
    return rect;
  }

  private Rect getRegionBox(TessBaseAPI tess) {
    final Pixa regions = tess.getRegions();
    final Rect boxRect = regions.getBoxRect(0);
    regions.recycle();
    return boxRect;
  }

  private Rect getTextline(TessBaseAPI tess, int index) {
    final Pixa regions = tess.getTextlines();
    final Rect boxRect = regions.getBoxRect(index);
    regions.recycle();
    return boxRect;
  }

  @AutoValue
  public abstract static class Pokemon {
    static Pokemon create(int cp, int hp, int stardust, String candy, String name) {
      return new AutoValue_Ocr_Pokemon(cp, hp, stardust, candy, name);
    }

    public abstract int getCp();

    public abstract int getHp();

    public abstract int getStardust();

    @Nullable
    public abstract String getCandy();

    @Nullable
    public abstract String getName();
  }
}
