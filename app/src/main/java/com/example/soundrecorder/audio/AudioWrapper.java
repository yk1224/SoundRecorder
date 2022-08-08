package com.example.soundrecorder.audio;

public class AudioWrapper {
    private AudioRecordWrapper audioRecordWrapper;
    private AudioTrackWrapper audioTrackWrapper;
    private int samplingRate = 44100;

    public AudioWrapper(String filePath) {
        audioRecordWrapper = new AudioRecordWrapper(samplingRate, filePath);
        audioTrackWrapper = new AudioTrackWrapper(samplingRate, filePath);
    }

    public void startRecord() {
        audioRecordWrapper.start();
    }

    public void stopRecord() {
        audioRecordWrapper.stop();
    }

    public void startPlay() {
        try {
            audioTrackWrapper.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        audioTrackWrapper.stop();
    }
}
