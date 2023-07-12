package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
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
        String name = friendRequestList.get(position);
        holder.friendRequestName.setText(name);

        holder.acceptFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add friend
                myDBHandler.addFriend(name, user, "Friend");
                user.setFriendList(myDBHandler.findFriendList(user));

                //remove friend from friend request
                myDBHandler.removeFriendReq(name, user);
                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                friendRequestList.remove(name);
                notifyDataSetChanged();

                listener.onFriendRequestAccepted(name);

                Toast.makeText(context, name+"'s friend request has been accepted", Toast.LENGTH_SHORT).show();

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
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                for (FriendRequest req: reqList){
                                    if (req.getFriendReqName().equals(user.getUsername())){
                                        //remove friend request
                                        reqList.remove(req);
                                        receivingUser.setFriendReqList(reqList);
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
                                myRef.child(name).setValue(receivingUser);
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

        holder.rejectFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove friend from friend request
                myDBHandler.removeFriendReq(name, user);
                user.setFriendReqList(myDBHandler.findFriendReqList(user));

                friendRequestList.remove(name);
                notifyDataSetChanged();

                Toast.makeText(context, name+"'s friend request has been rejected", Toast.LENGTH_SHORT).show();

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
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                for (FriendRequest req: reqList){
                                    if (req.getFriendReqName().equals(user.getUsername())){
                                        //remove friend request
                                        reqList.remove(req);
                                        receivingUser.setFriendReqList(reqList);
                                    }
                                }
                                ArrayList<FriendRequest> thisUserReqList = user.getFriendReqList();
                                for (FriendRequest req: thisUserReqList){
                                    if (req.getFriendReqName().equals(receivingUser.getUsername())){
                                        thisUserReqList.remove(req);
                                        user.setFriendReqList(reqList);
                                    }
                                }
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



    public interface FriendRequestAdapterListener {
        void onFriendRequestAccepted(String friendName);
    }
}
