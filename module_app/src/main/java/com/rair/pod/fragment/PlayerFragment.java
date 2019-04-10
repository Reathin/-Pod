package com.rair.pod.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rair.pod.MainActivity;
import com.rair.pod.R;
import com.rair.pod.base.RairApp;
import com.rair.pod.constant.Constants;
import com.rair.pod.service.MusicService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.rair.pod.utils.MusicUtil.formatTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.play_iv_pic)
    ImageView playIvPic;
    @BindView(R.id.play_tv_name)
    TextView playTvName;
    @BindView(R.id.play_tv_singer)
    TextView playTvSinger;
    @BindView(R.id.play_tv_album)
    TextView playTvAlbum;
    @BindView(R.id.play_tv_current_time)
    TextView playTvCurrentTime;
    @BindView(R.id.play_sb_progress)
    SeekBar playSbProgress;
    @BindView(R.id.play_tv_total_time)
    TextView playTvTotalTime;
    Unbinder unbinder;
    private MusicService musicService;
    private Handler handler;
    private LocalBroadcastManager broadcastManager;
    private PlayReceiver receiver;

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        musicService = RairApp.getApp().getService();
        playSbProgress.setOnSeekBarChangeListener(this);

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
                        musicService.prevMusic();
                        break;
                    case 2:
                        musicService.nextMusic();
                        break;
                    case 3:
                        MainActivity activity = (MainActivity) getActivity();
                        activity.showFragment(2);
                        break;
                    case 4:
                        musicService.pausePlay();
                        break;
                    case 5:
                        int currentPrev = musicService.getCurrent();
                        musicService.movePlay(currentPrev - 2000);
                        break;
                    case 6:
                        int currentNext = musicService.getCurrent();
                        musicService.movePlay(currentNext + 2000);
                        break;
                }
            }
        };
        handler.post(updateThread);
    }

    private void register() {
        receiver = new PlayReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEND);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    private Runnable updateThread = new Runnable() {
        public void run() {
            if (null != musicService.getAlbumPic()) {
                Bitmap bitmap = BitmapFactory.decodeFile(musicService.getAlbumPic());
                playIvPic.setImageBitmap(bitmap);
            } else {
                playIvPic.setImageResource(R.mipmap.pic_holder);
            }
            playSbProgress.setMax(musicService.getDuration());
            playTvName.setText(musicService.getSongName());
            playTvSinger.setText(musicService.getSingerName());
            playTvAlbum.setText(musicService.getAlbum());
            playTvTotalTime.setText(formatTime(musicService.getDuration()));
            playSbProgress.setProgress(musicService.getCurrent());
            playTvCurrentTime.setText(formatTime(musicService.getCurrent()));
            handler.postDelayed(updateThread, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) musicService.movePlay(progress);
    }

    class PlayReceiver extends BroadcastReceiver {
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
        if (isHidden()) broadcastManager.unregisterReceiver(receiver);
        else register();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateThread);
        broadcastManager.unregisterReceiver(receiver);
        handler = null;
        unbinder.unbind();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
