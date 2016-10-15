package com.braisgabin.pokescreenshot.processing;

public abstract class ProcessingException extends Exception {
  public ProcessingException(String message) {
    super(message);
  }

  public ProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
