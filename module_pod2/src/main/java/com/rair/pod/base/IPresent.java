package com.rair.pod.base;

/**
 *
 * @author wanglei
 * @date 2016/12/29
 */

public interface IPresent<V> {

    void attachV(V view);

    void detachV();
}
