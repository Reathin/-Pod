package com.rair.pod.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.rair.pod.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created on 2017/9/7
 *
 * @author Rair.
 * @description Activity基类，采用MVP模式
 */
public abstract class BaseActivity<P extends IPresent> extends SupportActivity implements IView<P> {

    private P p;
    protected Activity context;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getP();
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
        }
        BaseApplication.getInstance().addActivity(this);
        initView(savedInstanceState);
        initData();
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = ButterKnife.bind(this);
    }

    protected P getP() {
        if (p == null) {
            p = newP();
        }
        if (p != null) {
            if (!p.hasV()) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getP() != null) {
            getP().detachV();
        }
        p = null;
        unbinder.unbind();
        BaseApplication.getInstance().finishActivity(this);
    }

    /**
     * 显示toasty
     *
     * @param text 提示信息
     */
    public void showToasty(String text) {
        Toasty.info(AppUtils.getContext(), text, Toast.LENGTH_SHORT, false).show();
    }
}
