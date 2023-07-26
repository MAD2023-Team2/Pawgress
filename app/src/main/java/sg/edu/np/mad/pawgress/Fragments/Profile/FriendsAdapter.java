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
        Query updateFriendsList = myRef.child(user.getUsername()).child("friendList");
        updateFriendsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    FriendData friend = dataSnapshot.getValue(FriendData.class);
                    if (friend.getStatus().equals("Friend")){
                        found: {
                            for (FriendData existingFriend: recyclerFriendList)
                                if (existingFriend.getFriendName().equals(friend.getFriendName())) {
                                    break found;
                                }
                            recyclerFriendList.add(friend);
                            myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                            notifyDataSetChanged();
                        }
                    } else if (friend.getStatus().equals("Unfriended")) {
                        for (FriendData existingFriend: recyclerFriendList){
                            if (existingFriend.getFriendName().equals(friend.getFriendName())) {
                                recyclerFriendList.remove(existingFriend);
                                myDBHandler.removeFriend(friend.getFriendName(), user);
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        FriendData friend = recyclerFriendList.get(position);
        holder.friendName.setText(friend.getFriendName());

        Query query3 = myRef.child(friend.getFriendName()).child("profilePicturePath");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicturePath = dataSnapshot.getValue(String.class);
                    holder.profilePic.setImageResource(Integer.parseInt(profilePicturePath));
                }
                else{
                    int profilePicPath = 2131230856;
                    holder.profilePic.setImageResource(profilePicPath);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error

            }
        });

        holder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.setStatus("Unfriend");
                recyclerFriendList.remove(friend);
                notifyDataSetChanged();
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
                                for (FriendData friend: user.getFriendList()){
                                    Log.i(null, "Remove---------------------------------" + friend.getFriendName()+ friend.getStatus());
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

                Query query1 = myRef.orderByChild("username").equalTo(user.getUsername());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData tempUser = snapshot.getValue(UserData.class);

                                // Clear existing friend and friend request data in local SQLite database
                                myDBHandler.removeAllFriends(user);
                                myDBHandler.removeAllFriendRequests(user);

                                // Add new friends data to local SQLite database
                                for (FriendData friend : tempUser.getFriendList()) {
                                    myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                                }
                                for (FriendRequest req : tempUser.getFriendReqList()) {
                                    myDBHandler.addFriendReq(req.getFriendReqName(), user, req.getReqStatus());
                                }

                                myDBHandler.removeFriend(friend.getFriendName(), user);

                                user.setTaskList(myDBHandler.findTaskList(user));
                                user.setFriendList(myDBHandler.findFriendList(user));
                                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                                for (FriendData friend: user.getFriendList()){
                                    Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                                }
                            }
                        }
                        myRef.child(user.getUsername()).setValue(user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
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

    public void updateCurrentUserInFB(UserData user){

    }
}
