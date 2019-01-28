package com.konatsup.mediaplayersample;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int soundId;
    private int soundResource = R.raw.snare;
    private float bpm;
    private int count;
    private int speed;

    private Timer timer;
    private Handler handler;
    private long delay, period;

    private Button startButton, stopButton;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(getApplicationContext(), soundResource, 0);

        bpm = 120;
        speed = (int) (bpm / 120 * 50);
        textView.setText(bpm + "");

        handler = new Handler();

        count = 0;
        delay = 0;
        period = 10; // 0.01秒ごとにhandler内のrun()を呼ぶように

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer == null) {
                    timer = new Timer(false);
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    count++;
                                    speed = (int) ((120f / bpm) * 50);
                                    if (count % speed == 0) {
                                        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                                    }
                                }
                            });
                        }
                    }, delay, period);
                }
            }

        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        soundPool.release();
        super.onDestroy();
    }


    public void up(View v) {
        bpm += 1f;
        textView.setText(bpm + "");
    }

    public void down(View v) {
        bpm -= 1f;
        textView.setText(bpm + "");
    }

    public void up10(View v) {
        bpm += 10f;
        textView.setText(bpm + "");
    }

    public void down10(View v) {
        bpm -= 10f;
        textView.setText(bpm + "");
    }

}
