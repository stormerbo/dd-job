package cn.ddlover.job.registrar;

import cn.ddlover.job.annotation.JobProvider;
import cn.ddlover.job.domain.JobHolder;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.exception.JobProviderException;
import cn.ddlover.job.util.JobRegistry;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
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
      for (Entry<Method, JobProvider> entry : methodAnnotationMap.entrySet()) {
        Method method = entry.getKey();
        JobProvider jobProvider = entry.getValue();
        checkMethod(method);
        JobHolder jobHolder = parseJob(method, jobProvider);
        JobRegistry.registryJob(jobHolder);
      }
    }
    return bean;
  }

  private void checkMethod(Method method) {
    Class<?>[] parameterTypes = method.getParameterTypes();
    if (parameterTypes.length != 1) {
      throw new JobProviderException("Job method must only 1 param");
    }
    if (parameterTypes[0] != String.class) {
      throw new JobProviderException("Job method param type must be "+String.class.getName());
    }
    if(method.getReturnType() != Response.class) {
      throw new JobProviderException("Job method return type must be "+Response.class.getName());
    }
  }

  private JobHolder parseJob(Method method, JobProvider jobProvider) {
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

    JobHolder jobHolder = new JobHolder();
    jobHolder.setJob(job);
    jobHolder.setJobClass(method.getDeclaringClass());
    jobHolder.setMethodName(method.getName());
    return jobHolder;
  }
}
