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
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import sg.edu.np.mad.pawgress.Tasks.Task;

public class DailyLogIn extends AppCompatActivity {
    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
    UserData user;
    Boolean new_day = false;
    String quoteText;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log_in);
    }

    public void createChallenge(){
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        Task task = new Task(1, name, "In Progress", "Daily Challenge" ,0, 60, newDayDate,newDayDate,null,null,1, 0);
        myDBHandler.addTask(task, user);
        Log.w("Daily Log In", "Created Daily Challenge: " + task.getTaskName());
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.i(null, "Starting Daily LogIn Page");

        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        fetchRandomQuote();
        myDBHandler.updateQuoteAndAuthor(quoteText, author, user);

        try {
            new_day = receivingEnd.getExtras().getBoolean("new_day");
        } catch (Exception exception) {
            Log.w("DailyLogIn", "not from inside app");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String newDayDate = formatter.format(new Date());


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

                createChallenge();

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

            createChallenge();

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
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                            String newDayDate = formatter.format(new Date());
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

    public interface ProductivityQuoteApi {
        @GET("random") // Replace with the actual API endpoint
        Call<List<InspirationalQuote>> getRandomQuote();
    }

    private void fetchRandomQuote() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zenquotes.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductivityQuoteApi apiService = retrofit.create(ProductivityQuoteApi.class);

        Call<List<InspirationalQuote>> call = apiService.getRandomQuote();
        call.enqueue(new Callback<List<InspirationalQuote>>() {
            @Override
            public void onResponse(Call<List<InspirationalQuote>> call, Response<List<InspirationalQuote>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    InspirationalQuote quote = response.body().get(0);
                    quoteText = quote.getQuote();
                    author = quote.getAuthor();

                    onQuoteFetched(quoteText, author);
                } else {
                    // Handle API error or empty response
                }
            }

            @Override
            public void onFailure(Call<List<InspirationalQuote>> call, Throwable t) {
                // Handle network error
            }
        });
    }

    public void onQuoteFetched(String quoteText, String author) {
        this.quoteText = quoteText;
        this.author = author;
        myDBHandler.updateQuoteAndAuthor(quoteText, author, user);
    }
}
