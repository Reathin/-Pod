package com.rair.pod.bean;

/**
 * Created by Rair on 2017/7/20.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */
public class Music {

    private int id;
    private String album;//专辑名字
    private String singer;//歌手名字
    private String path;//sd卡路径
    private String name;//歌曲名字
    private long duration;//时长
    private long size;//大小
    private String pic;//图片路径

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
