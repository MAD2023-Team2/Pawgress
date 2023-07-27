package sg.edu.np.mad.pawgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import sg.edu.np.mad.pawgress.Fragments.Game_Shop.Product;
import sg.edu.np.mad.pawgress.Tasks.Task;

public class DailyLogIn extends AppCompatActivity {
    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
    UserData user;
    String newDayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_daily_log_in);

        // Getting user info via intent
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        myDBHandler.clearDatabase("IMAGE_URL");
        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        DatabaseReference database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ShopItems");

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);

                    // Get a reference to the image file in Firebase Storage
                    String imageName = product.getName();
                    String imageURL = imageName + ".png";
                    StorageReference imageRef = storage.getReference("InventoryImages/" + imageURL );

                    // Get the path for local storage
                    File localFile = null;
                    try {
                        localFile = File.createTempFile(imageName, ".png");
                    } catch (IOException e) {
                        // do nth
                    }

                    // Download the image to local storage
                    File finalLocalFile = localFile;
                    imageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                        // Image download success
                        myDBHandler.addImageURL(imageName, finalLocalFile.getAbsolutePath());
                        Log.e("DailyLogIn", "Downloaded image: " + imageName);
                    }).addOnFailureListener(exception -> {
                        // Handle any errors that occurred during the download
                        Log.e("DailyLogIn", "Error downloading image: " + imageName);
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nth
            }
        });
    }

    public void createDeleteChallenge(){
        ArrayList<Task> taskList = myDBHandler.findTaskList(user);
        for (Task task : taskList){
            try {
                if(task.getStatus().equals("In Progress") && (!task.getDueDate().equals(newDayDate)) && task.getDailyChallenge() == 1){
                    task.setStatus("Missed");
                    myDBHandler.updateTask(task, user.getUsername());
                    Log.v("Daily Log In","New Day, previous daily challenge task set to missed");
                }
            }
            catch (Exception e) {
                // task has no due date, therefore it is not a daily challenge
            }
        }

        Random random = new Random();
        int challengeInt = random.nextInt(6) + 1;
        String name;
        switch (challengeInt) {
            case 1:
                name = "Drink Water";
                break;
            case 2:
                name = "Read (books/news, etc) for 10 mins";
                break;
            case 3:
                name = "Stretch for 5 minutes";
                break;
            case 4:
                name = "Take a 10 minutes walk";
                break;
            case 5:
                name = "Meditate for 15 minutes";
                break;
            default:
                name = "Take a 5 minute break";
                break;
        }

        Task task = new Task(1, name, "In Progress", "Daily Challenge" ,0, 60, newDayDate,newDayDate,null,null,1, 0, null);
        myDBHandler.addTask(task, user);
        Log.w("Daily Log In", "Created Daily Challenge: " + task.getTaskName());
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.i(null, "Starting Daily LogIn Page");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        newDayDate = formatter.format(new Date());


        String lastInDate = user.getLastLogInDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date1;
        Date date2;
        int currency = user.getCurrency();
        int streak = user.getStreak();
        Log.w("Daily Log In", "Current Currency:" + currency + "\n" + "Current Streak: " + streak + "\n" + "logged in status: " + user.getLoggedInTdy());

        String tempStr = "No";

        try {
            date1 = dateFormat.parse(lastInDate);
            date2 = dateFormat.parse(newDayDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        TextView statusText = findViewById(R.id.logInStatus);
        TextView streakText = findViewById(R.id.streakText);
        TextView rewardText = findViewById(R.id.rewardText);
        Button closeButton = findViewById(R.id.closeDaily);

        if (lastInDate.equals(newDayDate)) {
            Log.w("Daily Log In", "Last log in date equal to today's date.");

            // scenario where user first create their account and their first streak will pop up
            if (user.getLoggedInTdy().equals(tempStr)){

                createDeleteChallenge();

                // text will change to "streak:1, current rewarded:0[for now], let's start streaking and stay productive!"
                statusText.setText("Let's start streaking and stay productive!");
                streakText.setText("Streak: " + streak);
                rewardText.setText("Reward: 1");
                myDBHandler.updateData(user.getUsername(), newDayDate, streak, currency, "Yes");
                user.setStreak(streak);
                user.setCurrency(currency);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DailyLogIn.this, MainMainMain.class);
                        user.setLastLogInDate(newDayDate);
                        intent.putExtra("User", user);
                        intent.putExtra("tab", "home_tab");
                        startActivity(intent);
                        finish();
                    }
                });
            }
            // scenario where user logs in on the same day but alrdy claimed their reward
            else{
                // text will change to "you've logged in today, let's get productive!"
                statusText.setText("You've logged in today, keep up with the Pawgress!");
                streakText.setText(" ");
                rewardText.setText(" ");
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DailyLogIn.this, MainMainMain.class);
                        user.setLastLogInDate(newDayDate);
                        intent.putExtra("User", user);
                        intent.putExtra("tab", "home_tab");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
        else{
            Log.w("Daily Log In","Last log in date not equal to todays date.");
            user.setLoggedInTdy("No");

            createDeleteChallenge();

            if (user.getLoggedInTdy().equals(tempStr)){
                long diffInMilliseconds = date2.getTime() - date1.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

                Log.w("Daily Log In","Difference in Log in days: " + diffInDays);

                if (diffInDays != 1){
                    //they break streak
                    Log.w("Daily Log In","Broke Streak");

                    int newCurrency = currency + 1;
                    myDBHandler.updateData(user.getUsername(), newDayDate, 1, newCurrency, "Yes");
                    user.setStreak(1);
                    user.setCurrency(newCurrency);
                    //set text to streak broken
                    statusText.setText("You broke your " + streak + " days streak! Let's keep streaking!");
                    streakText.setText("Streak: " + 1);
                    rewardText.setText("Reward: 1");
                    Log.w("Daily Log In","Current Streak: " + user.getStreak());
                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(DailyLogIn.this,MainMainMain.class);
                            user.setLastLogInDate(newDayDate);
                            intent.putExtra("User", user);
                            intent.putExtra("tab", "home_tab");
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                else{
                    //they streak
                    int newStreak = streak+1;

                    // for every 1 week, they gain an extra currency
                    int extra_reward = newStreak / 7;
                    int reward = extra_reward + 1;
                    int newCurrency = currency + reward;
                    Log.w("Daily Log In","Newstreak: "+newStreak);
                    Log.w("Daily Log In","New Currency: "+newCurrency);
                    myDBHandler.updateData(user.getUsername(), newDayDate, newStreak, newCurrency, "Yes");
                    user.setStreak(newStreak);
                    user.setCurrency(newCurrency);
                    //set text to streaking stuff idk
                    statusText.setText("You're streaking! Keep up the good work!");
                    streakText.setText("Streak: " + newStreak);
                    rewardText.setText("Reward: "+ reward);

                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Log.w("Daily Login","not same day");
                            Intent intent = new Intent(DailyLogIn.this,MainMainMain.class);
                            user.setLastLogInDate(newDayDate);
                            intent.putExtra("User", user);
                            intent.putExtra("tab", "home_tab");
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        }
    }
}
