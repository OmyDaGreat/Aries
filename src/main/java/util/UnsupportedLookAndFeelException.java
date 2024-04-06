package util;

import lombok.experimental.StandardException;

@StandardException
public class UnsupportedLookAndFeelException extends RuntimeException {
    public UnsupportedLookAndFeelException() {
        this("Unsupported Look and Feel", null);
    }
    public UnsupportedLookAndFeelException(Throwable cause) {
        this(cause != null ? cause.getMessage() : "Unsupported Look and Feel", cause);
    }
}