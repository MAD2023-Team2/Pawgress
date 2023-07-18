package sg.edu.np.mad.pawgress;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * This class provides utility methods for managing and showing notifications.
 */
public class NotificationUtils {
    private static final String CHANNEL_ID = "task_channel";
    private static final String CHANNEL_NAME = "Task Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for task reminders";

    private Context context;
    private NotificationManager notificationManager;

    /**
     * Constructor for the NotificationUtils class.
     * @param context The context in which the notifications are managed.
     */
    public NotificationUtils(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Creates a notification channel for task notifications.
     * This is required for Android versions Oreo (API level 26) and above.
     */
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Shows a notification with the provided notification ID and notification object.
     * @param notificationId The unique ID for the notification.
     * @param notification The notification to be shown.
     */
    public void showNotification(int notificationId, Notification notification) {
        notificationManager.notify(notificationId, notification);
    }
}
