package cn.ddlover.job.exception;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 11:45
 */
public class ConnectFailException extends RuntimeException{

  public ConnectFailException() {
  }

  public ConnectFailException(String message) {
    super(message);
  }

  public ConnectFailException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConnectFailException(Throwable cause) {
    super(cause);
  }

  public ConnectFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
