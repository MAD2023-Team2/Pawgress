package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class friends extends AppCompatActivity implements FriendRequestAdapter.FriendRequestAdapterListener{

    UserData user;
    FloatingActionButton addFriend;
    Button friendRequest;
    SearchView searchView;

    SearchAdapter searchAdapter;
    FriendRequestAdapter friendRequestAdapter;

    FriendsAdapter friendsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        ArrayList<FriendRequest> friendRequests = myDBHandler.findFriendReqList(user);
        user.setFriendReqList(friendRequests);

        setContentView(R.layout.activity_friends);

        RecyclerView recyclerView = findViewById(R.id.friendsRecycler);
        friendsAdapter =
                new FriendsAdapter(this, user, myDBHandler);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendsAdapter);

        addFriend = findViewById(R.id.addFriend);
        Dialog searchDialog = new Dialog(this);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseList(new FirebaseDataListener() {
                    @Override
                    public void onDataLoaded(ArrayList<String> firebaseList) {
                        /*
                        for (String name: firebaseList){
                            FriendRequest req = new FriendRequest(name, "Outgoing Pending");
                            ArrayList<FriendRequest> reqList = myDBHandler.findFriendReqList(user);
                            if (reqList.contains(req)){
                                firebaseList.remove(req.getFriendReqName());
                            }
                        }
                         */
                        searchDialog.setContentView(R.layout.search_friend);
                        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        searchDialog.setCancelable(true);

                        searchDialog.show();

                        RecyclerView searchRecyclerView = searchDialog.findViewById(R.id.searchRecyclerView);
                        searchAdapter =
                                new SearchAdapter(friends.this, firebaseList, myDBHandler, user);
                        LinearLayoutManager searchLayoutManager =
                                new LinearLayoutManager(friends.this);
                        searchRecyclerView.setLayoutManager(searchLayoutManager);
                        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        searchRecyclerView.setAdapter(searchAdapter);

                        searchView = searchDialog.findViewById(R.id.searchView);
                        searchView.clearFocus();
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                filterList(newText, firebaseList);
                                return true;
                            }
                        });
                    }
                });

            }
        });

        friendRequest = findViewById(R.id.friendRequest);
        Dialog requestDialog = new Dialog(this);
        friendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFirebaseRequestList(new FirebaseRequestDataListener() {
                    @Override
                    public void onRequestDataLoaded(ArrayList<String> firebaseRequestList) {
                        requestDialog.setContentView(R.layout.friend_requests);
                        requestDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        requestDialog.setCancelable(true);

                        requestDialog.show();

                        RecyclerView friendRequestRecyclerView = requestDialog.findViewById(R.id.friendRequestRecyclerView);
                        friendRequestAdapter =
                                new FriendRequestAdapter(friends.this, user, myDBHandler, firebaseRequestList, friends.this);
                        LinearLayoutManager searchLayoutManager =
                                new LinearLayoutManager(friends.this);
                        friendRequestRecyclerView.setLayoutManager(searchLayoutManager);
                        friendRequestRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        friendRequestRecyclerView.setAdapter(friendRequestAdapter);
                    }
                });
            }
        });
    }

    public void filterList(String text, ArrayList<String> firebaseList){
        ArrayList<String> filteredList = new ArrayList<>();
        for (String name: firebaseList){
            if (name.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(name);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
        }
        else {
            searchAdapter.setFilteredList(filteredList);
        }
    }

    public interface FirebaseDataListener {
        void onDataLoaded(ArrayList<String> firebaseList);
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    public void getFirebaseList(FirebaseDataListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> firebaseList = new ArrayList<>();

                UserData fbUser = myDBHandler.findUser(user.getUsername());
                fbUser.setTaskList(myDBHandler.findTaskList(user));
                fbUser.setFriendList(myDBHandler.findFriendList(user));
                fbUser.setFriendReqList(myDBHandler.findFriendReqList(user));

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = snapshot.getKey();
                        boolean isInFriendList = false;
                        boolean isInFriendReqList = false;

                        // Check the if user is already in Friends list
                        for (FriendData friend: user.getFriendList()){
                            if (friend.getFriendName().equals(username) && friend.getStatus().equals("Friend")) {
                                isInFriendList = true;
                                break;
                            }
                        }
                        // Check the if user is already in Friend Requests list
                        for (FriendRequest friendRequest: user.getFriendReqList()){
                            if (friendRequest.getFriendReqName().equals(username) && (friendRequest.getReqStatus().equals("Outgoing Pending") || friendRequest.getReqStatus().equals("Incoming Pending") || friendRequest.getReqStatus().equals("Accepted"))){
                                isInFriendReqList = true;
                            }
                        }
                        // If the name is not in either the friend list or friend requests list, add it to firebaseList
                        if (!isInFriendList && !isInFriendReqList && !username.equals(user.getUsername()) && !username.equals("admin")) {
                            firebaseList.add(username);
                            Log.v(null, username);
                        }
                    }
                }
                else {
                    Log.d("Usernames", "No usernames found");
                }
                listener.onDataLoaded(firebaseList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onDataLoaded(new ArrayList<>());
            }
        });
    }

    public interface FirebaseRequestDataListener {
        void onRequestDataLoaded(ArrayList<String> firebaseRequestList);
    }

    public void getFirebaseRequestList(FirebaseRequestDataListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users").child(user.getUsername()).child("friendReqList");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> firebaseReqList = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendReqName = snapshot.child("friendReqName").getValue(String.class);
                        String friendReqStatus = snapshot.child("reqStatus").getValue(String.class);

                        if (friendReqStatus != null && friendReqStatus.equals("Incoming Pending")) {
                            firebaseReqList.add(friendReqName);
                            Log.v(null, friendReqName);
                        }
                    }
                } else {
                    Log.d("Usernames", "No friend requests found");
                }
                listener.onRequestDataLoaded(firebaseReqList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onRequestDataLoaded(new ArrayList<>());
            }
        });
    }

    @Override
    public void onFriendRequestAccepted(String friendName) {
        friendsAdapter.updateFriendList(); // Call a method in the FriendsAdapter to update its data
    }
}