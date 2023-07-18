package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder{
    TextView friendRequestName;
    ImageButton rejectFriendRequest;
    ImageButton acceptFriendRequest;

    public FriendRequestViewHolder(View itemView){
        super(itemView);
        friendRequestName = itemView.findViewById(R.id.friendRequestName);
        acceptFriendRequest = itemView.findViewById(R.id.acceptFriendRequest);
        rejectFriendRequest = itemView.findViewById(R.id.rejectFriendRequest);
    }
}
