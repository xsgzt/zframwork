package com.ztyb.framework.appupdate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.ztyb.framework.R;

public class UIUtils {
    public static final int notifyID = 99;
    public static final String CHANNEL_ID = "111";

    public static Notification.Builder creatNotification(Context context, NotificationManager notificationManager) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0新增通知渠道
            CharSequence name = "QingFrame";
            int importance = NotificationManager.IMPORTANCE_LOW;   //优先级
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(false);            //闪灯开关
            mChannel.enableVibration(false);         //振动开关
            mChannel.setShowBadge(false);         //通知圆点开关
            notificationManager.createNotificationChannel(mChannel);
            builder = new Notification.Builder(context.getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.ic_launcher))
                .setContentText("0%")
                .setContentTitle("青结构")
                .setProgress(100, 0, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //小图标背景
            builder.setColor(context.getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }

        notificationManager.notify(notifyID, builder.build());
        return builder;
    }

    public static void cancleNotification(Context context, NotificationManager notificationManager) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationManager.cancel(notifyID);
    }
}
