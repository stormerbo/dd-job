package cn.ddlover.job.rpc;

import cn.ddlover.job.exception.InvokeNotSupportedException;
import cn.ddlover.job.proxy.RpcInvokeProxy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 18:59
 */
@Getter
@Setter
public class RpcClientFactoryBean implements FactoryBean<Object> {

  private Class<?> type;


  @Override
  public Object getObject() throws Exception {
    if (!this.type.isInterface()) {
      throw new InvokeNotSupportedException("RpcInvoke only support interface.");
    }
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(this.type);
    enhancer.setCallback(new RpcInvokeProxy());
    return enhancer.create();
  }

  @Override
  public Class<?> getObjectType() {
    return this.type;
  }
}
