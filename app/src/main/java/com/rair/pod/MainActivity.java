package com.rair.pod;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rair.pod.base.RairApp;
import com.rair.pod.constant.Constants;
import com.rair.pod.fragment.AboutFragment;
import com.rair.pod.fragment.MusicFragment;
import com.rair.pod.fragment.OptionFragment;
import com.rair.pod.fragment.PlayerFragment;
import com.rair.pod.fragment.StartFragment;
import com.rair.pod.service.MusicService;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_fl_container)
    FrameLayout mainFlContainer;
    @BindView(R.id.main_iv_ok)
    ImageView mainIvOk;
    @BindView(R.id.main_iv_prev)
    ImageView mainIvPrev;
    @BindView(R.id.main_iv_next)
    ImageView mainIvNext;
    @BindView(R.id.main_iv_menu)
    ImageView mainIvMenu;
    @BindView(R.id.main_iv_play)
    ImageView mainIvPlay;
    @BindView(R.id.main_tv_permission)
    TextView mainTvPermission;
    private Unbinder bind;
    private int lastIndex = -1;
    private Fragment[] fragments;
    private Handler handler;
    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        musicService = RairApp.getApp().getService();
        fragments = new Fragment[5];
        handler = new Handler();
        showFragment(0);
        checkPermission();
    }

    @OnClick({R.id.main_iv_ok, R.id.main_iv_prev, R.id.main_iv_next, R.id.main_iv_menu, R.id.main_iv_play})
    public void onViewClicked(View view) {
        Intent intent = new Intent(Constants.ACTION_SEND);
        switch (view.getId()) {
            case R.id.main_iv_ok:
                intent.putExtra(Constants.ACTION_KEY, Constants.ACTION_OK);
                break;
            case R.id.main_iv_prev:
                intent.putExtra(Constants.ACTION_KEY, Constants.ACTION_PREV);
                break;
            case R.id.main_iv_next:
                intent.putExtra(Constants.ACTION_KEY, Constants.ACTION_NEXT);
                break;
            case R.id.main_iv_menu:
                intent.putExtra(Constants.ACTION_KEY, Constants.ACTION_MENU);
                break;
            case R.id.main_iv_play:
                intent.putExtra(Constants.ACTION_KEY, Constants.ACTION_PLAY);
                break;
            default:
                break;
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void showFragment(int currentIndex) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentIndex == lastIndex) {
            return;
        }
        if (lastIndex != -1) {
            ft.hide(fragments[lastIndex]);
        }
        if (fragments[currentIndex] == null) {
            switch (currentIndex) {
                case 0:
                    fragments[currentIndex] = StartFragment.newInstance();
                    break;
                case 1:
                    fragments[currentIndex] = OptionFragment.newInstance();
                    break;
                case 2:
                    fragments[currentIndex] = MusicFragment.newInstance();
                    break;
                case 3:
                    fragments[currentIndex] = AboutFragment.newInstance();
                    break;
                case 4:
                    fragments[currentIndex] = PlayerFragment.newInstance();
                    break;
                default:
                    break;
            }
            ft.add(R.id.main_fl_container, fragments[currentIndex]);
        } else {
            ft.show(fragments[currentIndex]);
        }
        ft.commit();
        lastIndex = currentIndex;
    }

    private void checkPermission() {
        mainTvPermission.setText("我需要扫描音乐文件，请授予我权限。");
        AndPermission.with(this)
                .requestCode(520)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(this)
                .start();
    }

    @PermissionYes(520)
    private void getPermissionYes(List<String> grantedPermissions) {
        mainTvPermission.setText("欢迎使用...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainTvPermission.setVisibility(View.GONE);
                showFragment(1);
            }
        }, 2000);
    }

    @PermissionNo(520)
    private void getPermissionNo(List<String> deniedPermissions) {
        mainTvPermission.setText("没有必要的权限，无法进入系统。");
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, 400)
                    .setTitle("权限申请失败")
                    .setMessage("您拒绝了必要的权限，已经无法正常愉快的玩耍了，请在设置中手动授权！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 400:  // 这个400就是传入的数字。
                mainTvPermission.setText("欢迎使用...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainTvPermission.setVisibility(View.GONE);
                        showFragment(1);
                    }
                }, 2000);
                break;
            default:
                mainTvPermission.setText("没有必要的权限，无法进入系统。");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    private long exit = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exit) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exit = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        bind.unbind();
    }
}
