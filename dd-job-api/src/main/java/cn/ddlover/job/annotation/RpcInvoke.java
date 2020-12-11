package cn.ddlover.job.annotation;

import cn.ddlover.job.constant.RpcRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface RpcInvoke {
  RpcRequest method();
}
