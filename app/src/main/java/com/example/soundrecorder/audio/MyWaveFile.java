package com.example.soundrecorder.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MyWaveFile {

    private final int FILESIZE_SEEK = 4;
    private final int DATASIZE_SEEK = 40;

    // フォーマットID リニアPCMの場合01 00 2byte
    private final byte[] FMT_ID = {1, 0};
    //チャネルカウント モノラルなので1 ステレオなら2にする
    private final short chCount = 1;
    //fmtチャンクのバイト数
    private final int FMT_SIZE = 16;
    //ブロックサイズ (Byte/サンプリングレート * チャンネル数)
    private final short blockSize = (short) ((FMT_SIZE / 8) * chCount);
    //サンプルあたりのビット数 WAVでは8bitか16ビットが選べる
    private final short bitPerSample = 16;

    //wavファイルリフチャンクに書き込むチャンクID用
    private final byte[] RIFF = "RIFF".getBytes();
    //WAV形式でRIFFフォーマットを使用する
    private final byte[] WAVE= "WAVE".getBytes();
    //fmtチャンク　スペースも必要
    private final byte[] FMT = "fmt ".getBytes();
    //dataチャンク
    private final byte[] data = "data".getBytes();

    //リアルタイム処理なのでランダムアクセスファイルクラスを使用する
    private RandomAccessFile raf;
    private final String filePath;
    private File recFile;
    private int fileSize = 36;
    private final int samplingRate;
    private int bytePerSec;
    //波形データのバイト数
    private int dataSize = 0;


    public MyWaveFile(String filePath, int samplingRate) {
        this.filePath = filePath;
        // サンプリング周波数
        this.samplingRate = samplingRate;
        //データ速度
        this.bytePerSec = this.samplingRate * (FMT_SIZE / 8) * chCount;
    }

    public void createFile() {
        //	ファイルを作成
        recFile = new File(this.filePath);
        if (recFile.exists()) {
            recFile.delete();
        }
        try {
            recFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            raf = new RandomAccessFile(recFile, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //wavのヘッダを書き込み
        try {
            raf.seek(0);
            raf.write(RIFF);
            raf.write(littleEndianInteger(fileSize));
            raf.write(WAVE);
            raf.write(FMT);
            raf.write(littleEndianInteger(FMT_SIZE));
            raf.write(FMT_ID);
            raf.write(littleEndianShort(chCount));
            //サンプリング周波数
            raf.write(littleEndianInteger(this.samplingRate));
            raf.write(littleEndianInteger(bytePerSec));
            raf.write(littleEndianShort(blockSize));
            raf.write(littleEndianShort(bitPerSample));
            raf.write(data);
            raf.write(littleEndianInteger(dataSize));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private byte[] littleEndianInteger(int i) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) i;
        buffer[1] = (byte) (i >> 8);
        buffer[2] = (byte) (i >> 16);
        buffer[3] = (byte) (i >> 24);

        return buffer;
    }

    // PCMデータを追記するメソッド
    public void addBigEndianData(short[] shortData) {

        // ファイルにデータを追記
        try {
            raf.seek(raf.length());
            raf.write(littleEndianShorts(shortData));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ファイルサイズを更新
        updateFileSize();
        // データサイズを更新
        updateDataSize();
    }

    // ファイルサイズを更新
    private void updateFileSize() {
        fileSize = (int) (recFile.length() - 8);
        byte[] fileSizeBytes = littleEndianInteger(fileSize);
        try {
            raf.seek(FILESIZE_SEEK);
            raf.write(fileSizeBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // データサイズを更新
    private void updateDataSize() {
        dataSize = (int) (recFile.length() - 44);
        byte[] dataSizeBytes = littleEndianInteger(dataSize);
        try {
            raf.seek(DATASIZE_SEEK);
            raf.write(dataSizeBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // short型変数をリトルエンディアンのbyte配列に変更
    private byte[] littleEndianShort(short s) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) s;
        buffer[1] = (byte) (s >> 8);

        return buffer;
    }

    // short型配列をリトルエンディアンのbyte配列に変更
    private byte[] littleEndianShorts(short[] s) {
        byte[] buffer = new byte[s.length * 2];
        int i;

        for (i = 0; i < s.length; i++) {
            buffer[2 * i] = (byte) s[i];
            buffer[2 * i + 1] = (byte) (s[i] >> 8);
        }
        return buffer;
    }


    // ファイルを閉じる
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
