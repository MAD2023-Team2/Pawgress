package sg.edu.np.mad.pawgress;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

/**
 * This class is a BroadcastReceiver that handles the generation and display of notifications.
 */
public class MyNotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "task_channel";
    private static final int NOTIFICATION_ID_MORNING = 1;
    private static final int NOTIFICATION_ID_NOON = 2;
    private static final int NOTIFICATION_ID_EVENING = 3;
    private Context context;
    public UserData user;

    /**
     * Default constructor for the MyNotificationReceiver class.
     */
    public MyNotificationReceiver() {
        // Default constructor
    }

    /**
     * Constructor for the MyNotificationReceiver class.
     * @param context The context in which the receiver is running.
     * @param user The user data containing the task list.
     */
    public MyNotificationReceiver(Context context, UserData user) {
        this.context = context;
        this.user = user;
    }

    /**
     * This method is called when the BroadcastReceiver receives a broadcast intent.
     * It generates unique notification IDs based on the current time and creates and shows notifications.
     * @param context The context in which the receiver is running.
     * @param intent The received intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Generate unique notification IDs using the current time
        int notificationIdMorning = NOTIFICATION_ID_MORNING + (int) (currentTimeMillis % 1000);
        int notificationIdNoon = NOTIFICATION_ID_NOON + (int) (currentTimeMillis % 1000);
        int notificationIdEvening = NOTIFICATION_ID_EVENING + (int) (currentTimeMillis % 1000);

        // Create and show the notifications using the unique IDs
        createNotification(context, notificationIdMorning, "Good morning!");
        createNotification(context, notificationIdNoon, "Good afternoon!");
        createNotification(context, notificationIdEvening, "Good evening!");
    }

    /**
     * Creates and displays a notification with the provided greeting message.
     * The notification message is based on the number of tasks left for the day.
     * @param context The context in which the notification is created.
     * @param notificationId The unique ID for the notification.
     * @param greeting The greeting message for the notification.
     */
    private void createNotification(Context context, int notificationId, String greeting) {
        // Cast the context to the MainActivity class to access its methods
        MainMainMain mainActivity = (MainMainMain) context;

        // Count the tasks left for the day using the user's task list
        int tasksLeft = mainActivity.countTasks(user.getTaskList());

        // Set the notification message based on the conditions
        String message;
        if (tasksLeft > 0) {
            message = tasksLeft + " task" + (tasksLeft > 1 ? "s" : "") + " left for the day";
        } else {
            message = "All tasks completed! Good progress today";
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.corgi)
                .setContentTitle(greeting)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an instance of the NotificationUtils class
        NotificationUtils notificationUtils = new NotificationUtils(context);

        // Create the notification channel
        notificationUtils.createNotificationChannel();

        // Show the notification
        notificationUtils.showNotification(notificationId, builder.build());
    }
}


