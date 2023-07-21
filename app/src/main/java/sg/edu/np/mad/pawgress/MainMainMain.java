package sg.edu.np.mad.pawgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import sg.edu.np.mad.pawgress.Fragments.Game.GameFragment;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.Fragments.Profile.ProfileFragment;
import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.databinding.ActivityMainMainMainBinding;

public class MainMainMain extends AppCompatActivity {
    ActivityMainMainMainBinding binding;
    UserData user;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
    private static final int MORNING_NOTIFICATION_ID = 1;
    private static final int NOON_NOTIFICATION_ID = 2;
    private static final int EVENING_NOTIFICATION_ID = 3;
    private boolean previousNotificationPreference;


    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting content view
        setContentView(R.layout.activity_main_main_main);
        // setTheme(R.style.Theme_Pawgress_LightBeigeMode);
        // Binding of navigation bar and fragments
        binding = ActivityMainMainMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load the current notification preference from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean userWantsNotifications = sharedPreferences.getBoolean("notification_preference", true);

        // Save the current notification preference to compare in onResume
        previousNotificationPreference = userWantsNotifications;

        // Update the notification preference and schedule/cancel notification accordingly
        updateNotificationPreference(userWantsNotifications);


        // Update the bottomNavigationView selection based of page of origin
        Intent receivingEnd = getIntent();
        String tab = receivingEnd.getExtras().getString("tab");
        user = receivingEnd.getParcelableExtra("User");
        Log.i("MainMainMain", "Tab: " + tab);
        if (tab.equals("tasks_tab")){
            replaceFragment(new TasksFragment());

            // Sets navigation bar item to tasks
            binding.bottomNavigationView.setSelectedItemId(R.id.tasks_tab);

        } else if (tab.equals("profile_tab")) {
            replaceFragment(new ProfileFragment());

            // Sets navigation bar item to profile
            binding.bottomNavigationView.setSelectedItemId(R.id.profile_tab);

        } else{
            replaceFragment(new HomeFragment());
        }

