package com.example.ashram_app.ui.meditation;

public class AudioProperties {
    String audioName, audioURL;

    public AudioProperties() {
    }

    public AudioProperties(String audioName, String audioURL) {
        this.audioName = audioName;
        this.audioURL = audioURL;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }
}