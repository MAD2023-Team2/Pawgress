package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DailyLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(null, "Starting Daily LogIn Page");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        String lastInDate = user.getLastLoginDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1;
        Date date2;
        int currency = user.getCurrency();
        int streak = user.getStreak();
        try{
            date1 = dateFormat.parse(lastInDate);
            date2 = dateFormat.parse(newDayDate);
        } catch(ParseException e){
            e.printStackTrace();
            return;
        }
        TextView statusText = findViewById(R.id.logInStatus);
        TextView streakText = findViewById(R.id.streakText);
        TextView rewardText = findViewById(R.id.rewardText);
        Button closeButton = findViewById(R.id.closeDaily);
        if (lastinDate.equals(newDayDate)){
            System.out.println("Last log in date equal to todays date.");

            // scenario where user first create their account and their first streak will pop up
            if (user.getLoggedInTdy() == false){
                // text will change to "streak:1, current rewarded:0[for now], let's start streaking and stay productive!"

                statusText.setText("Let's start streaking and stay productive!");
                streakText.setText("Streak: " + streak);
                rewardText.setText("Reward: " + streak*0);
                user.setLoggedInTdy(true);
                closeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                        startActivity(intent);
                    }
                });
            }
            // scenario where user logs in on the same day but alrdy claimed their reward
            else{
                // text will change to "you've logged in today, let's get productive!"
                statusText.setText("You've logged in today, keep up with the Pawgress!");
                streakText.setText(" ");
                rewardText.setText(" ");
                closeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                        startActivity(intent);
                    }
                });
            }
        }
        else{
            System.out.println("Last log in date not equal to todays date.");
            user.setLoggedInTdy(false);

            if (user.getLoggedInTdy() == false){
                long diffInMilliseconds = date2.getTime() - date1.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

                System.out.println("Last log in date: " + date1);
                System.out.println("Today's date: " + date2);
                System.out.println("Difference in days: " + diffInDays);

                if (diffInDays != 1){
                    //they break streak
                    user.setLoggedInTdy(true);
                    user.setStreak(1);
                    user.setCurrency(currency + 1*0);
                    //set text to streak broken
                    statusText.setText("You broke your " + streak + " days streak! Let's keep streaking!");
                    streakText.setText("Streak: " + 1);
                    rewardText.setText("Reward: " + 0 );
                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                            startActivity(intent);
                        }
                    });
                }

                else{
                    //they streak
                    user.setStreak(user.getStreak() + 1);
                    user.setCurrency(currency + 1*0);
                    user.setLoggedInTdy(true);
                    //set text to streaking stuff idk
                    statusText.setText("You're streaking! Keep up the good work!");
                    streakText.setText("Streak: " + user.getStreak());
                    rewardText.setText("Reward: " + user.getStreak() * 0);
                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
}