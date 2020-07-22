package com.xiake.otur.util;


import com.erichary.blog.utils.ExtendMethod;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 子线程执行指定方法类
 *
 * @param <T> 执行完成返回的类型
 * @param <P> 参数类型
 * @author lipengchao
 */
public class ThreadUtil<T, P> implements Callable<T> {

    /**
     * 线程需要执行的方法
     */
    private final ExtendMethod<T, P> extendMethod;

    /**
     * 方法执行所需参数
     */
    private final P param;

    public ThreadUtil(ExtendMethod<T, P> extendMethod, P param) {
        this.extendMethod = extendMethod;
        this.param = param;
    }

    @Override
    public T call() throws Exception {
        return extendMethod.executeMethod(param);
    }
}