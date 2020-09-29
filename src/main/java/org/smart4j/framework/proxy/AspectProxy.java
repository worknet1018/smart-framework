package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public abstract class AspectProxy implements Proxy{
    
    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);
    
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result =null;
        
        Class<?> cls =proxyChain.getTargetClass();
        Method method =proxyChain.getTargetMethod();
        Object[] params =proxyChain.getMethodParams();
        
        begin();
        try {
            if (intercept(cls,method,params)){
                before(cls,method,params);
                result =proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else {
                result =proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            logger.error("proxy failure",e);
            error(cls,method,params,e);
        } finally {
            end();
        }

        return result;
    }

    private void end() {
    }

    private void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
    }

    private void before(Class<?> cls, Method method, Object[] params) throws Throwable{
    }

    private boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable{
        return true;
    }

    public void error(Class<?> cls,Method method,Object[] params,Throwable e){
        
    }
    private void begin() {
    }
}
