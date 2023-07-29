package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
import android.content.Intent;
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

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.FriendData;
import sg.edu.np.mad.pawgress.FriendRequest;
import sg.edu.np.mad.pawgress.LoginPage;
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
        // Commented code below was attempt to update friends info automatically whenever the user data changed in the firebase
        // Update of friends info was successful past the first friend added via them accepting current user request
        // Unsure as to why first friend added when other user accepted current user request would not trigger code below
        // This code has effectively been replaced by refresh button

        /*Query updateFriendsList = myRef.child(user.getUsername()).child("friendList");
        updateFriendsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean friendFound = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FriendData friend = dataSnapshot.getValue(FriendData.class);
                    Log.v("REALTIMEUPDATE", "BIG LOOP----------------1" +friend.getFriendName() +friend.getStatus() + "!");
                    if (friend.getStatus().equals("Friend Incoming")) {
                        Log.v("REALTIMEUPDATE", "FRIEND INCOMING----------------1");
                        found1:{
                            for(FriendData recyclerFriend: recyclerFriendList){
                                if(recyclerFriend.getFriendName().equals(friend.getFriendName())){
                                    friendFound = true;
                                    break found1;
                                }
                            }
                            ArrayList<FriendData> dbList = myDBHandler.findFriendList(user);
                            found: {
                                Log.v("REALTIMEUPDATE", "FOUND----------------1");
                                for (FriendData existingFriend : dbList) {
                                    if (existingFriend.getFriendName().equals(friend.getFriendName())) {
                                        existingFriend.setStatus("Friend");
                                        myDBHandler.removeAllFriends(user);
                                        for (FriendData frienddb : dbList) {
                                            myDBHandler.addFriend(frienddb.getFriendName(), user, frienddb.getStatus());
                                            Log.v("REALTIMEUPDATE", "FRIEND-------------" + frienddb.getFriendName() + frienddb.getStatus());
                                        }
                                        recyclerFriendList.add(friend);
                                        Log.v("REALTIMEUPDATE", "---------------------1");
                                        notifyDataSetChanged();
                                        myRef.child(user.getUsername()).child("friendList").setValue(myDBHandler.findFriendList(user));
                                        Log.v("REALTIMEUPDATE", "BREAK-FOUND-------------1");
                                        friendFound = true;
                                        break found;
                                    }
                                }
                                friend.setStatus("Friend");
                                myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                                recyclerFriendList.add(friend);
                                Log.v("REALTIMEUPDATE", "---------------------2");
                                notifyDataSetChanged();
                                myRef.child(user.getUsername()).child("friendList").setValue(myDBHandler.findFriendList(user));
                            }
                            Log.v("REALTIMEUPDATE", "BREAK-FOUND-------------2");
                            friendFound = true;
                        }
                    } else if (friend.getStatus().equals("Unfriended Incoming")) {
                        ArrayList<FriendData> dbList = myDBHandler.findFriendList(user);
                        for (FriendData existingFriend : dbList) {
                            if (existingFriend.getFriendName().equals(friend.getFriendName())) {
                                existingFriend.setStatus("Unfriended");
                                myDBHandler.removeAllFriends(user);
                                for (FriendData frienddb : dbList) {
                                    myDBHandler.addFriend(frienddb.getFriendName(), user, frienddb.getStatus());
                                    Log.v("REALTIMEUPDATE", "FRIEND-------------" + frienddb.getFriendName() + frienddb.getStatus());
                                }
                                recyclerFriendList.remove(existingFriend);
                                notifyDataSetChanged();
                                Log.v("REALTIMEUPDATE", "---------------------3");
                                myRef.child(user.getUsername()).child("friendList").setValue(myDBHandler.findFriendList(user));
                                friendFound = true;
                                break;
                            }
                        }
                        Log.v("REALTIMEUPDATE", "BREAK-FOUND---------3");
                        break;
                    }
                    if (friendFound) {
                        break; // Break out of the outer loop if a friend is found or unfriended
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        FriendData friend = recyclerFriendList.get(position);
        holder.friendName.setText(friend.getFriendName());

        // Getting profile pic of friend
        Query query3 = myRef.child(friend.getFriendName()).child("profilePicturePath");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicturePath = dataSnapshot.getValue(String.class);
                    int profilePicturePathInt = Integer.parseInt(profilePicturePath);

                    // Setting profile pic of friend
                    switch (profilePicturePathInt) {
                        case 1: holder.profilePic.setImageResource(R.drawable.corgi_sunglasses); break;
                        case 2: holder.profilePic.setImageResource(R.drawable.corgi); break;
                        case 3: holder.profilePic.setImageResource(R.drawable.corgi_bone_toy); break;
                        case 4: holder.profilePic.setImageResource(R.drawable.golden_retriever); break;
                        case 5: holder.profilePic.setImageResource(R.drawable.retriever_sunglasses); break;
                        case 6: holder.profilePic.setImageResource(R.drawable.retriever_bone_toy); break;
                        case 7: holder.profilePic.setImageResource(R.drawable.grey_cat); break;
                        case 8: holder.profilePic.setImageResource(R.drawable.grey_sunglasses_cat); break;
                        case 9: holder.profilePic.setImageResource(R.drawable.grey_fish_cat); break;
                        case 10: holder.profilePic.setImageResource(R.drawable.orange_cat); break;
                        case 11: holder.profilePic.setImageResource(R.drawable.orange_sunglasses_cat); break;
                        case 12: holder.profilePic.setImageResource(R.drawable.orange_fish_cat); break;
                        default: holder.profilePic.setImageResource(R.drawable.corgi_sunglasses); break;
                    }
                }
                else{
                    holder.profilePic.setImageResource(R.drawable.corgi_sunglasses);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Remove friend button
        holder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.setStatus("Unfriend");
                recyclerFriendList.remove(friend);
                // Update friends recyclerview when friend is removed
                notifyDataSetChanged();

                // Updating friend's info of change in current user's status in firebase
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(friend.getFriendName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Current user already exists, update its status to "Unfriended"
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
                                // Update friend's info in firebase
                                myRef.child(friend.getFriendName()).setValue(receivingUser);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });

                // Updating current user info of change in friend's status in firebase
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

                                // Removing friend (change status to "Unfriended")
                                myDBHandler.removeFriend(friend.getFriendName(), user);

                                // Update user object
                                user.setTaskList(myDBHandler.findTaskList(user));
                                user.setFriendList(myDBHandler.findFriendList(user));
                                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                                for (FriendData friend: user.getFriendList()){
                                    Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                                }
                            }
                        }
                        // Update user info in firebase
                        myRef.child(user.getUsername()).setValue(user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });

        // View friend's game room button
        holder.viewFriend .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pulling friend info from firebase
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(friend.getFriendName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);

                                // Start ViewFriendGame intent with most recent friend info
                                Intent intent = new Intent(context, ViewFriendGame.class);
                                intent.putExtra("User", receivingUser);
                                context.startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerFriendList.size();
    }

    // Updating of friend list
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

    // Update friend recycler, refresh
    public void setData(ArrayList<FriendData> firebaseList){
        this.recyclerFriendList = firebaseList;
        notifyDataSetChanged();
    }
}
