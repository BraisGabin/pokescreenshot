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
    final Rect ocrRect = cpRect();
    tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "é-");
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();
    String text2 = text.replace(" ", "");
    text2 = text2.replace('O', '0');
    text2 = text2.replace('o', '0');
    text2 = text2.replace('l', '1');
    text2 = text2.replace('Z', '2');
    text2 = text2.replace('S', '5');
    final String[] lines = text2.split("\n");
    Rect regionRect = new Rect();

    int cp = -1;
    final Pattern pattern = Pattern.compile("[^0-9]+([0-9]+)", Pattern.CASE_INSENSITIVE);

    for (int i = lines.length - 1; i >= 0; i--) {
      final Matcher matcher = pattern.matcher(lines[i]);
      if (matcher.matches()) {
        regionRect.set(getTextline(tess, i));
        regionRect.offset(ocrRect.left, ocrRect.top);
        cpRegion.set(regionRect);
        cp = Integer.parseInt(matcher.group(1));
        break;
      }
    }
    tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "");

    if (cp <= 0) {
      throw new Ocr.CpException("Error parsing CP:\n" + text);
    }
    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(regionRect, paint);
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
    final Rect ocrRect = nameRect(cpHeight);
    tess.setRectangle(ocrRect);
    tess.getUTF8Text();

    final Rect boxRect = getRegionBox(tess);
    boxRect.offset(ocrRect.left, ocrRect.top);
    final int rectWidth = (width / 2 - boxRect.left) * 2 + Math.round(2 * d);
    final Rect ocrRect2 = new Rect(boxRect);
    ocrRect2.right = boxRect.left + rectWidth;

    tess.setRectangle(ocrRect2);
    final String name = tess.getUTF8Text();
    final Rect boxRect2 = getRegionBox(tess);
    boxRect2.offset(ocrRect2.left, ocrRect2.top);

    if (name == null) {
      throw new Ocr.NameException("Error parsing name: No name");
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

  private Rect nameRect(int cpHeight) {
    Rect rect = new Rect(0, 0, Math.round(400 * d), Math.round(44 * d));
    rect.offset(width / 2 - rect.width() / 2, cpHeight + Math.round(275 * d));
    return rect;
  }

  int hp(int cpHeight) throws Ocr.HpException {
    final Rect ocrRect = hpRect(cpHeight);
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();
    Rect regionRect = new Rect();

    int hp = -1;
    final Pattern pattern = Pattern.compile("[^/]+/([0-9]+)", Pattern.CASE_INSENSITIVE);

    String text2 = text.replace("l", "1");
    text2 = text2.replace("S", "5");
    text2 = text2.replace("O", "0");
    text2 = text2.replace(" ", "");
    Matcher matcher = pattern.matcher(text2);
    if (matcher.matches()) {
      regionRect.set(getRegionBox(tess));
      regionRect.offset(ocrRect.left, ocrRect.top);
      hp = Integer.parseInt(matcher.group(1));
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
    final Rect ocrRect = candyRect(cpHeight);
    tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, " '.ABCDEFGHIJKLMNOPQRSTUVWXYZo");
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    String text2 = text.replace(" ", "");
    text2 = text2.replace("NIDORANQ", "NIDORAN♀");
    text2 = text2.replace("NIDORANo", "NIDORAN♂");
    text2 = text2.replace("NIDORANJ'", "NIDORAN♂");

    String candy = Candy.candyType(text2);

    final Rect boxRect = getRegionBox(tess);
    boxRect.offset(ocrRect.left, ocrRect.top);
    tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "");

    if (candy == null) {
      throw new Ocr.CandyException("Error parsing candy: " + text);
    }

    if (canvas != null) {
      paint.setColor(Color.RED);
      canvas.drawRect(ocrRect, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawRect(boxRect, paint);
    }

    return candy;
  }

  private Rect candyRect(int cpHeight) {
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(24 * d));
    rect.offset(width / 2, cpHeight + Math.round((496) * d));
    return rect;
  }

  int stardust(int cpHeight) throws Ocr.StardustException {
    final Rect ocrRect = stardustRect(cpHeight);
    tess.setRectangle(ocrRect);
    final String text = tess.getUTF8Text();

    int stardust = -1;
    Rect regionRect = new Rect();
    final Pattern pattern = Pattern.compile("^[HEi@]([0-9]+) .*$", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(text);
    if (matcher.matches()) {
      regionRect.set(getRegionBox(tess));
      regionRect.offset(ocrRect.left, ocrRect.top);
      stardust = Integer.parseInt(matcher.group(1));
      if (!Stardust.isStardustCorrect(stardust)) {
        stardust = -1;
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
    Rect rect = new Rect(0, 0, width / 2 - Math.round(12 * d), Math.round(28 * d));
    rect.offset(width / 2, cpHeight + Math.round(547 * d));
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
}
