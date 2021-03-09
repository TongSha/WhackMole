package com.example.whackmole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Boolean flag = false;
    private Boolean isShow = true;
    private Button startBtn;
    private ImageView showImg;
    private TextView txt;
    private MyHandler myHandler = new MyHandler(this);
    private int touchTime;
    private int showTime;
    private final static int MOLE_NUM = 5;
    private final static int WHACK_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start_btn);
        showImg = findViewById(R.id.show_img);
        txt = findViewById(R.id.txt);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setText("游戏中...");
                startBtn.setEnabled(false);
                showTime = 0;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            do {
                                showTime++;
                                Message message = new Message();
                                message.what = WHACK_NUM;
                                message.arg1 = touchTime;
                                message.arg2 = showTime;
                                myHandler.sendMessage(message);
                                int time = new Random().nextInt(1000) % (1000 - 500 + 1) + 1000;
                                sleep(time);
                            } while (showTime <= MOLE_NUM);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        showImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImg.setVisibility(View.GONE);
                touchTime++;
            }
        });
    }

    private static class MyHandler extends Handler {

        private final WeakReference<MainActivity> wr;
        public MyHandler(MainActivity ma) {
            wr = new WeakReference<>(ma);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity ma = wr.get();

            if (msg.arg2 <= MOLE_NUM) {
                int x = new Random().nextInt(800) % (800 - 100 + 1) + 100;
                int y = new Random().nextInt(1500) % (1500 - 500 + 1) + 500;
                ma.showImg.setX(x);
                ma.showImg.setY(y);
                ma.showImg.setVisibility(View.VISIBLE);
            } else {
                ma.startBtn.setText("点击开始");
                ma.startBtn.setEnabled(true);
                ma.showImg.setVisibility(View.GONE);
                Toast.makeText(ma, "地鼠打完了!", Toast.LENGTH_SHORT).show();
            }
            ma.txt.setText("打到了" + msg.arg1 + "只" + ",总共10只");
        }
    }
}