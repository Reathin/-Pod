package com.rair.pod.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rair.pod.MainActivity;
import com.rair.pod.R;
import com.rair.pod.base.RairApp;
import com.rair.pod.constant.Constants;
import com.rair.pod.service.MusicService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.about_tv_join)
    TextView aboutTvJoin;
    Unbinder unbinder;
    private AboutReceiver receiver;
    private LocalBroadcastManager broadcastManager;
    private Handler handler;
    private MusicService musicService;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        register();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        musicService.pausePlay();
                        break;
                    case 1:
                        if (musicService.isPlay())
                            musicService.prevMusic();
                        break;
                    case 2:
                        if (musicService.isPlay())
                            musicService.nextMusic();
                        break;
                    case 3:
                        MainActivity activity = (MainActivity) getActivity();
                        activity.showFragment(1);
                        break;
                    case 4:
                        musicService.pausePlay();
                        break;
                }
            }
        };
    }

    private void register() {
        musicService = RairApp.getApp().getService();
        receiver = new AboutReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEND);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    @OnClick(R.id.about_tv_join)
    public void onViewClicked() {
        joinQQGroup(Constants.QQ_KEY);
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    class AboutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra(Constants.ACTION_KEY);
            Message message = Message.obtain(handler);
            switch (key) {
                case Constants.ACTION_OK:
                    message.what = 0;
                    break;
                case Constants.ACTION_PREV:
                    message.what = 1;
                    break;
                case Constants.ACTION_NEXT:
                    message.what = 2;
                    break;
                case Constants.ACTION_MENU:
                    message.what = 3;
                    break;
                case Constants.ACTION_PLAY:
                    message.what = 4;
                    break;
            }
            handler.sendMessage(message);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) broadcastManager.unregisterReceiver(receiver);
        else register();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        broadcastManager.unregisterReceiver(receiver);
        unbinder.unbind();
    }
}
