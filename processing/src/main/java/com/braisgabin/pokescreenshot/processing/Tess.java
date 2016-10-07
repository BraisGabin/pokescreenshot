package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    final int valueNoCp = 219;
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

  int cp(Rect cpRegion) throws Ocr.CpException {
    int cp = -1;
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

      final Pattern pattern = Pattern.compile("([0-9]+)", Pattern.CASE_INSENSITIVE);

      final Matcher matcher = pattern.matcher(text);
      if (matcher.find()) {
        cp = Integer.parseInt(matcher.group(1));
      }
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");
    }

    if (cp <= 0) {
      throw new Ocr.CpException("Error parsing CP:\n" + text);
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

    return cp;
  }

  private Rect cpRect() {
    Rect rect = new Rect(0, 0, Math.round(160 * d), Math.round((HEIGHT_CP - 28) * d));
    rect.offset(width / 2 - rect.width() / 2, Math.round(28 * d));
    rect.right -= Math.round(30 * d);
    return rect;
  }

  String name(int cpHeight) throws Ocr.NameException {
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

    if (name == null) {
      throw new Ocr.NameException("Error parsing name: No name");
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

  int hp(int cpHeight) throws Ocr.HpException {
    int hp = -1;
    final String text;

    final Rect ocrRect = hpRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = (getRegionBox(tess));
      regionRect.offset(ocrRect.left, ocrRect.top);

      final Pattern pattern = Pattern.compile("[^/]+/([0-9]+)", Pattern.CASE_INSENSITIVE);

      String text2 = text.replace("l", "1");
      text2 = text2.replace("S", "5");
      text2 = text2.replace("O", "0");
      text2 = text2.replace(" ", "");
      Matcher matcher = pattern.matcher(text2);
      if (matcher.matches()) {
        hp = Integer.parseInt(matcher.group(1));
      }
    }

    if (hp <= 0) {
      throw new Ocr.HpException("Error parsing HP:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return hp;
  }

  private Rect hpRect(int cpHeight) {
    Rect rect = new Rect(0, 0, Math.round(180 * d), Math.round(28 * d));
    rect.offset(width / 2 - rect.width() / 2, cpHeight + Math.round(335 * d));
    return rect;
  }

  String candy(int cpHeight) throws Ocr.CandyException {
    final String candy;
    final String text;

    final Rect ocrRect = candyRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, " '.ABCDEFGHIJKLMNOPQRSTUVWXYZo");
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();

      String text2 = text.replace(" ", "");
      text2 = text2.replace("NIDORANQ", "NIDORAN♀");
      text2 = text2.replace("NIDORANo", "NIDORAN♂");
      text2 = text2.replace("NIDORANJ'", "NIDORAN♂");

      candy = Candy.candyType(text2);

      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);
      tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");
    }

    if (candy == null) {
      throw new Ocr.CandyException("Error parsing candy: " + text);
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return candy;
  }

  private Rect candyRect(int cpHeight) {
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(24 * d));
    rect.offset(width / 2, cpHeight + Math.round((496) * d));
    return rect;
  }

  int stardust(int cpHeight) throws Ocr.StardustException {
    int stardust = -1;
    final String text;

    final Rect ocrRect = stardustRect(cpHeight);
    final Rect regionRect;
    synchronized (tess) {
      tess.setRectangle(ocrRect);
      text = tess.getUTF8Text();
      regionRect = getRegionBox(tess);
      regionRect.offset(ocrRect.left, ocrRect.top);

      final Pattern pattern = Pattern.compile("[HEi@] ?([0-9]+)", Pattern.CASE_INSENSITIVE);

      Matcher matcher = pattern.matcher(text);
      if (matcher.matches()) {
        stardust = Integer.parseInt(matcher.group(1));
        if (!Stardust.isStardustCorrect(stardust)) {
          stardust = -1;
        }
      }
    }

    if (stardust < 0) {
      throw new Ocr.StardustException("Error parsing stardust:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
    }

    return stardust;
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
