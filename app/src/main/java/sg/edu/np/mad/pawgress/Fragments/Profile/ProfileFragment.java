package sg.edu.np.mad.pawgress.Fragments.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ImageView profilePictureImageView;
    private int[] profilePictures = {R.drawable.corgi, R.drawable.corgi_sunglasses};



// TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePictureImageView = view.findViewById(R.id.imageView10);


        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        // Getting User data
        UserData dbData = myDBHandler.findUser(SaveSharedPreference.getUserName(getActivity()));
        TextView username = view.findViewById(R.id.ProfileUsername);

        // Set text as Username
        username.setText(dbData.getUsername());

        //Log Out button
        Button logoutButton = (Button) view.findViewById(R.id.logOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clears shared preference so no auto login
                SaveSharedPreference.clearUserName(getActivity());

                // Goes to login page after logging out
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });

        // Edit Profile Button
        Button editProfileButton = (Button) view.findViewById(R.id.editProfilePicture);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SaveSharedPreference.clearUserName(getActivity()); //clears shared preference so no auto login
                Intent intent = new Intent(getActivity(), editProfilePassword.class);
                intent.putExtra("User", dbData);
                startActivity(intent);
            }
        });

        // User Settings Button
        Button userSettingsButton = (Button) view.findViewById(R.id.userSettings);
        userSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ProfilePage", "User Settings button clicked");

                //SaveSharedPreference.clearUserName(getActivity()); //clears shared preference so no auto login
                Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                intent.putExtra("User", dbData);
                startActivity(intent);
            }
        });

        // profile picture image view
        // ImageView profilePictureImageView = view.findViewById(R.id.profile_picture_image_view);
        //this.profilePictureImageView = profilePictureImageView; // Assign to class level variable


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Retrieve the updated profile picture path from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicturePath = sharedPreferences.getString("profilePicturePath", "");

        // Update the profile picture ImageView if the path is not empty
        if (!profilePicturePath.isEmpty() && profilePictureImageView != null) {
            int profilePictureResId = Integer.parseInt(profilePicturePath);
            profilePictureImageView.setImageResource(profilePictureResId);
        }
    }



}