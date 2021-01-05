package cn.ddlover.job.annotation;

import cn.ddlover.job.constant.RouteTypeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 15:21
 * 用来标注 任务的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface JobProvider {

  /**
   * 任务名称
   * 不填时默认以类名#方法名作为name
   */
  String name() default "";

  RouteTypeEnum routeType() default RouteTypeEnum.RANDOM;

  @AliasFor("value")
  String cron() default "";

  @AliasFor("cron")
  String value() default "";

  long timeout() default 60L;

  int retryTime() default 0;

  String owner();

  String warningEmail() default "";

  String param() default "";

  /**
   * 定时任务的描述
   */
  String desc() default "";
}
