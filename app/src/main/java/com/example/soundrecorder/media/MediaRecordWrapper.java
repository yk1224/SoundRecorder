package com.example.soundrecorder.media;

import android.media.MediaRecorder;

import java.io.File;

public class MediaRecordWrapper {
    //録音
    private MediaRecorder mediarecorder; //録音用のメディアレコーダークラス
    String filePath;

    public MediaRecordWrapper(String filePath) {
        this.filePath = filePath;
    }

    public void startMediaRecord(){
        try{
            File mediafile = new File(filePath);
            if(mediafile.exists()) {
                //ファイルが存在する場合は削除する
                mediafile.delete();
            }
            mediafile = null;
            mediarecorder = new MediaRecorder();
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(filePath);
            //録音の準備をする
            mediarecorder.prepare();
            //録音開始
            mediarecorder.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //停止
    public boolean stopRecord(){
        if(mediarecorder != null){
            try{
                //録音停止
                mediarecorder.stop();
                mediarecorder.reset();
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
