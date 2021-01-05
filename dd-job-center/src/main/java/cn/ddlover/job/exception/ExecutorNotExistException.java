package cn.ddlover.job.exception;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 17:05
 */
public class ExecutorNotExistException extends RuntimeException{

  public ExecutorNotExistException() {
  }

  public ExecutorNotExistException(String message) {
    super(message);
  }

  public ExecutorNotExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutorNotExistException(Throwable cause) {
    super(cause);
  }

  public ExecutorNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
