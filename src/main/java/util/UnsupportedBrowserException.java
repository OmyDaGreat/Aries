package util;

import lombok.experimental.StandardException;

@StandardException
public class UnsupportedBrowserException extends RuntimeException {
  public UnsupportedBrowserException() {
    this("The specified browser types is unsupported.", null);
  }

  public UnsupportedBrowserException(Throwable cause) {
    this(cause != null ? cause.getMessage() : "The specified browser type is unsupported.", cause);
  }
}
