package org.gbif.service.exception;


/**
 * Generic persistence exception.
 */
public class PersistenceException extends RuntimeException {

  private static final long serialVersionUID = 2276263875942459859L;

  public PersistenceException() {
    // empty block
  }

  public PersistenceException(String message) {
    super(message);
  }

  public PersistenceException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public PersistenceException(Throwable throwable) {
    super(throwable);
  }

}
