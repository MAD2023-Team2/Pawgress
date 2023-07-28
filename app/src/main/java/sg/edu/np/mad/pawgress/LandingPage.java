package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class LandingPage extends AppCompatActivity {
    private TextSwitcher textView;
    private String oriText = "Pawgress";
    private int charIndex = 0;
    private Handler handler = new Handler();
    // 3 seconds delay
    private static final int SPLASH_DELAY_MILLIS = 3000;
    String quoteText;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setTheme(R.style.Theme_Pawgress_FullScreenMode);
        setContentView(R.layout.activity_landing_page);

        textView = findViewById(R.id.textView9);

        textView.setInAnimation(null);
        textView.setOutAnimation(null);

        textView.setFactory(new ViewSwitcher.ViewFactory() {

            // text configurations
            @Override
            public View makeView() {
                TextView textView = new TextView(LandingPage.this);
                textView.setTextSize(36);
                textView.setTextColor(Color.parseColor("#FACDB0"));
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL));
                return textView;
            }
        });

        // Getting motivational message from api
        fetchRandomQuote();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(null, "Landing Page 3 seconds");
        animateText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectToMain();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void animateText() {
        // clear any existing text
        textView.setText("");
        // reset char index
        charIndex = 0;
        // delay between each char
        handler.postDelayed(characterAnimator, 100);
    }

    private Runnable characterAnimator = new Runnable() {
        @Override
        public void run() {
            if (charIndex <= oriText.length()) {
                String newText = oriText.substring(0, charIndex);
                textView.setText(newText);
                charIndex++;
                // delay between char
                handler.postDelayed(this, 100);
            }
        }
    };

    private void redirectToMain() {
        Intent intent = new Intent(LandingPage.this, LoginPage.class);
        startActivity(intent);
        finish();
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
                Toast.makeText(LandingPage.this, "No internet access. Unable to load motivational message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onQuoteFetched(String quoteText, String author) {
        this.quoteText = quoteText;
        this.author = author;

        // Save quote and author to shared preferences
        SaveSharedPreference.setQuote(LandingPage.this, quoteText);
        SaveSharedPreference.setAuthor(LandingPage.this, author);
    }
}