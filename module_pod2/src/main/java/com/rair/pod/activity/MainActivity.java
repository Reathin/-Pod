package com.rair.pod.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rair.pod.R;
import com.rair.pod.base.BaseActivity;
import com.rair.pod.event.MenuEvent;
import com.rair.pod.event.NextEvent;
import com.rair.pod.event.OkEvent;
import com.rair.pod.event.PlayEvent;
import com.rair.pod.event.PrevEvent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.tv_permission)
    TextView tvPermission;
    @BindView(R.id.iv_ok)
    ImageView ivOk;
    @BindView(R.id.iv_prev)
    ImageView ivPrev;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        if (!AndPermission.hasPermission(this, Permission.STORAGE)) {

        } else {

        }
    }

    @OnClick({R.id.iv_ok, R.id.iv_prev, R.id.iv_next, R.id.iv_menu, R.id.iv_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ok:
                EventBus.getDefault().post(new OkEvent());
                break;
            case R.id.iv_prev:
                EventBus.getDefault().post(new PrevEvent());
                break;
            case R.id.iv_next:
                EventBus.getDefault().post(new NextEvent());
                break;
            case R.id.iv_menu:
                EventBus.getDefault().post(new MenuEvent());
                break;
            case R.id.iv_play:
                EventBus.getDefault().post(new PlayEvent());
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getOptionMenuId() {
        return 0;
    }

    @Override
    public Object newP() {
        return null;
    }
}
