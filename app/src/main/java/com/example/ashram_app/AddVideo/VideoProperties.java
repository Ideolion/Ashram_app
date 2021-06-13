package com.example.ashram_app.AddVideo;

public class VideoProperties {
    private String name;
    private String videourl;
    private String search;

    public VideoProperties(String name, String videourl, String search) {
        this.name = name;
        this.videourl = videourl;
        this.search = search;
    }

    public VideoProperties() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }




}

