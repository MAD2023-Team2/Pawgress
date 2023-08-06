package sg.edu.np.mad.pawgress.tutorials;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.viewpager.widget.ViewPager;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.Config;
import sg.edu.np.mad.pawgress.CompanionSelectionActivity;
import sg.edu.np.mad.pawgress.CreateAccount;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class Tutorial_Page extends AppCompatActivity {
    // Declare Variables
    ViewPager mSLideViewPager;
    CircleIndicator indicator;
    Button backbtn, nextbtn, skipbtn;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout and make app fullscreen
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.tutorial_page);

        // Initialize TextViews and buttons
        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);

        // Initialize circle indicator
        indicator = findViewById(R.id.indicator);
        int indicatorWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()) + 0.5f);
        int indicatorHeight = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics()) + 0.5f);
        int indicatorMargin = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                getResources().getDisplayMetrics()) + 0.5f);

        Config config = new Config.Builder().width(indicatorWidth)
                .height(indicatorHeight)
                .margin(indicatorMargin)
                .animator(R.animator.indicator_animator)
                .animatorReverse(R.animator.indicator_animator_reverse)
                .drawable(R.drawable.black_radius_square)
                .build();
        indicator.initialize(config);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the previous slide as long as it is not on the first slide
                if (getitem(0) > 0){

                    mSLideViewPager.setCurrentItem(getitem(-1),true);

                }

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the next slide as long as it is not on the last slide
                // if on last slide, proceed to the next activity ( pet selection )
                if (getitem(0) < 2)
                    mSLideViewPager.setCurrentItem(getitem(1),true);
                else {

                    // Receive user data through intent
                    Intent receivingEnd = getIntent();
                    UserData user = receivingEnd.getParcelableExtra("User");
                    Intent i = new Intent(Tutorial_Page.this, CompanionSelectionActivity.class);

                    // Send user data
                    i.putExtra("User", user);
                    startActivity(i);
                    finish();

                }

            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to the next activity without swiping through all slides
                // Receive user data through intent
                Intent receivingEnd = getIntent();
                UserData user = receivingEnd.getParcelableExtra("User");
                Intent i = new Intent(Tutorial_Page.this, CompanionSelectionActivity.class);

                // Send user data
                i.putExtra("User", user);
                startActivity(i);
                finish();

            }
        });

        // Initialize ViewPager and its adapter
        mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        mSLideViewPager.setAdapter(viewPagerAdapter);

        // Set up the indicator and add a listener to handle changes in the ViewPager position
        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);
        indicator.setViewPager(mSLideViewPager);

    }

    @SuppressLint("NewApi")
    public void setUpindicator(int position){
        // Set up indicators and change dot colors
        dots = new TextView[3];

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            // Update the indicators based on the selected ViewPager position
            // skip button invible on last page, and change next to skip
            // change text color base on page number
            // no back button on the first page
            setUpindicator(position);
            if (position == 2){
                skipbtn.setVisibility(View.INVISIBLE);
                nextbtn.setText("Finish");
            }
            else{
                skipbtn.setVisibility(View.VISIBLE);
                nextbtn.setText("Next");
            }
            if (position == 0 || position == 1){
                int color = getResources().getColor(R.color.white);
                backbtn.setTextColor(color);
                nextbtn.setTextColor(color);

            }
            else{
                int color = getResources().getColor(R.color.defaultButtonText);
                backbtn.setTextColor(color);
                nextbtn.setTextColor(color);
            }
            if (position > 0){

                backbtn.setVisibility(View.VISIBLE);

            }
            else {

                backbtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }
}