        // Switching of navigation bar tabs
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.home_tab){
                //Replace fragment with HomeFragment
                replaceFragment(new HomeFragment());
            }
            if (itemID == R.id.game_tab){
                //Replace fragment with GameFragment
                replaceFragment(new GameFragment());
            }
            if (itemID == R.id.tasks_tab){
                //Replace fragment with TaskFragment
                replaceFragment(new TasksFragment());
            }
            if (itemID == R.id.profile_tab){
                //Replace fragment with ProfileFragment
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    // Add this method to handle the notification preference change from NotificationSelection activity
    @Override
    protected void onResume() {
        super.onResume();

        // Load the current notification preference from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean userWantsNotifications = sharedPreferences.getBoolean("notification_preference", true);

        // Compare with the previous notification preference to check if it changed
        if (userWantsNotifications != previousNotificationPreference) {
            // Update the notification preference and schedule/cancel notification accordingly
            updateNotificationPreference(userWantsNotifications);

            // Update the previous notification preference for the next comparison
            previousNotificationPreference = userWantsNotifications;
        }
    }


    // Replaces current fragment with new fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");
        UserData fbUser = myDBHandler.findUser(user.getUsername());
        fbUser.setTaskList(myDBHandler.findTaskList(user));
        fbUser.setFriendList(myDBHandler.findFriendList(user));
        fbUser.setFriendReqList(myDBHandler.findFriendReqList(user));

        // Set friends and friend request list based on Firebase, not SQLite
        Query query = myRef.orderByChild("username").equalTo(user.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserData tempUser = snapshot.getValue(UserData.class);

                        // Clear existing friend and friend request data in local SQLite database
                        myDBHandler.removeAllFriends(user);
                        myDBHandler.removeAllFriendRequests(user);

                        // Add new friends data to local SQLite database
                        for (FriendData friend : tempUser.getFriendList()) {
                            myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                        }
                        for (FriendRequest req : tempUser.getFriendReqList()) {
                            myDBHandler.addFriendReq(req.getFriendReqName(), user, req.getReqStatus());
                        }
                        fbUser.setFriendList(tempUser.getFriendList());
                        fbUser.setFriendReqList(tempUser.getFriendReqList());
                        for (FriendData friend: fbUser.getFriendList()){
                            Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                        }
                    }
                }
                myRef.child(user.getUsername()).setValue(fbUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }

    private void scheduleMorningNotification(Notification notification) {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, MORNING_NOTIFICATION_ID);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        // Generate a unique request code for the morning notification
        int uniqueRequestCode = 1001;

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                uniqueRequestCode,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Calculate the time for 7am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);


        // If the time has already passed today, schedule it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Get the AlarmManager and schedule the notification at 7am
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );

        Log.i("MainMainMain", "Scheduled Morning Notification time: " + calendar.getTime());
    }

    private void cancelMorningNotification() {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID,  MORNING_NOTIFICATION_ID);

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1001,
                notificationIntent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        // If a matching PendingIntent exists, cancel the notification
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void scheduleNoonNotification(Notification notification) {
        Log.i("MainMainMain", "Received Notification: " + notification);
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, NOON_NOTIFICATION_ID);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Add log to inspect the notificationIntent extras
        Bundle extras = notificationIntent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Log.i("MainMainMain", "Extra: " + key + ", Value: " + extras.get(key));
            }
        }

        // Calculate the time for noon
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 6);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, schedule it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Log.i("MainMainMain", "Scheduled Noon Notification time: " + calendar.getTime());

        // Get the AlarmManager and schedule the notification at noon
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );

        Log.i("MainMainMain", "Scheduled Noon Notification time: " + calendar.getTime());
    }



    private void cancelNoonNotification() {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, NOON_NOTIFICATION_ID);

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        // If a matching PendingIntent exists, cancel the notification
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void scheduleEveningNotification(Notification notification) {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, EVENING_NOTIFICATION_ID);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        // Generate a unique request code for the evening notification
        int uniqueRequestCode = 1003;

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                uniqueRequestCode,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Calculate the time for 6pm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 7);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, schedule it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Get the AlarmManager and schedule the notification at 6pm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );

        Log.i("MainMainMain", "Scheduled Evening Notification time: " + calendar.getTime());
    }

    private void cancelEveningNotification() {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, EVENING_NOTIFICATION_ID);

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1003,
                notificationIntent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        // If a matching PendingIntent exists, cancel the notification
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void updateNotificationPreference(boolean userWantsNotifications) {
        int tasksDueCount = myDBHandler.getNumberOfTasksDueAtTime();

        if (userWantsNotifications) {
            // Schedule the morning notification
            scheduleMorningNotification(getNotification("Good morning! You have " + tasksDueCount + " tasks due today. You got this!", "morning"));

            // Schedule the noon notification
            scheduleNoonNotification(getNotification("Good afternoon! You have " + tasksDueCount + " tasks left due today. Stay on track!", "noon"));

            // Schedule the evening notification
            scheduleEveningNotification(getNotification("Good evening! You have " + tasksDueCount + " tasks left to do. Finish strong!", "evening"));

            Log.i("MainMainMain", "Notifications Scheduled and Created!");
        } else {
            // Cancel all notifications if the user prefers not to receive notifications
            cancelMorningNotification();
            cancelNoonNotification();
            cancelEveningNotification();

            Log.i("MainMainMain", "Notifications Canceled!");
        }
    }

    private Notification getNotification(String content, String notificationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);

        // Set the notification title based on the notification type
        String notificationTitle;
        switch (notificationType) {
            case "morning":
                notificationTitle = "Morning Notification";
                break;
            case "noon":
                notificationTitle = "Noon Notification";
                break;
            case "evening":
                notificationTitle = "Evening Notification";
                break;
            default:
                notificationTitle = "Default Notification";
                break;
        }

        builder.setContentTitle(notificationTitle);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        // Log to check if the notification object is created successfully
        Notification notification = builder.build();
        Log.i("MainMainMain", "Notification: " + notification);

        return builder.build(); // Return the built notification, not the builder itself
    }

    // Create separate methods for getting morning, noon, and evening notifications
    private Notification getMorningNotification(String content) {
        return getNotification(content, "morning");
    }

    private Notification getNoonNotification(String content) {
        return getNotification(content, "noon");
    }

    private Notification getEveningNotification(String content) {
        return getNotification(content, "evening");
    }

}
