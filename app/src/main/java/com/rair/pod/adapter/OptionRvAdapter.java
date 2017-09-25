package com.rair.pod.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rair.pod.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rair on 2017/7/21.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */

public class OptionRvAdapter extends RecyclerView.Adapter<OptionRvAdapter.OptionHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> datas;
    private OptionRvAdapter.OnItemClickListener onItemClickListener;

    public OptionRvAdapter(Context context, ArrayList<HashMap<String, Object>> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public OptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_menu_item, parent, false);
        return new OptionHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionHolder holder, final int position) {
        HashMap<String, Object> map = datas.get(position);
        String option = (String) map.get("option");
        int icon = (int) map.get("icon");
        holder.ivIcon.setImageResource(icon);
        holder.tvOption.setText(option);
        final int mPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener)
                    onItemClickListener.itemClick(mPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OptionHolder extends RecyclerView.ViewHolder {

        private TextView tvOption;
        private ImageView ivIcon;

        OptionHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.menu_item_iv_icon);
            tvOption = (TextView) itemView.findViewById(R.id.menu_item_tv_option);
        }
    }

    public interface OnItemClickListener {

        void itemClick(int position);
    }

    public void setOnItemClickListener(OptionRvAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
