package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;

import org.smart4j.framework.helper.DatabasesHelper;

import java.lang.reflect.Method;


public class TransactionProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue(){
            return false;
        }
    };



    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result;
        boolean flag =FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                DatabasesHelper.beginTransaction();
                LOGGER.debug("begin traction");
                result=proxyChain.doProxyChain();
                DatabasesHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            } catch (Throwable e) {
                DatabasesHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;

            } finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result =proxyChain.doProxyChain();
        }
        return result;


    }
}
