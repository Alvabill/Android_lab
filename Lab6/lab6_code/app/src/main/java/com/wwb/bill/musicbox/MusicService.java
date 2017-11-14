package com.wwb.bill.musicbox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by 15945 on 2017/11/13.
 */

public class MusicService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public MusicService() {
        try{
            //模拟器测试时歌曲路径mediaPlayer.setDataSource("/data/elt.mp3");
            //手机测试路径
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/melt.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //播放按钮，服务处理函数
    public void playAndpause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }
    //停止按钮，服务处理函数
    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            try{
                mediaPlayer.prepare();
                //按钮事件被触发后，进度条回到最开始的位置
                mediaPlayer.seekTo(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //退出按钮，服务处理函数
    public void quit(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    //拖动进度条，服务处理函数
    public void TrackingTouch( int position) {
        mediaPlayer.seekTo(position);
    }

    //定义onBind方法所返回对象1
    public final IBinder binder = new MyBinder();
    //继承Binder实现IBinder类
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            switch (code){
                case 101:
                    //播放按钮，服务处理函数
                    playAndpause();
                    break;
                case 102:
                    //停止按钮，服务处理函数
                    stop();
                    break;
                case 103:
                    //退出按钮，服务处理函数
                    quit();
                    break;
                case 104:
                    //界面刷新。服务返回数据函数
                    int playTimeNow = mediaPlayer.getCurrentPosition();
                    int playTime = mediaPlayer.getDuration();
                    int isPlay = 0;
                    if(mediaPlayer.isPlaying())isPlay = 1;
                    reply.writeInt(playTimeNow);
                    reply.writeInt(playTime);
                    reply.writeInt(isPlay);
                    return true;
                case 105:
                    //拖动进度条，服务处理函数
                    TrackingTouch(data.readInt());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

}
