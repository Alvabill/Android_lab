package com.example.a15945.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import static com.example.a15945.lab3.shoppingList.STATICACTION;

/**
 * Created by 15945 on 2017/10/28.
 */

public class StaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(STATICACTION)){
            Bundle bundle = intent.getExtras();
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), bundle.getInt("imgID"));

            Intent intent1 = new Intent(context,goods.class);
            intent1.putExtra("name",bundle.getString("name"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

            //Notification部分
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("新商品热卖")
                    .setContentText(bundle.getString("name")+"仅售"+bundle.getString("price"))
                    .setTicker("您有一条新消息")
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.full_star)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            //获取状态栏管理
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = builder.build();
            //绑定Notification，发送通知请求
            manager.notify(0,notification);
        }
    }
}

//            intent1.setAction(STATICACTION);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);