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

                int selectedProfilePicture = profilePictures.get(holder.getAdapterPosition());
                String profilePicturePath = userData.getProfilePicturePath();

                if (profilePicturePath != null && profilePicturePath.equals(String.valueOf(selectedProfilePicture))) {
                    Log.i("ProfilePictureAdapter", "Profile Picture existingly selected");
                    Toast.makeText(context, "You have already selected this profile picture.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("ProfilePictureAdapter", "Updating profile picture...");
                    userData.setProfilePicturePath(String.valueOf(selectedProfilePicture));
                    MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
                    dbHandler.saveProfilePicture(userData.getUsername(), String.valueOf(selectedProfilePicture));
                    listener.onProfilePictureSelected(selectedProfilePicture);
                    SaveSharedPreference.setProfilePic(context, selectedProfilePicture);
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

