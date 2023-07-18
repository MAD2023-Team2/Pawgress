package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    ImageView iconImageView;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        iconImageView = itemView.findViewById(R.id.profile_picture_image_view);
    }
}


