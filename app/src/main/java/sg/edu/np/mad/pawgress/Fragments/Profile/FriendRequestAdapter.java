package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder>{

    Context context;
    ArrayList<String> friendRequestList;
    ArrayList<String> recyclerViewList;
    MyDBHandler myDBHandler;

    UserData user;
    private FriendRequestAdapterListener listener;

    public FriendRequestAdapter(Context context, UserData user, MyDBHandler myDBHandler, ArrayList<String> friendRequestList, FriendRequestAdapterListener listener) {
        this.context = context;
        this.user = user;
        this.myDBHandler = myDBHandler;
        this.friendRequestList = friendRequestList;
        this.listener = listener;
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.request_of_friend,
                parent,
                false);

        return new FriendRequestViewHolder(item);
    }

    @Override
    public void onBindViewHolder(FriendRequestViewHolder holder, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        String name = friendRequestList.get(holder.getAdapterPosition());
        holder.friendRequestName.setText(name);

        // Getting profile pic of incoming friend request
        Query query3 = myRef.child(name).child("profilePicturePath");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicturePath = dataSnapshot.getValue(String.class);
                    int profilePicturePathInt = Integer.parseInt(profilePicturePath);

                    // Setting profile pic of request
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


        // Accept friend request button
        holder.acceptFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updating shared preference of request list size
                SaveSharedPreference.setOldReqlistsize(context, SaveSharedPreference.getOldReqlistsize(context)-1);

                //add friend
                myDBHandler.addFriend(name, user, "Friend");
                user.setFriendList(myDBHandler.findFriendList(user));

                //remove friend from friend request
                myDBHandler.removeFriendReq(name, user);
                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                friendRequestList.remove(name);
                // Notify the request recyclerview about the item removal
                notifyDataSetChanged();

                // Notify friends recyclerview of added friend
                listener.onFriendRequestAccepted(name);

                Toast.makeText(context, name+"'s friend request has been accepted", Toast.LENGTH_SHORT).show();

                // Updating requester of addition of current user as friend
                Query query = myRef.orderByChild("username").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Friend request already exists, remove it from request list
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                for (FriendRequest req: reqList){
                                    if (req.getFriendReqName().equals(user.getUsername())){
                                        //remove friend request
                                        reqList.remove(req);
                                        receivingUser.setFriendReqList(reqList);
                                        break;
                                    }
                                }
                                ArrayList<FriendData> friendList = receivingUser.getFriendList();
                                // Checks if friend is already in friend list, if it is: change the status
                                found:{
                                    for (FriendData friend: friendList){
                                        if (friend.getFriendName().equals(user.getUsername())){
                                            friendList.remove(friend);

                                            FriendData newFriend = new FriendData(user.getUsername(), "Friend");
                                            friendList.add(newFriend);
                                            receivingUser.setFriendList(friendList);
                                            break found;
                                        }
                                    }
                                    // If friend not in friend list, add friend to friend list
                                    FriendData newFriend = new FriendData(user.getUsername(), "Friend");
                                    friendList.add(newFriend);
                                    receivingUser.setFriendList(friendList);
                                }
                                // Update requester info in firebase
                                myRef.child(name).setValue(receivingUser);

                                // Updating current user of addition of requester as friend
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

                                                // If requester already exists in current user's friends list as "Unfriended"
                                                ArrayList<FriendData> thisUserFriendList = myDBHandler.findFriendList(user);
                                                for (FriendData friend: thisUserFriendList){
                                                    if (friend.getFriendName().equals(receivingUser.getUsername())){
                                                        // Remove the requester from list
                                                        thisUserFriendList.remove(friend);
                                                        break;
                                                    }
                                                }
                                                // Add requester back with "Friend" status
                                                thisUserFriendList.add(new FriendData(receivingUser.getUsername(), "Friend"));

                                                // Removing requester from current user's request list
                                                ArrayList<FriendRequest> thisUserFriendReqList = myDBHandler.findFriendReqList(user);
                                                for (FriendRequest req: thisUserFriendReqList){
                                                    if (req.getFriendReqName().equals(receivingUser.getUsername())){
                                                        thisUserFriendReqList.remove(req);
                                                        break;
                                                    }
                                                }

                                                // Updating user info
                                                myDBHandler.addFriend(receivingUser.getUsername(), user, "Friend");
                                                user.setTaskList(myDBHandler.findTaskList(user));
                                                user.setFriendList(thisUserFriendList);
                                                user.setFriendReqList(thisUserFriendReqList);

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
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        });

        // Reject friend request button
        holder.rejectFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updating shared preference of request list size
                SaveSharedPreference.setOldReqlistsize(context, SaveSharedPreference.getOldReqlistsize(context)-1);

                //remove friend from friend request
                myDBHandler.removeFriendReq(name, user);
                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                friendRequestList.remove(name);
                // Notify the request recyclerview about the item removal
                notifyItemRemoved(holder.getAdapterPosition());

                Toast.makeText(context, name+"'s friend request has been rejected", Toast.LENGTH_SHORT).show();

                // Updating requester of rejection of request
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Friend request already exists, remove it from request list
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);

                                // Removing current user from requester's request list
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                for (FriendRequest req: reqList){
                                    if (req.getFriendReqName().equals(user.getUsername())){
                                        //remove friend request
                                        reqList.remove(req);
                                        receivingUser.setFriendReqList(reqList);
                                        break;
                                    }
                                }

                                // Removing requester from current user's request list
                                ArrayList<FriendRequest> thisUserReqList = user.getFriendReqList();
                                for (FriendRequest req: thisUserReqList){
                                    if (req.getFriendReqName().equals(receivingUser.getUsername())){
                                        thisUserReqList.remove(req);
                                        user.setFriendReqList(reqList);
                                        break;
                                    }
                                }
                                // Updating current user and requester info
                                myRef.child(name).setValue(receivingUser);
                                myRef.child(user.getUsername()).setValue(user);
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

    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    // Send added friend to friends.class to update friends recycler
    public interface FriendRequestAdapterListener {
        void onFriendRequestAccepted(String friendName);
    }
}
