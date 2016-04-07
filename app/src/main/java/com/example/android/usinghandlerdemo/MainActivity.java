package com.example.android.usinghandlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    ProgressBar bar;
    //khai báo handler class để xử lý đa tiến trình
    Handler handler;
    //dùng AtomicBoolean để thay thế cho boolean
    AtomicBoolean isrunning=new AtomicBoolean(false);
    //boolean
    Button btnstart;
    TextView lblmsg;
    int dem=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar=(ProgressBar) findViewById(R.id.progressBar1);
        btnstart=(Button) findViewById(R.id.btnstart);
        lblmsg=(TextView) findViewById(R.id.textView1);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem++;
                Log.i("so tien tring",String.valueOf(dem));
                dostart();
            }
        });
        //viết lệnh cho handler class để nhận thông điệp
        //gửi về từ tiến trình con
        //mọi thông điệp sẽ được xử lý trong handleMessage
        //từ tiến trình con ta gửi Message về cho main thread
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //msg.arg1 là giá trị được trả về trong message
                //của tiến trình con
                bar.setProgress(msg.arg1);
                lblmsg.setText(msg.arg1+"%");
            }
        };

    }
    public  void dostart(){
        bar.setProgress(0);
        isrunning.set(false);
        Thread th= new Thread(new Runnable() {
            @Override
            public void run() {
                //vòng lặp chạy 100 lần
                for(int i=1;i<=100 && isrunning.get();i++)
                {
                    Log.i("is running", String.valueOf(isrunning.get()));
                    //cho tiến trình tạm ngừng 100 mili second
                    SystemClock.sleep(100);
                    //lấy message từ Main thread
                    Message msg=handler.obtainMessage();
                    //gán giá trị vào cho arg1 để gửi về Main thread
                    msg.arg1=i;
                    //gửi lại Message này về cho Main Thread
                    handler.sendMessage(msg);
                }
            }
        });
        isrunning.set(true);
        //kích hoạt tiến trình
        th.start();

    }

}
