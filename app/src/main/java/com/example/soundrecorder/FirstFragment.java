package com.example.soundrecorder;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.soundrecorder.audio.AudioWrapper;
import com.example.soundrecorder.databinding.FragmentFirstBinding;
import com.example.soundrecorder.media.MediaRecordWrapper;

import java.io.File;
import java.util.Arrays;

public class FirstFragment extends Fragment {
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private final int REQUEST_PERMISSION = 1000;

    private FragmentFirstBinding binding;
    private static final int STOPPING = 0;
    private static final int RECORDING = 1;
    private static final int PLAYING = 2;
    private int status = STOPPING;

    private static final int MODE_AUDOI_RECORDER = 1;
    private static final int MODE_MEDIA_RECORDER = 2;
    private int mode = MODE_AUDOI_RECORDER;

    File extDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MUSIC);

    AudioWrapper audioWrapper = new AudioWrapper(extDir.getAbsoluteFile() + "/audiorec.wav");
    MediaRecordWrapper mediaRecordWrapper = new MediaRecordWrapper(extDir.getAbsoluteFile() + "/mediarec.wav");

//    AudioWrapper audioWrapper = new AudioWrapper("/storage/sdcard/Android/data/audiorec.wav");
//    MediaRecordWrapper mediaRecordWrapper = new MediaRecordWrapper("/storage/sdcard/Android/data/mediarec.wav");


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        status = STOPPING;
        mode = MODE_AUDOI_RECORDER;
        viewRefresh();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonModeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == MODE_AUDOI_RECORDER) {
                    mode = MODE_AUDOI_RECORDER;
                } else {
                    mode = MODE_MEDIA_RECORDER;
                }
                status = STOPPING;
                viewRefresh();
            }
        });

        binding.buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == STOPPING) {
                    if (mode == MODE_AUDOI_RECORDER) {
                        audioWrapper.startRecord();
                    } else {
                        mediaRecordWrapper.startMediaRecord();
                    }
                    status = RECORDING;
                } else {
                    if (mode == MODE_AUDOI_RECORDER) {
                        audioWrapper.stopRecord();
                    } else {
                        mediaRecordWrapper.stopRecord();
                    }
                    status = STOPPING;
                }
                viewRefresh();
            }
        });

        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == STOPPING) {
                    if (mode == MODE_AUDOI_RECORDER) {
                        audioWrapper.startPlay();
                    } else {

                    }
                    status = PLAYING;
                } else {
                    if (mode == MODE_AUDOI_RECORDER) {
                        audioWrapper.stopPlay();
                    } else {

                    }
                    status = STOPPING;
                }
                viewRefresh();
            }
        });
    }

    private void viewRefresh() {
        modeView();
        statusView();
    }

    private void modeView() {
        if (mode == MODE_AUDOI_RECORDER) {
            binding.buttonModeChange.setText(R.string.mode_audio_recorder);
        } else if (mode == MODE_MEDIA_RECORDER) {
            binding.buttonModeChange.setText(R.string.mode_media_recorder);
        }
    }

    private void statusView() {
        if (status == STOPPING) {
            binding.textviewFirst.setText(R.string.stopping);
            binding.buttonRecord.setText(R.string.record);
            binding.buttonRecord.setEnabled(true);
            binding.buttonPlay.setText(R.string.play);
            binding.buttonPlay.setEnabled(true);
        } else if (status == RECORDING) {
            binding.textviewFirst.setText(R.string.recording);
            binding.buttonRecord.setText(R.string.stop);
            binding.buttonRecord.setEnabled(true);
            binding.buttonPlay.setText(R.string.play);
            binding.buttonPlay.setEnabled(false);
        } else if (status == PLAYING) {
            binding.textviewFirst.setText(R.string.playing);
            binding.buttonRecord.setText(R.string.record);
            binding.buttonRecord.setEnabled(false);
            binding.buttonPlay.setText(R.string.stop);
            binding.buttonPlay.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("permissions: " + Arrays.asList(permissions).toString());
    }

}