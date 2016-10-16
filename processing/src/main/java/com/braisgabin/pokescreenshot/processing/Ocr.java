package com.braisgabin.pokescreenshot.processing;

import android.graphics.Rect;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ocr implements ScreenshotReader {
  private final static String TAG = "OCR";

  private final Tess tess;
  private int cp = -1;
  private int cpHeight = -1;
  private String name = null;
  private int hp = -1;
  private String candy = null;
  private int stardust = -1;

  public Ocr(Tess tess) {
    this.tess = tess;
  }

  public void debug() throws Ocr.Exception {
    Log.d(TAG, "CP: " + cp());
    Log.d(TAG, "Name: " + name());
    Log.d(TAG, "HP: " + hp());
    Log.d(TAG, "Candy: " + candy());
    Log.d(TAG, "Stardust: " + stardust());
  }

  @Override
  public synchronized int cp() throws CpException {
    if (cp < 0) {
      final Rect rect = new Rect();
      final String text;
      try {
        text = tess.cp(rect);
      } catch (Tess.TessException e) {
        throw new CpException("Error from Tesseract", e);
      }

      final Pattern pattern = Pattern.compile("([0-9]+)");

      final Matcher matcher = pattern.matcher(text);
      if (matcher.find()) {
        cp = Integer.parseInt(matcher.group(1));
      }

      if (cp <= 0) {
        throw new CpException("Error parsing CP:\n" + text);
      }
      cpHeight = rect.bottom;
    }
    return this.cp;
  }

  @Override
  public synchronized String name() throws NameException, CpException {
    if (name == null) {
      if (cpHeight < 0) {
        cp();
      }
      final String text;
      try {
        text = tess.name(cpHeight);
      } catch (Tess.TessException e) {
        throw new NameException("Error from Tesseract", e);
      }
      if (text.equals("")) {
        throw new NameException("Error parsing name: No name founded");
      }
      name = text;
    }
    return name;
  }

  @Override
  public synchronized int hp() throws HpException, CpException {
    if (hp < 0) {
      if (cpHeight < 0) {
        cp();
      }
      final String text;
      try {
        text = tess.hp(cpHeight);
      } catch (Tess.TessException e) {
        throw new HpException("Error from Tesseract", e);
      }

      final Pattern pattern = Pattern.compile("[^/]+/([0-9]+)");

      final String text2 = text
          .replace("l", "1")
          .replace("S", "5")
          .replace("O", "0")
          .replace(" ", "");
      final Matcher matcher = pattern.matcher(text2);
      if (matcher.matches()) {
        hp = Integer.parseInt(matcher.group(1));
      }

      if (hp <= 0) {
        throw new HpException("Error parsing HP:\n" + text);
      }
    }
    return hp;
  }

  @Override
  public synchronized String candy() throws CandyException, CpException {
    if (candy == null) {
      if (cpHeight < 0) {
        cp();
      }
      final String text;
      try {
        text = tess.candy(cpHeight);
      } catch (Tess.TessException e) {
        throw new CandyException("Error from Tesseract", e);
      }

      final String text2 = text
          .replace(" ", "")
          .replace("NIDORANU", "NIDORAN♂")
          .replace("NIDORANJ", "NIDORAN♂")
          .replace("NIDORANQ", "NIDORAN♀");

      candy = Candy.candyType(text2);

      if (candy == null) {
        throw new CandyException("Error parsing candy:\n" + text);
      }
    }
    return candy;
  }

  @Override
  public synchronized int stardust() throws StardustException, CpException {
    if (stardust < 0) {
      if (cpHeight < 0) {
        cp();
      }
      final String text;
      try {
        text = tess.stardust(cpHeight);
      } catch (Tess.TessException e) {
        throw new StardustException("Error from Tesseract", e);
      }

      final Pattern pattern = Pattern.compile("([0-9]+)");

      Matcher matcher = pattern.matcher(text);
      if (matcher.matches()) {
        stardust = Integer.parseInt(matcher.group(1));
        if (!Stardust.isStardustCorrect(stardust)) {
          stardust = -1;
        }
      }

      if (stardust < 0) {
        throw new Ocr.StardustException("Error parsing stardust:\n" + text);
      }
    }
    return stardust;
  }
}
