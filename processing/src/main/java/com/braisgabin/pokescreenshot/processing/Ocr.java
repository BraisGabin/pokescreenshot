package com.braisgabin.pokescreenshot.processing;

import android.graphics.Rect;
import android.util.Log;

public class Ocr {
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

  public synchronized int cp() throws CpException {
    if (cp < 0) {
      final Rect rect = new Rect();
      cp = tess.cp(rect);
      cpHeight = rect.bottom;
    }
    return this.cp;
  }

  public synchronized String name() throws NameException, CpException {
    if (name == null) {
      if (cpHeight < 0) {
        cp();
      }
      name = tess.name(cpHeight);
    }
    return name;
  }

  public synchronized int hp() throws HpException, CpException {
    if (hp < 0) {
      if (cpHeight < 0) {
        cp();
      }
      hp = tess.hp(cpHeight);
    }
    return hp;
  }

  public synchronized String candy() throws CandyException, CpException {
    if (candy == null) {
      if (cpHeight < 0) {
        cp();
      }
      candy = tess.candy(cpHeight);
    }
    return candy;
  }

  public synchronized int stardust() throws StardustException, CpException {
    if (stardust < 0) {
      if (cpHeight < 0) {
        cp();
      }
      stardust = tess.stardust(cpHeight);
    }
    return stardust;
  }

  public static abstract class Exception extends ProcessingException {
    public Exception(String message) {
      super(message);
    }
  }

  public static class CpException extends Exception {
    public CpException(String message) {
      super(message);
    }
  }

  public static class HpException extends Exception {
    public HpException(String message) {
      super(message);
    }
  }

  public static class NameException extends Exception {
    public NameException(String message) {
      super(message);
    }
  }

  public static class CandyException extends Exception {
    public CandyException(String message) {
      super(message);
    }
  }

  public static class StardustException extends Exception {
    public StardustException(String message) {
      super(message);
    }
  }
}
