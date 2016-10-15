package com.braisgabin.pokescreenshot.processing;

public interface ScreenshotReader {
  int cp() throws CpException;

  String name() throws NameException, CpException;

  int hp() throws HpException, CpException;

  String candy() throws CandyException, CpException;

  int stardust() throws StardustException, CpException;

  abstract class Exception extends ProcessingException {
    public Exception(String message) {
      super(message);
    }

    public Exception(String message, Throwable cause) {
      super(message, cause);
    }
  }

  class CpException extends Exception {
    public CpException(String message) {
      super(message);
    }

    public CpException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  class HpException extends Exception {
    public HpException(String message) {
      super(message);
    }

    public HpException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  class NameException extends Exception {
    public NameException(String message) {
      super(message);
    }

    public NameException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  class CandyException extends Exception {
    public CandyException(String message) {
      super(message);
    }

    public CandyException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  class StardustException extends Exception {
    public StardustException(String message) {
      super(message);
    }

    public StardustException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
