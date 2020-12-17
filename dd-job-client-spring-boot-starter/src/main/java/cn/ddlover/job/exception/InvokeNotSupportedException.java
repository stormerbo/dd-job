package cn.ddlover.job.exception;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/17 19:03
 */
public class InvokeNotSupportedException extends RuntimeException{

  public InvokeNotSupportedException() {
  }

  public InvokeNotSupportedException(String message) {
    super(message);
  }

  public InvokeNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvokeNotSupportedException(Throwable cause) {
    super(cause);
  }

  public InvokeNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
