package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.FriendData;
import sg.edu.np.mad.pawgress.FriendRequest;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder>{

    ArrayList<FriendData> friendList;

    ArrayList<FriendData> recyclerFriendList;
    UserData user;
    MyDBHandler myDBHandler;
    Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("Users");

    public FriendsAdapter(Context context, UserData user, MyDBHandler myDBHandler) {
        this.context = context;
        this.user = user;
        this.friendList = myDBHandler.findFriendList(user);
        this.myDBHandler = myDBHandler;

        recyclerFriendList = new ArrayList<FriendData>();
        for (FriendData friend: friendList){
            if (friend.getStatus().equals("Friend")){
                recyclerFriendList.add(friend);
            }
        }
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend,
                parent,
                false);

        return new FriendsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        FriendData friend = recyclerFriendList.get(position);
        holder.friendName.setText(friend.getFriendName());
        holder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDBHandler.removeFriend(friend.getFriendName(), user);
                friend.setStatus("Unfriend");
                recyclerFriendList.remove(friend);
                notifyDataSetChanged();
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(friend.getFriendName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Friend already exists, update its status
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);
                                ArrayList<FriendData> friendList = receivingUser.getFriendList();
                                found:{
                                    for (FriendData friend: friendList){
                                        if (friend.getFriendName().equals(user.getUsername())){
                                            friend.setStatus("Unfriended");
                                            break found;
                                        }
                                    }
                                }
                                myRef.child(friend.getFriendName()).setValue(receivingUser);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        });

        holder.viewFriend .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerFriendList.size();
    }

    public void updateFriendList() {
        friendList = myDBHandler.findFriendList(user);
        recyclerFriendList.clear();
        for (FriendData friend : friendList) {
            if (friend.getStatus().equals("Friend")) {
                recyclerFriendList.add(friend);
            }
        }
        notifyDataSetChanged();
    }
}
