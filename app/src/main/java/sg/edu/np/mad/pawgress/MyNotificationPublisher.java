package sg.edu.np.mad.pawgress;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATIONID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyNotificationPublisher", "Received Intent: " + intent);

        // Check if the intent contains the notification object
        if (intent.hasExtra(NOTIFICATION)) {
            // If the intent has the notification extra, process the notification
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            if (notification != null) {
                Log.i("MyNotificationPublisher", "Received Notification: " + notification.toString());

                // Create the notification channel for Android Oreo and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel(MainMainMain.NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(channel);
                    }
                }

                int id = intent.getIntExtra(NOTIFICATIONID, 0);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(id, notification);
                }
            } else {
                Log.e("MyNotificationPublisher", "Notification object which is supposed to have the data is null.");
            }
        } else {
            Log.e("MyNotificationPublisher", "Notification object is missing in the intent but its fine if its the first one.");
        }
    }
}

