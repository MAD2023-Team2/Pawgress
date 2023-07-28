package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import sg.edu.np.mad.pawgress.R;

public class FriendsViewHolder extends RecyclerView.ViewHolder{
    TextView friendName;
    ImageButton removeFriend, viewFriend;
    ConstraintLayout friendCard;
    ImageView profilePic;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        friendName = itemView.findViewById(R.id.friendName);
        removeFriend = itemView.findViewById(R.id.removeFriend);
        viewFriend = itemView.findViewById(R.id.viewFriend);
        friendCard = itemView.findViewById(R.id.friendCard);
        profilePic = itemView.findViewById(R.id.profilePic);
    }
}
