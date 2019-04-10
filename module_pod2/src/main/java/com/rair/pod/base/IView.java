package com.rair.pod.base;

import android.view.View;

/**
 * @author wanglei
 * @date 2016/12/29
 */

public interface IView<P> {

    void bindUI(View rootView);

    void initView();

    void initData();

    int getLayoutId();

    P newP();
}
