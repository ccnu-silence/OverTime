package wistcat.overtime.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import sample.myresource.util.PlatformVersion;
import wistcat.overtime.R;
import wistcat.overtime.main.runningpage.RunningTasksActivity;

/**
 * @author wistcat 2016/9/22
 */
public class NotificationHelper {

    private NotificationHelper(){}

    public static void notifyNormal(Context context, int count) {
        final int TASK_NORMAL = 1;
        NotificationManager manager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (count == 0) {
            manager.cancel(TASK_NORMAL);
            return;
        }
        // +需要一个Intent，指向需要打开的Activity
        Intent intent = new Intent(context, RunningTasksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // +需要一个PendingIntent，存放Intent，并设置FLAG
        PendingIntent pending = PendingIntent.getActivity(context, RunningTasksActivity.REQUEST_NOTIFICATION,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // +需要一个NotificationCompat.Builder，用来创建Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.circle_2)
                .setContentTitle("OverTime")
                .setContentText("我的任务")
                .setNumber(count)
                .setTicker("添加任务")
                .setContentIntent(pending)
                .setOngoing(true);
        if (PlatformVersion.isJellyBean()) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        manager.notify(TASK_NORMAL, builder.build());
    }



}
