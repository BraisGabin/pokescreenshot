package com.braisgabin.pokescreenshot.processing;

public interface ScreenshotReader {
  int cp() throws CpException;

  String name() throws NameException, CpException;

  int hp() throws HpException, CpException;

  float weight() throws WeightException, CpException;

  float height() throws HeightException, CpException;

  String candy() throws CandyException, CpException;

  int stardust() throws StardustException, CpException;

  abstract class Exception extends ProcessingException {
    public Exception(String message) {
      super(message);
    }
  }

  class CpException extends Exception {
    public CpException(String message) {
      super(message);
    }
  }

  class HpException extends Exception {
    public HpException(String message) {
      super(message);
    }
  }

  class WeightException extends Exception {
    public WeightException(String message) {
      super(message);
    }
  }

  class HeightException extends Exception {
    public HeightException(String message) {
      super(message);
    }
  }

  class NameException extends Exception {
    public NameException(String message) {
      super(message);
    }
  }

  class CandyException extends Exception {
    public CandyException(String message) {
      super(message);
    }
  }

  class StardustException extends Exception {
    public StardustException(String message) {
      super(message);
    }
  }
}
