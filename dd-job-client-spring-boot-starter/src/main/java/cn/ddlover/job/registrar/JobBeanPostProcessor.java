package cn.ddlover.job.registrar;

import cn.ddlover.job.annotation.JobProvider;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.util.JobRegistry;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 16:05
 */
@Component
public class JobBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
    Map<Method, JobProvider> methodAnnotationMap = MethodIntrospector
        .selectMethods(clazz, (MetadataLookup<JobProvider>) method -> AnnotatedElementUtils.getMergedAnnotation(method, JobProvider.class));
    if (!methodAnnotationMap.isEmpty()) {
      methodAnnotationMap.forEach((method, jobProvider) -> {
        Job job = parseJob(method, jobProvider);
        JobRegistry.registryJob(job);
      });
    }
    return bean;
  }

  private Job parseJob(Method method, JobProvider jobProvider) {
    Job job = new Job();
    String name;
    if (StringUtils.hasText(jobProvider.name())) {
      name = jobProvider.name();
    } else {
      name = method.getDeclaringClass().getName() + "#" + method.getName();
    }
    job.setJobName(name);
    job.setRouteType(jobProvider.routeType().getType());
    job.setCron(jobProvider.cron());
    job.setTimeout(jobProvider.timeout());
    job.setRetryTime(jobProvider.retryTime());
    job.setOwner(jobProvider.owner());
    job.setWarningEmail(jobProvider.warningEmail());
    job.setJobParam(jobProvider.param());
    job.setDesc(jobProvider.desc());
    return job;
  }
}
