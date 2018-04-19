package com.rair.pod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rair.pod.R;
import com.rair.pod.bean.Music;
import com.rair.pod.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Rair on 2017/7/20.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */
public class MusicListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Music> datas;

    public MusicListAdapter(Context context, ArrayList<Music> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music music = datas.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_music_item, parent, false);
            holder.tvSong = convertView.findViewById(R.id.music_item_tv_song);
            holder.tvDuration = convertView.findViewById(R.id.music_item_tv_duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSong.setText(music.getName());
        holder.tvDuration.setText(MusicUtil.formatTime(music.getDuration()));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvSong, tvDuration;
    }
}
