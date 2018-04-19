package com.rair.pod.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.rair.pod.utils.AndroidVersionUtil;
import com.rair.pod.utils.AppUtils;
import com.rair.pod.utils.DensityUtils;

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
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        BaseApplication.getIns().addActivity(this);
        bindUI(null);
        initView();
        initData();
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = ButterKnife.bind(this);
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
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
        BaseApplication.getIns().finishActivity(this);
        unbinder.unbind();
    }

    /**
     * 设置toolbar
     *
     * @param toolbar      toolbar
     * @param title        标题
     * @param isSetBackBtn 返回键
     */
    protected void setToolbar(Toolbar toolbar, String title, boolean isSetBackBtn) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (AndroidVersionUtil.isLollipop()) {
            toolbar.setElevation(DensityUtils.dip2px(this, 4));
        }
        if (isSetBackBtn && getSupportActionBar() != null) {
            //返回键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> ActivityCompat.finishAfterTransition(this));
        }
    }

    /**
     * 设置tool
     *
     * @param title             标题
     * @param isSetElevation    是否设置阴影
     * @param isSetBackBtn      是否设置返回键
     */
    protected void setToolbar(Toolbar toolbar, String title, boolean isSetBackBtn, boolean isSetElevation) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (AndroidVersionUtil.isLollipop() && isSetElevation) {
            //5.0以上，设置toolbar阴影
            toolbar.setElevation(8F);
        }
        if (isSetBackBtn) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> ActivityCompat.finishAfterTransition(this));
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * 显示toast
     *
     * @param msg
     */
    private Toast toast;

    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toasty.info(AppUtils.getContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
