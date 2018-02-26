package com.rair.pod.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rair.pod.bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rair on 2017/7/21.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */
public class MusicService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private Context context;
    /**
     * MediaPlayer对象
     */
    private MediaPlayer mMediaPlayer;
    /**
     * 歌曲播放进度
     */
    private int currentTime = 0;
    private int currentItem = -1;//当前播放第几首歌
    private ArrayList<Music> songs;//要播放的歌曲集合

    @Override
    public void onCreate() {
        super.onCreate();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 根据歌曲存储路径播放歌曲
     */
    public void playMusic(String path) {
        try {
            /* 重置MediaPlayer */
            mMediaPlayer.reset();
            /* 设置要播放的文件的路径 */
            mMediaPlayer.setDataSource(path);
            /* 准备播放 */
            mMediaPlayer.prepare();
            /* 开始播放 */
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextMusic();
                }
            });
        } catch (IOException e) {
        }
    }

    /**
     * 下一首
     */
    public void nextMusic() {
        if (++currentItem >= songs.size()) {
            currentItem = 0;
        }
        playMusic(songs.get(currentItem).getPath());
    }

    /**
     * 上一首
     */
    public void prevMusic() {
        if (--currentItem < 0) {
            currentItem = songs.size() - 1;
        }
        playMusic(songs.get(currentItem).getPath());
    }

    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        if (mMediaPlayer.isPlaying()) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return currentTime;
        }
    }

    /**
     * 跳到输入的进度
     */
    public void movePlay(int progress) {
        mMediaPlayer.seekTo(progress);
        currentTime = progress;
    }

    /**
     * 歌曲是否真在播放
     */
    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * 暂停或开始播放歌曲
     */
    public void pausePlay() {
        if (mMediaPlayer.isPlaying()) {
            currentTime = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    public String getSongName() {
        return songs.get(currentItem).getName();
    }

    public String getAlbum() {
        return songs.get(currentItem).getAlbum();
    }

    public String getAlbumPic() {
        return songs.get(currentItem).getPic();
    }

    public String getSingerName() {
        return songs.get(currentItem).getSinger();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentListItme) {
        this.currentItem = currentListItme;
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Music> songs) {
        this.songs = songs;
    }

    /**
     * 自定义绑定Service类，通过这里的getService得到Service，
     * 之后就可调用Service这里的方法了
     */
    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
