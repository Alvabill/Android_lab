package com.wwb.bill.musicbox;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private MusicService musicService = new MusicService();
    private ObjectAnimator objectAnimator = new ObjectAnimator();
    private ImageView musicCover;
    private TextView playStatu, playTimeNow, playTime ;
    private SeekBar seekBar;
    private Button playBtn, stopBtn, quitBtn;
    private IBinder mBinder;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private static boolean hasPermission = false;
    private static boolean isStopped = false;
    private static boolean isCreate = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this); //动态获取读取内置存储权限

        {//绑定Service
            Intent intent = new Intent(this, MusicService.class);
            startService(intent);//启动Service
            bindService(intent, sc, BIND_AUTO_CREATE);  //绑定指定的Service
        }

        ViewID();//获取控件ID
        AddListener();//添加监听事件
        initUI();//UI初始化
        Rotate();//图片旋转

        MyThread();//子线程，每休眠100毫米执行一次
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }

    @Override
    protected void onResume() {
      //  startPlaying();
        super.onResume();
    }

    private void ViewID(){
        isCreate = true;
        musicCover = (ImageView)findViewById(R.id.cover);
        playStatu = (TextView)findViewById(R.id.playStatus);
        playTimeNow = (TextView)findViewById(R.id.timeBegin);
        playTime = (TextView)findViewById(R.id.playtime);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        playBtn = (Button)findViewById(R.id.playBtn);
        stopBtn = (Button)findViewById(R.id.stopBtn);
        quitBtn = (Button)findViewById(R.id.quitBtn);
    }

    private void AddListener(){
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreate = false;
                isStopped = false;
                try{
                    int code = 101;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopped = true;
                isCreate = false;
                try{
                    int code = 102;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(sc);
                try {
                    int code = 103;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);

                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
        seekBar.setMax(musicService.mediaPlayer.getDuration());
        playTime.setText(time.format(musicService.mediaPlayer.getDuration()));
    }

    private void Rotate() {
        objectAnimator = ObjectAnimator.ofFloat(musicCover, "rotation", 0f, 360f);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(8000);
        objectAnimator.setRepeatCount(-1);
    }

    private void MyThread(){
        Thread mThread = new Thread(){
            @Override
            public void  run(){
                while(true){
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(sc != null && hasPermission==true){
                        mHandler.obtainMessage(123).sendToTarget();//发送message到关联线程的消息队列
                    }
                }
            }
        };
        mThread.start();
    }

    final Handler mHandler = new Handler(){
        //每个Hanlder都关联了一个线程，每个线程内部都维护了一个消息队列MessageQueue，Handler实际上也就关联了一个消息队列
        //可以通过Handler将Message和Runnable对象发送到该Handler所关联线程的MessageQueue（消息队列）中，
        //然后该消息队列一直在循环拿出一个Message，对其进行处理，处理完之后拿出下一个Message，继续进行处理，周而复始。
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            int SplayTimeNow = 0;
            int SplayTime = 0;
            int isPlay = 0;
            try{
                int code = 104;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                mBinder.transact(code, data, reply, 0);
                SplayTimeNow = reply.readInt();
                SplayTime = reply.readInt();
                isPlay = reply.readInt();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            switch (msg.what){
                case 123:
                    //UI更新
                    //初始Status
                    if(isCreate){
                        playStatu.setText("  ");
                    }
                    //点击stop按钮
                    else if(isStopped){
                        playStatu.setText("Stopped");
                        objectAnimator.end();
                    }
                    //音乐播放/暂停
                    else if(isPlay == 1){
                        playStatu.setText("Playing");
                        playBtn.setText("PAUSED");
                        if(objectAnimator.isRunning()){
                            objectAnimator.resume();
                        }else{
                            objectAnimator.start();
                        }
                    } else{
                        playStatu.setText("Paused");
                        playBtn.setText("PLAY");
                        objectAnimator.pause();
                    }

                    //获取当前进度条位置
                    seekBar.setProgress(SplayTimeNow);
                    //对进度条进行监听
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //更新播放时间，progress用于获取当前数值的大小，单位为毫秒
                            playTimeNow.setText(time.format(progress));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            try{
                                int code = 105;
                                Parcel data = Parcel.obtain();
                                Parcel reply = Parcel.obtain();
                                data.writeInt(seekBar.getProgress());
                                mBinder.transact(code, data, reply, 0);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    };


    private ServiceConnection sc = new ServiceConnection() {
        //当Activity和Service连接成功时回调该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d("service", "connected");
            mBinder = service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            sc = null;
        }
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 0;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE"};
    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            else {
                hasPermission = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //用户同意授权
            startPlaying();
            hasPermission = true;
        }else{
            //用户拒绝授权
            System.exit(0);
        }
        return;
    }

    private void startPlaying(){
        try{
            int code = 106;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            mBinder.transact(code, data, reply, 0);
            reply.recycle();
            data.recycle();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int SplayTimeNow = 0;
        int SplayTime = 0;
        int isPlay = 0;
        try{
            int code = 104;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            mBinder.transact(code, data, reply, 0);

            SplayTimeNow = reply.readInt();
            SplayTime = reply.readInt();
            isPlay = reply.readInt();
            reply.recycle();
            data.recycle();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        seekBar.setProgress(SplayTimeNow);
        seekBar.setMax(SplayTime);
        playTime.setText(time.format(SplayTime));
    }

}
