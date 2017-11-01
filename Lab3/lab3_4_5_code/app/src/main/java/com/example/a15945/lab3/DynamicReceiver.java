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

import static com.example.a15945.lab3.shoppingList.DYNAMICACTION;

/**
 * Created by 15945 on 2017/10/28.
 */

public class DynamicReceiver extends BroadcastReceiver {
    public static int i  = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DYNAMICACTION)){
            Bundle bundle = intent.getExtras();
            Bitmap bm  = BitmapFactory.decodeResource(context.getResources(),
                    bundle.getInt("imgID"));
            //Notification部分
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")
                    .setContentText(bundle.getString("name")+"已添加到购物车")
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.full_star)
                    .setAutoCancel(true)
                    //通知栏点击跳转
                    .setFullScreenIntent(PendingIntent.getActivity(context,0,new Intent(context,shoppingcartList.class),0),true);
            //获取状态栏管理
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = builder.build();
            //绑定Notification，发送通知请求
            manager.notify(i++,notification);

        }
    }
}
