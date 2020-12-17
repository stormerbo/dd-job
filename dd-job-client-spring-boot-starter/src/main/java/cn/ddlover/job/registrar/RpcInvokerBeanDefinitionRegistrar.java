package cn.ddlover.job.registrar;

import cn.ddlover.job.annotation.EnableDdJobClient;
import cn.ddlover.job.annotation.RpcInvoke;
import cn.ddlover.job.rpc.RpcClientFactoryBean;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 17:46
 */
public class RpcInvokerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

  private ResourceLoader resourceLoader;
  private Environment environment;
  private BeanFactory beanFactory;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    ClassPathScanningCandidateComponentProvider scanner = getScanner();
    scanner.setResourceLoader(resourceLoader);
    scanner.addIncludeFilter(new AnnotationTypeFilter(RpcInvoke.class));

    Set<String> basePackages = getBasePackages(importingClassMetadata);
    for (String basePackage : basePackages) {
      Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
      for (BeanDefinition candidateComponent : candidateComponents) {
        if (candidateComponent instanceof AnnotatedBeanDefinition) {
          AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
          AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
          Assert.isTrue(annotationMetadata.isInterface(), "@RpcInvoke can only be specified on an interface");
          registerRpcClient(registry, annotationMetadata);
        }
      }
    }
  }

  private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata) {
    String className = annotationMetadata.getClassName();
    BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(RpcClientFactoryBean.class);
    definition.addPropertyValue("type", className);
    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
    beanDefinition.setPrimary(true);

    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{className});
    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
  }


  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.beanFactory = beanFactory;
  }

  protected ClassPathScanningCandidateComponentProvider getScanner() {
    return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
      @Override
      protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        if (beanDefinition.getMetadata().isIndependent()) {
          if (!beanDefinition.getMetadata().isAnnotation()) {
            isCandidate = true;
          }
        }
        return isCandidate;
      }
    };
  }

  protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
    Map<String, Object> attributes = importingClassMetadata
        .getAnnotationAttributes(EnableDdJobClient.class.getCanonicalName());

    Set<String> basePackages = new HashSet<>();
    for (String pkg : (String[]) attributes.get("value")) {
      if (StringUtils.hasText(pkg)) {
        basePackages.add(pkg);
      }
    }
    for (String pkg : (String[]) attributes.get("basePackages")) {
      if (StringUtils.hasText(pkg)) {
        basePackages.add(pkg);
      }
    }
    for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
      basePackages.add(ClassUtils.getPackageName(clazz));
    }

    if (basePackages.isEmpty()) {
      basePackages.add(
          ClassUtils.getPackageName(importingClassMetadata.getClassName()));
    }
    basePackages.add("cn.ddlover.job.service");
    return basePackages;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
