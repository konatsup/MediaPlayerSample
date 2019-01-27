package com.konatsup.mediaplayersample;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private MediaPlayer mediaPlayer;
    private PlaybackParams params;
    private float bpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        bpm = 120;
        params = new PlaybackParams();

    }

    private boolean audioSetup() {
        boolean fileCheck = false;

        // rawにファイルがある場合
        mediaPlayer = MediaPlayer.create(this, R.raw.snare);
        // 音量調整を端末のボタンに任せる
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        fileCheck = true;

        return fileCheck;
    }

    private void audioPlay() {

        if (mediaPlayer == null) {
            // audio ファイルを読出し
            if (audioSetup()) {
                Toast.makeText(getApplication(), "Rread audio file", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // 繰り返し再生する場合
            mediaPlayer.stop();
            mediaPlayer.reset();
            // リソースの解放
            mediaPlayer.release();
        }
        mediaPlayer.setLooping(true);
        // 再生する
        mediaPlayer.start();

        // 終了を検知するリスナー
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("debug", "end of audio");
                audioStop();
            }
        });
    }

    private void audioStop() {
        // 再生終了
        mediaPlayer.stop();
        // リセット
        mediaPlayer.reset();
        // リソースの解放
        mediaPlayer.release();

        mediaPlayer = null;
    }


    public void start(View v) {
        audioPlay();
        textView.setText(bpm + "");
    }

    public void stop(View v) {
        if (mediaPlayer != null) {
            // 音楽停止
            audioStop();
            textView.setText(bpm + "");
        }
    }

    public void up(View v) {
        bpm += 10f;
        params.setSpeed((float) bpm / 120f);
        mediaPlayer.setPlaybackParams(params);
        textView.setText(bpm + "");
    }

    public void down(View v) {
        bpm -= 10f;
        params.setSpeed((float) bpm / 120f);
        mediaPlayer.setPlaybackParams(params);
        textView.setText(bpm + "");
    }

}
