package com.rair.pod.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rair.pod.MainActivity;
import com.rair.pod.R;
import com.rair.pod.adapter.OptionRvAdapter;
import com.rair.pod.base.RairApp;
import com.rair.pod.constant.Constants;
import com.rair.pod.service.MusicService;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionFragment extends Fragment implements OptionRvAdapter.OnItemClickListener {

    @BindView(R.id.option_rv_menu)
    RecyclerView optionRvMenu;
    Unbinder unbinder;
    private String[] options = {"音乐", "关于"};
    private int[] icons = {R.mipmap.ic_music, R.mipmap.ic_about};
    private Handler handler;
    private LocalBroadcastManager broadcastManager;
    private OptionReceiver receiver;
    private int selPosition = 0;
    private MusicService musicService;
    private MainActivity activity;

    public static OptionFragment newInstance() {
        return new OptionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_option, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        activity = (MainActivity) getActivity();
        ArrayList<HashMap<String, Object>> maps = initData();
        new LinearSnapHelper().attachToRecyclerView(optionRvMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        optionRvMenu.setLayoutManager(layoutManager);
        OptionRvAdapter adapter = new OptionRvAdapter(getContext(), maps);
        optionRvMenu.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        register();

        //如果在播放直接到播放
        if (musicService.isPlay()) activity.showFragment(4);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0: //Ok
                        itemClick(selPosition);
                        break;
                    case 1: //上一个
                        optionRvMenu.smoothScrollToPosition(0);
                        selPosition = 0;
                        break;
                    case 2: //下一个
                        optionRvMenu.smoothScrollToPosition(1);
                        selPosition = 1;
                        break;
                    case 3:
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
        receiver = new OptionReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEND);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void itemClick(int position) {
        MainActivity activity = (MainActivity) getActivity();
        switch (position) {
            case 0: //音乐
                activity.showFragment(2);
                break;
            case 1: //关于
                activity.showFragment(3);
                break;
        }
    }

    private ArrayList<HashMap<String, Object>> initData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        for (int i = 0; i < options.length; i++) {
            map = new HashMap<>();
            map.put("option", options[i]);
            map.put("icon", icons[i]);
            list.add(map);
        }
        return list;
    }

    class OptionReceiver extends BroadcastReceiver {
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
