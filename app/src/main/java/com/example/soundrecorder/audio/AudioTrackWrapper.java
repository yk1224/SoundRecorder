package com.example.soundrecorder.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.soundrecorder.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioTrackWrapper {

    private int SamplingRate;
    private String filePath;
    private AudioTrack audioTrack;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public AudioTrackWrapper(int SamplingRate, String filePath) {
        this.SamplingRate = SamplingRate;
        this.filePath = filePath;
        this.initAudioTrack();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAudioTrack() {
        // バッファサイズの計算
        int bufSize = android.media.AudioTrack.getMinBufferSize(
                SamplingRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        // AudioTrack.Builder API level 26より
        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SamplingRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(bufSize)
                .build();
    }

    public void start() throws IOException {
        // wavを読み込む
        FileInputStream input = new FileInputStream(this.filePath);
        byte[] wavData = new byte[input.available()];
        // input.read(wavData)
        input.read(wavData);
        input.close();

        audioTrack.play();
        audioTrack.write(wavData, 44, wavData.length-44);
    }

    public void stop() {
        audioTrack.stop();
    }
}
