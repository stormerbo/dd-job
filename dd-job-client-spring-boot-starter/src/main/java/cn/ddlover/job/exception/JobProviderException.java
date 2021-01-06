package cn.ddlover.job.exception;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 17:58
 */
public class JobProviderException extends RuntimeException{

  public JobProviderException() {
  }

  public JobProviderException(String message) {
    super(message);
  }

  public JobProviderException(String message, Throwable cause) {
    super(message, cause);
  }

  public JobProviderException(Throwable cause) {
    super(cause);
  }

  public JobProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
