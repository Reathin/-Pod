package com.rair.pod.base;

/**
 *
 * @author wanglei
 * @date 2016/12/29
 */

public interface IPresent<V> {
    /**
     * attachV
     *
     * @param view view
     */
    void attachV(V view);

    /**
     * detachV
     */
    void detachV();

    /**
     * 是否有v
     *
     * @return Boolean
     */
    boolean hasV();
}
