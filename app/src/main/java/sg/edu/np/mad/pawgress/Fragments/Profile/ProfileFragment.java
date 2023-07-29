package sg.edu.np.mad.pawgress.Fragments.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import sg.edu.np.mad.pawgress.FriendData;
import sg.edu.np.mad.pawgress.FriendRequest;
import sg.edu.np.mad.pawgress.LandingPage;
import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskCompletion;
import sg.edu.np.mad.pawgress.Tasks.TaskGame;
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
                // Checks that there is internet connection before logging out
                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(), "No internet access. Unable to log out.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Clears shared preference so no auto login
                    SaveSharedPreference.clearUserName(getActivity());

                    // Goes to login page after logging out
                    Intent intent = new Intent(getActivity(), LoginPage.class);
                    startActivity(intent);
                }
            }
        });

        // Edit Profile Button
        Button editProfileButton = (Button) view.findViewById(R.id.editProfilePassword);
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

        // Analytics Button
        Button analyticsButton = (Button) view.findViewById(R.id.analytics);
        analyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Analytics.class);
                intent.putExtra("User", dbData);
                startActivity(intent);
            }
        });

        // profile picture image view
        // ImageView profilePictureImageView = view.findViewById(R.id.profile_picture_image_view);
        //this.profilePictureImageView = profilePictureImageView; // Assign to class level variable


        Button friendsButton = (Button) view.findViewById(R.id.findFriends);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    Toast.makeText(getActivity(), "No internet access. Unable to access friends.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getActivity(), friends.class);
                    intent.putExtra("User", dbData);
                    startActivity(intent);
                }
            }
        });

        //Delete Account button
        Button deleteAccountButton = (Button) view.findViewById(R.id.deleteButton);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    Toast.makeText(getActivity(), "No internet access. Unable to delete account.", Toast.LENGTH_SHORT).show();
                }
                else{
                    // cheat method for deleting account
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Account?");
                    builder.setMessage("");

                    // Set the positive button and its click listener
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                            DatabaseReference myRef = database.getReference("Users");

                            String userName = dbData.getUsername();
                            Toast.makeText(getContext(), "Account has been deleted!", Toast.LENGTH_SHORT).show();


                            // Set friends and friend request list based on Firebase, not SQLite
                            Query query = myRef.orderByChild("username").equalTo(dbData.getUsername());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            UserData tempUser = snapshot.getValue(UserData.class);

                                            // Get most recent friend and request list from firebase
                                            dbData.setFriendList(tempUser.getFriendList());
                                            dbData.setFriendReqList(tempUser.getFriendReqList());
                                            for (FriendData friend: dbData.getFriendList()){
                                                Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                                            }

                                            for (FriendData friend: dbData.getFriendList()){
                                                Log.v("22345678345678", String.valueOf(friend.getFriendName()));
                                                Query query = myRef.orderByChild("username").equalTo(friend.getFriendName());
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Log.v("22345678345678", String.valueOf(dataSnapshot.getValue()));

                                                        if (dataSnapshot.exists()) {
                                                            // Friend exists, update as deleted
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                UserData receivingUser = snapshot.getValue(UserData.class);
                                                                ArrayList<FriendData> friendList = receivingUser.getFriendList();
                                                                found:{
                                                                    for (FriendData friend: friendList){
                                                                        if (friend.getFriendName().equals(dbData.getUsername())){
                                                                            friend.setStatus("Deleted");
                                                                            break found;
                                                                        }
                                                                    }
                                                                }
                                                                for (FriendData friend: dbData.getFriendList()){
                                                                    Log.i(null, "Remove---------------------------------" + friend.getFriendName()+ friend.getStatus());
                                                                }
                                                                myRef.child(friend.getFriendName()).setValue(receivingUser);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                            }

                                            for (FriendRequest friendRequest: dbData.getFriendReqList()){
                                                Query query = myRef.orderByChild("username").equalTo(friendRequest.getFriendReqName());
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                UserData friend = snapshot.getValue(UserData.class);
                                                                ArrayList<FriendRequest> friendFriendReqList = friend.getFriendReqList();
                                                                for (FriendRequest friendRequest: friendFriendReqList){
                                                                    if (friendRequest.getFriendReqName().equals(dbData.getUsername())){
                                                                        friendRequest.setFriendReqName("Deleted");
                                                                    }
                                                                }
                                                                myRef.child(friend.getUsername()).child("friendReqList").setValue(friendFriendReqList);
                                                            }

                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                            }

                                            // Remove user in firebase
                                            myRef.child(userName).removeValue();
                                            myDBHandler.clearDatabase("ACCOUNTS");
                                            myDBHandler.clearDatabase("TASKS");
                                            myDBHandler.clearDatabase("FRIENDS");
                                            myDBHandler.clearDatabase("FRIENDREQUEST");

                                            // Clears shared preference so no auto login
                                            SaveSharedPreference.clearUserName(getActivity());

                                            // Goes to login page after logging out
                                            Intent intent = new Intent(getActivity(), LandingPage.class);
                                            startActivity(intent);
                                        }
                                    }
                                    else{
                                        // Remove user in firebase
                                        myRef.child(userName).removeValue();
                                        myDBHandler.clearDatabase("ACCOUNTS");
                                        myDBHandler.clearDatabase("TASKS");
                                        myDBHandler.clearDatabase("FRIENDS");
                                        myDBHandler.clearDatabase("FRIENDREQUEST");

                                        // Clears shared preference so no auto login
                                        SaveSharedPreference.clearUserName(getActivity());

                                        // Goes to login page after logging out
                                        Intent intent = new Intent(getActivity(), LandingPage.class);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                            // Clears shared preference so no auto login
                            SaveSharedPreference.clearUserName(getActivity());

                            // Goes to login page after logging out
                            Intent intent = new Intent(getActivity(), LandingPage.class);
                            startActivity(intent);
                        }
                    });

                    // Set the negative button and its click listener
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nth
                        }
                    });

                    // Create and show the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Setting profile picture
        switch (SaveSharedPreference.getProfilePic(getActivity())) {
            case 1: profilePictureImageView.setImageResource(R.drawable.corgi_sunglasses); break;
            case 2: profilePictureImageView.setImageResource(R.drawable.corgi); break;
            case 3: profilePictureImageView.setImageResource(R.drawable.corgi_bone_toy); break;
            case 4: profilePictureImageView.setImageResource(R.drawable.golden_retriever); break;
            case 5: profilePictureImageView.setImageResource(R.drawable.retriever_sunglasses); break;
            case 6: profilePictureImageView.setImageResource(R.drawable.retriever_bone_toy); break;
            case 7: profilePictureImageView.setImageResource(R.drawable.grey_cat); break;
            case 8: profilePictureImageView.setImageResource(R.drawable.grey_sunglasses_cat); break;
            case 9: profilePictureImageView.setImageResource(R.drawable.grey_fish_cat); break;
            case 10: profilePictureImageView.setImageResource(R.drawable.orange_cat); break;
            case 11: profilePictureImageView.setImageResource(R.drawable.orange_sunglasses_cat); break;
            case 12: profilePictureImageView.setImageResource(R.drawable.orange_fish_cat); break;
            default: profilePictureImageView.setImageResource(R.drawable.corgi_sunglasses); break;
        }

        /*// Retrieve the updated profile picture path from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicturePath = sharedPreferences.getString("profilePicturePath", "");

        // Update the profile picture ImageView if the path is not empty
        if (!profilePicturePath.isEmpty() && profilePictureImageView != null) {
            int profilePictureResId = Integer.parseInt(profilePicturePath);
            profilePictureImageView.setImageResource(profilePictureResId);
        }*/
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}