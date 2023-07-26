package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class SearchViewHolder extends RecyclerView.ViewHolder{
    TextView searchFriendName;
    ImageButton searchAddFriend;
    ImageView searchProfilePic;

    public SearchViewHolder(View itemView){
        super(itemView);
        searchFriendName = itemView.findViewById(R.id.searchFriendName);
        searchAddFriend = itemView.findViewById(R.id.searchAddFriend);
        searchProfilePic = itemView.findViewById(R.id.searchProfilePic);
    }
}
