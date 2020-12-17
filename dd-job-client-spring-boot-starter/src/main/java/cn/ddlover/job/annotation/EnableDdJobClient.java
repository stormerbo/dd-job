package cn.ddlover.job.annotation;

import cn.ddlover.job.registrar.RpcInvokerBeanDefinitionRegistrar;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 14:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(RpcInvokerBeanDefinitionRegistrar.class)
public @interface EnableDdJobClient {

  String[] value() default {};

  String[] basePackages() default {};

  Class<?>[] basePackageClasses() default {};
}
