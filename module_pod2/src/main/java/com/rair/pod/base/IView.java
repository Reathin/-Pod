package com.rair.pod.base;

import android.os.Bundle;
import android.view.View;

/**
 * @author wanglei
 * @date 2016/12/29
 */

public interface IView<P> {
    /**
     * ButterKnife Bind
     *
     * @param rootView rootView (Activity null,Fragment rootView)
     */
    void bindUI(View rootView);

    /**
     * 初始化视图
     *
     * @param savedInstanceState savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 布局id
     *
     * @return 例如：R.layout.activity_main
     */
    int getLayoutId();

    /**
     * OptionMenu id
     *
     * @return 例如：R.menu.menu_main
     */
    int getOptionMenuId();

    /**
     * newP()
     *
     * @return <P extends BasePresent></P>
     */
    P newP();
}
