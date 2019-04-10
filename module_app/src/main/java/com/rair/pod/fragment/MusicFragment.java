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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rair.pod.MainActivity;
import com.rair.pod.R;
import com.rair.pod.adapter.MusicListAdapter;
import com.rair.pod.base.RairApp;
import com.rair.pod.bean.Music;
import com.rair.pod.constant.Constants;
import com.rair.pod.service.MusicService;
import com.rair.pod.utils.MusicUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.music_list_view)
    ListView musicListView;
    @BindView(R.id.music_ll_empty)
    LinearLayout musicLlEmpty;
    Unbinder unbinder;
    private MusicService musicService;
    private ArrayList<Music> songs;
    private Handler handler;
    private MusicReceiver receiver;
    private LocalBroadcastManager broadcastManager;
    private int selPosition = 0;
    private MusicListAdapter adapter;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        songs = MusicUtil.getAllMusics(getContext());
       adapter = new MusicListAdapter(getContext(), songs);
        musicListView.setAdapter(adapter);
        musicListView.setOnItemClickListener(this);
        musicListView.setEmptyView(musicLlEmpty);

        register();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (songs.size() > 0)
                            playClick(selPosition);
                        break;
                    case 1:
                        if (selPosition > 0 && selPosition <= songs.size())
                            moveTo(selPosition--);
                        break;
                    case 2:
                        if (selPosition >= 0 && selPosition < songs.size())
                            moveTo(selPosition++);
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
        receiver = new MusicReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEND);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    private void moveTo(int position) {
        musicListView.setSelection(position);
    }

    private void playClick(int position) {
        MainActivity activity = (MainActivity) getActivity();
        if (null != musicService) {
            Music music = songs.get(position);
            musicService.setCurrentItem(position);
            musicService.setSongs(songs);
            musicService.playMusic(music.getPath());
            activity.showFragment(4);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playClick(position);
    }

    class MusicReceiver extends BroadcastReceiver {
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
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        broadcastManager.unregisterReceiver(receiver);
        unbinder.unbind();
    }
}
