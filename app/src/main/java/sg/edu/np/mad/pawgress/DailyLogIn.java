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
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_in);
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.i(null, "Starting Daily LogIn Page");
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String newDayDate = formatter.format(new Date());


        String lastInDate = user.getLastLogInDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date1;
        Date date2;
        int currency = user.getCurrency();
        int streak = user.getStreak();
        System.out.println("Current Currency:" + currency + "\n" + "Current Streak: " + streak + "\n" + "logged in status: " + user.getLoggedInTdy());

        String tempStr = "No";

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

        if (lastInDate.equals(newDayDate)){
            System.out.println("Last log in date equal to todays date.");
            System.out.println("same day:" + user.getLoggedInTdy().equals(tempStr));
            System.out.println("same day status : " + user.getLoggedInTdy());

            // scenario where user first create their account and their first streak will pop up
            if (user.getLoggedInTdy().equals(tempStr)){
                // text will change to "streak:1, current rewarded:0[for now], let's start streaking and stay productive!"

                statusText.setText("Let's start streaking and stay productive!");
                streakText.setText("Streak: " + streak);
                rewardText.setText("Reward: " + (streak*1));
                myDBHandler.updateData(user.getUsername(), newDayDate, streak, streak*1, "Yes");

                closeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                        intent.putExtra("User", user);
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
                        intent.putExtra("User", user);
                        startActivity(intent);
                    }
                });
            }
        }
        else{
            System.out.println("Last log in date not equal to todays date.");
            user.setLoggedInTdy("No");
            System.out.println(user.getLoggedInTdy());
            if (user.getLoggedInTdy().equals(tempStr)){
                long diffInMilliseconds = date2.getTime() - date1.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

                System.out.println("Last log in date: " + date1);
                System.out.println("Today's date: " + date2);
                System.out.println("Difference in days: " + diffInDays);

                if (diffInDays != 1){
                    //they break streak
                    System.out.println("break streak");

                    myDBHandler.updateData(user.getUsername(), newDayDate, 1, currency+1, "Yes");

                    //set text to streak broken
                    statusText.setText("You broke your " + streak + " days streak! Let's keep streaking!");
                    streakText.setText("Streak: " + 1);
                    rewardText.setText("Reward: " + 1);
                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }
                    });
                }

                else{
                    //they streak
                    int newStreak = streak+1;
                    int newCurrency = currency + (newStreak*1);
                    System.out.println("Newstreak:"+newStreak);
                    myDBHandler.updateData(user.getUsername(), newDayDate, newStreak, newCurrency, "Yes");

                    //set text to streaking stuff idk
                    statusText.setText("You're streaking! Keep up the good work!");
                    streakText.setText("Streak: " + newStreak);
                    rewardText.setText("Reward: " + newStreak*1);
                    closeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent intent = new Intent(DailyLogIn.this,HomePage.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
}