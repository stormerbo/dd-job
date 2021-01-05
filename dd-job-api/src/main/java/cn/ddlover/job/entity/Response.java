package cn.ddlover.job.entity;

import cn.ddlover.job.constant.ResponseCode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 17:35
 */
@Getter
@Setter
public class Response<T> {

  /**
   * 返回的响应码
   */
  private Integer code;
  /**
   * 响应消息
   */
  private String message;

  private Integer totalRecord;

  /**
   * 数据内容
   */
  private T data;

  public Response(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public Response(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public Response(ResponseCode responseCode, T data) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.data = data;
  }

  public Response(ResponseCode responseCode, T data, Integer totalRecord) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.data = data;
    this.totalRecord = totalRecord;
  }

  public Response(ResponseCode responseCode) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
  }

  /**
   * 无返回数据
   */
  public static Response<Void> success() {
    return new Response<>(ResponseCode.SUCCESS, null);
  }

  /**
   * 返回数据
   */
  public static <T> Response<T> successWithData(T data) {
    return new Response<>(ResponseCode.SUCCESS, data);
  }

  public static <T extends List<?>> Response<T> successWithListData(T data, Integer totalRecord) {
    return new Response<>(ResponseCode.SUCCESS, data, totalRecord);
  }

  public static Response<Void> successWithMessage(String message) {
    return new Response<>(ResponseCode.SUCCESS.getCode(), message);
  }

  public static Response<Void> fail(String message) {
    return new Response<>(ResponseCode.FAIL.getCode(), message);
  }
}
