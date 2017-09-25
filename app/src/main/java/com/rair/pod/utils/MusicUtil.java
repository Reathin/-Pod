package com.rair.pod.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.rair.pod.bean.Music;

import java.util.ArrayList;

/**
 * Created by Rair on 2017/7/20.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */

public class MusicUtil {

    /**
     * 得到媒体库中的全部歌曲
     *
     * @param context context
     * @return 集合
     */
    public static ArrayList<Music> getAllMusics(Context context) {
        ArrayList<Music> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Music music = new Music();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                long size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String albumArt = getAlbumArt(context, albumId);
                music.setName(name);
                music.setSinger(singer);
                music.setPath(path);
                music.setDuration(duration);
                music.setSize(size);
                music.setAlbum(album);
                music.setPic(albumArt);
                list.add(music);
            }
            cursor.close();
        }
        return list;
    }

    private static String getAlbumArt(Context context, int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cursor = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id), projection, null, null, null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            album_art = cursor.getString(0);
        }
        cursor.close();
        return album_art;
    }

    /**
     * 格式化时间
     *
     * @param duration 时长
     * @return 00:00
     */
    public static String formatTime(long duration) {
        if (duration / 1000 % 60 < 10) {
            return duration / 1000 / 60 + ":0" + duration / 1000 % 60;
        }
        return duration / 1000 / 60 + ":" + duration / 1000 % 60;
    }
}
