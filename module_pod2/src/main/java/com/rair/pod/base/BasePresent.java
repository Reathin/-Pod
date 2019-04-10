package com.rair.pod.base;

import java.lang.ref.WeakReference;

/**
 *
 * @author aill
 * @date 2017/9/7
 */

public class BasePresent<V extends IView> implements IPresent<V> {

    private WeakReference<V> v;

    @Override
    public void attachV(V view) {
        v = new WeakReference<>(view);
    }

    @Override
    public void detachV() {
        if (v.get() != null) {
            v.clear();
        }
        v = null;
    }

    protected V getV() {
        if (v == null || v.get() == null) {
            throw new IllegalStateException("V不能为空！");
        }
        return v.get();
    }

    @Override
    public boolean hasV() {
        return v != null && v.get() != null;
    }
}
