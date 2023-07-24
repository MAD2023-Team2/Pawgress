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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import sg.edu.np.mad.pawgress.Fragments.Game_Shop.GameFragment;
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

        Intent receivingEnd = getIntent();
        String tab = receivingEnd.getExtras().getString("tab");
        user = receivingEnd.getParcelableExtra("User");

        // Load the current notification preference from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean userWantsNotifications = sharedPreferences.getBoolean("notification_preference", true);

        // Save the current notification preference to compare in onResume
        previousNotificationPreference = userWantsNotifications;

        // Update the notification preference and schedule/cancel notification accordingly
        updateNotificationPreference(userWantsNotifications);


        // Update the bottomNavigationView selection based of page of origin
        Log.w(null, "User is null : " + String.valueOf(user==null));
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
        try{
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
        catch (Exception e){
            // do nth ( account has been deleted, therefore no list )
        }
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
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 44);
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
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 46);
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
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 47);
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

    private void scheduleStudyAdviceNotification(Notification notification) {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, 4); // Use a different ID for the Study Advice Notification
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1004,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Calculate the time for 7:02 AM
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, schedule it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Get the AlarmManager and schedule the notification at 7:02 AM
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );

        Log.i("MainMainMain", "Scheduled Study Advice Notification time: " + calendar.getTime());
    }

    private void cancelStudyAdviceNotification() {
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, 4); // Use the same ID used in the scheduleStudyAdviceNotification method

        // Use the unique request code in the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1004,
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

        if (userWantsNotifications) {
            int countTaskNotifs = myDBHandler.countTasks(myDBHandler.findTaskList(user));
            Random random = new Random();

            // Get a random morning notification message
            List<String> morningNotifList = new ArrayList<>();
            morningNotifList.add(countTaskNotifs + "Good morning! Rise and shine, it's a brand new day full of possibilities. Make it count!");
            morningNotifList.add(countTaskNotifs + "Rise and grind! A productive day starts with a positive mindset. You've got this!");
            morningNotifList.add(countTaskNotifs + "It's a new day, a new beginning. Start it with enthusiasm and accomplish great things!");

            String selectedMorningNotif = morningNotifList.get(random.nextInt(morningNotifList.size()));

            // Schedule the morning notification
            scheduleMorningNotification(getNotification(selectedMorningNotif, "morning"));

            // Get a random noon notification message
            List<String> noonNotifList = new ArrayList<>();
            noonNotifList.add(countTaskNotifs + "Halfway there! You're doing great. Take a deep breath, stretch, and keep up the good work!");
            noonNotifList.add(countTaskNotifs + "Keep pushing forward! The afternoon is a perfect time to tackle challenging tasks. Stay focused!");
            noonNotifList.add(countTaskNotifs + "Remember to recharge as you conquer the day! You're doing fantastic!");

            String selectedNoonNotif = morningNotifList.get(random.nextInt(noonNotifList.size()));

            // Schedule the noon notification
            scheduleNoonNotification(getNotification(selectedNoonNotif, "noon"));

            // Get a random evening notification message
            List<String> eveningNotifList = new ArrayList<>();
            eveningNotifList.add(countTaskNotifs + "Great job today! Take a moment to pat yourself on the back for all the progress you've made.");
            eveningNotifList.add(countTaskNotifs + "Finish strong! There's still time to wrap up any pending tasks. You're almost there!");
            eveningNotifList.add(countTaskNotifs + "Take pride in the progress you've made today! Keep going and finish strong. Tomorrow's success begins with today's efforts.");

            String selectedEveningNotif = eveningNotifList.get(random.nextInt(eveningNotifList.size()));

            // Schedule the evening notification
            scheduleEveningNotification(getNotification(selectedEveningNotif, "evening"));

            // Get a random piece of study advice
            List<String> studyAdviceList = new ArrayList<>();
            studyAdviceList.add("Set Clear Goals: Define what you want to achieve each day and break tasks into smaller, manageable chunks. It will make your progress feel more tangible and achievable.");
            studyAdviceList.add("Time Management: Prioritize tasks based on their importance and deadline. Allocate specific time blocks for studying and stick to your schedule.");
            studyAdviceList.add("Stay Organized: Keep your study space clutter-free and organize your materials for easy access. A tidy environment can help you stay focused.");
            studyAdviceList.add("Take Breaks: Remember to take short breaks during study sessions. Stretch, walk around, or do something enjoyable to refresh your mind.");
            studyAdviceList.add("Stay Positive: Maintain a positive mindset. Believe in your abilities and stay motivated even during challenging times.");
            studyAdviceList.add("Practice Active Learning: Engage with the material actively by summarizing, questioning, and discussing concepts. It enhances retention and understanding.");
            studyAdviceList.add("Seek Help: Don't hesitate to ask for help if you encounter difficulties. Reach out to teachers, peers, or online resources for support.");
            studyAdviceList.add("Reward Yourself: Celebrate your achievements, no matter how small. Treat yourself to something you enjoy after completing a significant task.");
            studyAdviceList.add("Stay Consistent: Consistency is key to success. Make studying a regular habit, and over time, it will yield great results.");
            studyAdviceList.add("Take Care of Yourself: Remember to get enough rest, eat well, and exercise. A healthy body and mind contribute to better learning and productivity.");

            String selectedStudyAdvice = studyAdviceList.get(random.nextInt(studyAdviceList.size()));

            // Schedule the Study Advice Notification with the selected study advice
            scheduleStudyAdviceNotification(getStudyAdviceNotification(selectedStudyAdvice));

            Log.i("MainMainMain", "Notifications Scheduled and Created!");
        } else {
            // Cancel all notifications if the user prefers not to receive notifications
            cancelMorningNotification();
            cancelNoonNotification();
            cancelEveningNotification();

            // Cancel the Study Advice Notification
            cancelStudyAdviceNotification();

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
            case "study":
                notificationTitle = "Study Advice Notification";
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

    private Notification getStudyAdviceNotification(String content) {
        return getNotification(content, "study");
    }

}
