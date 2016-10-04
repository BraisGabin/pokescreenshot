package com.braisgabin.pokescreenshot.processing;

import android.util.Log;

public class Ocr {
  private final static String TAG = "OCR";

  private final Tess tess;

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

  public int cp() throws CpException {
    return tess.cp();
  }

  public String name() throws NameException {
    return tess.name();
  }

  public int hp() throws HpException {
    return tess.hp();
  }

  public String candy() throws CandyException {
    return tess.candy();
  }

  public int stardust() throws StardustException {
    return tess.stardust();
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
