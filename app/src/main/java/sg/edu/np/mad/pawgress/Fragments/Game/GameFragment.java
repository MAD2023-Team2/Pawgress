package sg.edu.np.mad.pawgress.Fragments.Game;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_game, container, false);

        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);
        UserData user = myDBHandler.findUser(SaveSharedPreference.getUserName(getActivity()));
        ImageView pet_picture = view.findViewById(R.id.corgi_1);
        if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user.getPetDesign() == R.drawable.orange_cat){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.corgi);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}
        pet_picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        int random = new Random().nextInt(3);
                        MediaPlayer mediaPlayer;

                        if (random == 0){
                            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi_down_sound);
                        } else if (random == 1) {
                            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi_up_sound);
                        }
                        else {
                            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi_3_sound);
                        }

                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            };
                        });
                        Animation anim = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                1f, 0.85f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim.setFillAfter(true); // Needed to keep the result of the animation
                        anim.setDuration(150);
                        v.startAnimation(anim);

                        break;

                    case MotionEvent.ACTION_UP:
                        Animation anim2 = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                0.85f, 1f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim2.setFillAfter(true); // Needed to keep the result of the animation
                        anim2.setDuration(150);
                        v.startAnimation(anim2);
                        break;
                }

                return true;
            }
        });
        return view;
    }
}