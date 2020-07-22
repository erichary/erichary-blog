package com.erichary.blog.utils;

/**
 * 线程回调接口
 *
 * @author lipengchao
 */
public interface ExtendMethod<T, P> {
    /**
     * 接口回调方法
     *
     * @param param P
     * @return T
     */
    T executeMethod(P param);
}
