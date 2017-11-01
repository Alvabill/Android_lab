package com.example.a15945.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class shoppingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //RemoteView 架构允许用户程序更新主屏幕的View，点击Widget 激活点击事件， Android 会将其 转发给用户程序，由AppWidgetProviders 类处理，使得用户程序可更新主 屏幕Widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shopping_widget);
        //添加点击事件，点击widget后返回主界面
        Intent intent = new Intent(context,shoppingList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置事件监听，点击
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        //更新Appwidget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.shopping_widget);
        Bundle bundle = intent.getExtras();

        if(intent.getAction().equals("com.example.a15945.lab3.dynamicReceiver")){
            rv.setTextViewText(R.id.widgetText,bundle.getString("name")+"已添加到购物车");
            rv.setImageViewResource(R.id.widgetImg,bundle.getInt("imgID"));

            Intent intent2 = new Intent(context,shoppingcartList.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent2, 0);
            rv.setOnClickPendingIntent(R.id.widget,pi);

        }

         else if(intent.getAction().equals("com.example.a15945.lab3.staticReceiver")){
            rv.setTextViewText(R.id.widgetText,bundle.getString("name")+"仅售"+bundle.getString("price"));
            rv.setImageViewResource(R.id.widgetImg,bundle.getInt("imgID"));

            Intent intent1 = new Intent(context,goods.class);
            intent1.putExtra("name",bundle.getString("name"));
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, 0);
            rv.setOnClickPendingIntent(R.id.widget,pi);

        }

        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context.getApplicationContext(),
                shoppingWidget.class), rv);

    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

