package com.example.dong.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import verificationCodeInput.VerificationCodeInput;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    Timer timer;
    TimerTask timerTask;
    int count;
    Handler handler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {
                //do something
                if (count != 0) {
                    textView.setText((count--) + "");
                }

            }
            return false;
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.test);

        VerificationCodeInput verificationCodeInput = findViewById(R.id.verificationCodeInput);
        verificationCodeInput.setBg(verificationCodeInput.getmEditTextList().get(0),true);
        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.afterTextChangedListener() {
            @Override
            public void onComplete(String content) {

            }
        });

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelTime();
            }
        });





    }

    private void cancelTime() {
        if (timerTask != null) {
            timerTask.cancel();
            timer.cancel();
        }
    }

    private void startTime() {
        timer = new Timer();
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        count = 30;
        timer.schedule(timerTask, 1000, 1000);//延时1s，每隔500毫秒执行一次run方法
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTime();
    }
}
