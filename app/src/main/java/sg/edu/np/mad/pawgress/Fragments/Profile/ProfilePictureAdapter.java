package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class ProfilePictureAdapter extends RecyclerView.Adapter<ProfilePictureAdapter.ProfileViewHolder> {
    private ArrayList<Integer> profilePictures;
    private Context context;
    private UserData userData;
    private ProfilePictureSelectionListener listener;

    public ProfilePictureAdapter(ArrayList<Integer> profilePictures, Context context, UserData userData, ProfilePictureSelectionListener listener) {
        this.profilePictures = profilePictures;
        this.context = context;
        this.userData = userData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_profile_picture, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Log.e("ProfilePictureAdapter", "OnBind Works!");
        int profilePicture = profilePictures.get(position);
        holder.profilePictureImageView.setImageResource(profilePicture);

        holder.profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData == null) {
                    Log.e("ProfilePictureAdapter", "UserData object is null");
                    return;
                }

                /*
                ----- Dictionary -----
                1 = corgi_sunglasses
                2 = corgi
                3 = corgi_bone_toy
                4 = golden_retriever
                5 = retriever_sunglasses
                6 = retriever_bone_toy
                7 = grey_cat
                8 = grey_sunglasses_cat
                9 = grey_fish_cat
                10 = orange_cat
                11 = orange_sunglasses_cat
                12 = orange_fish_cat
                */

                int selectedProfilePicture = profilePictures.get(holder.getAdapterPosition());

                // Converting each image to an id
                if (selectedProfilePicture == R.drawable.corgi_sunglasses){selectedProfilePicture = 1;}
                else if (selectedProfilePicture == R.drawable.corgi) {selectedProfilePicture = 2;}
                else if (selectedProfilePicture == R.drawable.corgi_bone_toy) {selectedProfilePicture = 3;}
                else if (selectedProfilePicture == R.drawable.golden_retriever) {selectedProfilePicture = 4;}
                else if (selectedProfilePicture == R.drawable.retriever_sunglasses) {selectedProfilePicture = 5;}
                else if (selectedProfilePicture == R.drawable.retriever_bone_toy) {selectedProfilePicture = 6;}
                else if (selectedProfilePicture == R.drawable.grey_cat) {selectedProfilePicture = 7;}
                else if (selectedProfilePicture == R.drawable.grey_sunglasses_cat) {selectedProfilePicture = 8;}
                else if (selectedProfilePicture == R.drawable.grey_fish_cat) {selectedProfilePicture = 9;}
                else if (selectedProfilePicture == R.drawable.orange_cat) {selectedProfilePicture = 10;}
                else if (selectedProfilePicture == R.drawable.orange_sunglasses_cat) {selectedProfilePicture = 11;}
                else if (selectedProfilePicture == R.drawable.orange_fish_cat) {selectedProfilePicture = 12;}
                else{
                    // Adding this here in case we add more profile pics, follow above if statements accordingly
                    selectedProfilePicture = 1;
                }

                String profilePicturePath = userData.getProfilePicturePath();

                if (profilePicturePath != null && profilePicturePath.equals(String.valueOf(selectedProfilePicture))) {
                    Log.i("ProfilePictureAdapter", "Profile Picture existingly selected");
                    Toast.makeText(context, "You have already selected this profile picture.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("ProfilePictureAdapter", "Updating profile picture...");
                    userData.setProfilePicturePath(String.valueOf(selectedProfilePicture));

                    // Saving profile pic id number
                    MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
                    SaveSharedPreference.setProfilePic(context, selectedProfilePicture);
                    dbHandler.saveProfilePicture(userData.getUsername(), String.valueOf(selectedProfilePicture));

                    // Converting back to R.drawable path
                    switch (selectedProfilePicture) {
                        case 1: selectedProfilePicture = R.drawable.corgi_sunglasses; break;
                        case 2: selectedProfilePicture = R.drawable.corgi; break;
                        case 3: selectedProfilePicture = R.drawable.corgi_bone_toy; break;
                        case 4: selectedProfilePicture = R.drawable.golden_retriever; break;
                        case 5: selectedProfilePicture = R.drawable.retriever_sunglasses; break;
                        case 6: selectedProfilePicture = R.drawable.retriever_bone_toy; break;
                        case 7: selectedProfilePicture = R.drawable.grey_cat; break;
                        case 8: selectedProfilePicture = R.drawable.grey_sunglasses_cat; break;
                        case 9: selectedProfilePicture = R.drawable.grey_fish_cat; break;
                        case 10: selectedProfilePicture = R.drawable.orange_cat; break;
                        case 11: selectedProfilePicture = R.drawable.orange_sunglasses_cat; break;
                        case 12: selectedProfilePicture = R.drawable.orange_fish_cat; break;
                        default: selectedProfilePicture = R.drawable.corgi_sunglasses; break;
                    }

                    listener.onProfilePictureSelected(selectedProfilePicture);
                    Log.i("SaveSharedPref", "----------------" + SaveSharedPreference.getProfilePic(context));
                    Toast.makeText(context, "Profile picture set!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return profilePictures.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePictureImageView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            profilePictureImageView = itemView.findViewById(R.id.profile_picture_image_view);
        }
    }

    public interface ProfilePictureSelectionListener {
        void onProfilePictureSelected(int profilePicture);
    }
}

