package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import sg.edu.np.mad.pawgress.LandingPage;
import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class friends extends AppCompatActivity implements FriendRequestAdapter.FriendRequestAdapterListener{

    UserData user;
    Button addFriend;
    Button friendRequest;
    ImageView refreshButton;
    TextView returnButtonFriends;
    SearchView searchView;
    SearchAdapter searchAdapter;
    FriendRequestAdapter friendRequestAdapter;
    FriendsAdapter friendsAdapter;
    TextView requestCountText;
    TextView noFriendReqText;
    //TextView noFriendsText;
    int reqCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        MyDBHandler myDBHandler = new MyDBHandler(friends.this, null, null, 1);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        // Getting user info
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        // Instantiate firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");
        Query query = myRef.orderByChild("username").equalTo(user.getUsername());

        // Setting friend recyclerview
        RecyclerView recyclerView = findViewById(R.id.friendsRecycler);
        friendsAdapter =
                new FriendsAdapter(friends.this, user, myDBHandler);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(friends.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendsAdapter);

        // Back button
        returnButtonFriends = findViewById(R.id.backButton);
        returnButtonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Updating user friend info by pulling from firebase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserData tempUser = snapshot.getValue(UserData.class);

                        // Update user friends and request list
                        user.setFriendList(tempUser.getFriendList());
                        user.setFriendReqList(tempUser.getFriendReqList());

                        //ArrayList<FriendRequest> friendRequests = myDBHandler.findFriendReqList(user);
                        //user.setFriendReqList(friendRequests);
                        //setContentView(R.layout.activity_friends);

                        // Creating list to be put into recyclerview
                        ArrayList<FriendData> friendList = tempUser.getFriendList();
                        ArrayList<FriendData> recyclerFriendList = new ArrayList<FriendData>();
                        for (FriendData friend: friendList){
                            if (friend.getStatus().equals("Friend")){
                                recyclerFriendList.add(friend);
                            }
                        }
                        // Update friends recyclerview with new list
                        friendsAdapter.setData(recyclerFriendList);

                        // Count the number of friends in friend list
                        int numFriend = 0;
                        for (FriendData friend: user.getFriendList()){
                            if (friend.getStatus().equals("Friend")){
                                numFriend += 1;
                            }
                        }
                        /*noFriendsText = findViewById(R.id.noFriendsText);
                        if (numFriend == 0){
                            noFriendsText.setVisibility(View.VISIBLE);
                            noFriendsText.setText("No friends for now :(");
                        }*/
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // Search for friend button
        addFriend = findViewById(R.id.addFriend);
        Dialog searchDialog = new Dialog(friends.this, R.style.CustomDialog);
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
                        }*/

                        // Setting search dialog
                        searchDialog.setContentView(R.layout.search_friend);
                        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        searchDialog.setCancelable(true);

                        searchDialog.show();

                        // Setting search recyclerview
                        RecyclerView searchRecyclerView = searchDialog.findViewById(R.id.searchRecyclerView);
                        searchAdapter =
                                new SearchAdapter(friends.this, firebaseList, myDBHandler, user);
                        LinearLayoutManager searchLayoutManager =
                                new LinearLayoutManager(friends.this);
                        searchRecyclerView.setLayoutManager(searchLayoutManager);
                        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        searchRecyclerView.setAdapter(searchAdapter);

                        // Setting search bar
                        searchView = searchDialog.findViewById(R.id.searchView);
                        searchView.setQueryHint("Enter username");
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

        // Friend requests button
        friendRequest = findViewById(R.id.friendRequest);
        Dialog requestDialog = new Dialog(friends.this, R.style.CustomDialog);
        friendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFirebaseRequestList(new FirebaseRequestDataListener() {
                    @Override
                    public void onRequestDataLoaded(ArrayList<String> firebaseRequestList) {
                        // Setting request dialog
                        requestDialog.setContentView(R.layout.friend_requests);
                        requestDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        requestDialog.setCancelable(true);
                        requestDialog.show();

                        // Setting no friend request text
                        noFriendReqText = requestDialog.findViewById(R.id.noFriendReqText);
                        if (firebaseRequestList.size() == 0){
                            noFriendReqText.setVisibility(View.VISIBLE);
                            noFriendReqText.setText("No incoming friend requests :(");
                        }

                        // Saving the request as "seen" so request indicator does not keep showing
                        SaveSharedPreference.setSeenFriendReq(friends.this, "seen");
                        requestCountText = findViewById(R.id.requestCountText);
                        requestCountText.setVisibility(View.INVISIBLE);

                        // Setting friend request recyclerview
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

        // New incoming friend request indicator
        requestCountText = findViewById(R.id.requestCountText);
        DatabaseReference myRef1 = database.getReference("Users").child(user.getUsername()).child("friendReqList");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> firebaseReqList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendReqName = snapshot.child("friendReqName").getValue(String.class);
                        String friendReqStatus = snapshot.child("reqStatus").getValue(String.class);

                        // Checking that the change in request list is an incoming friend request
                        if (friendReqStatus != null && friendReqStatus.equals("Incoming Pending")) {
                            firebaseReqList.add(friendReqName);
                            Log.v(null, friendReqName);
                        }
                    }
                }

                // Comparing previous request list size and current size, if current size is bigger, there is a new request
                int oldReqListSize = SaveSharedPreference.getOldReqlistsize(friends.this);
                Log.v("", "-------check-------" + oldReqListSize + firebaseReqList.size() + SaveSharedPreference.getSeenFriendReq(friends.this));
                if (firebaseReqList.size() > oldReqListSize){
                    // Shared preference saved as "unseen" to indicate new friend request
                    SaveSharedPreference.setSeenFriendReq(friends.this, "unseen");
                    SaveSharedPreference.setOldReqlistsize(friends.this, firebaseReqList.size());
                }

                // Setting the friend request indicator
                reqCount = firebaseReqList.size();
                if ((reqCount != 0) && (!SaveSharedPreference.getSeenFriendReq(friends.this).equals("seen"))){
                    requestCountText.setText(String.valueOf(reqCount));
                    requestCountText.setVisibility(View.VISIBLE);
                    requestCountText.invalidate();
                }
                else{
                    requestCountText.setVisibility(View.INVISIBLE);
                    requestCountText.invalidate();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // Refresh button for friends recyclerview
        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(user.getUsername());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        if (dataSnapshot2.exists()) {
                            try{
                                for (DataSnapshot snapshot : dataSnapshot2.getChildren()) {
                                    // Pull updated user info
                                    UserData tempUser = snapshot.getValue(UserData.class);

                                    // Creating list to be put into recyclerview
                                    ArrayList<FriendData> friendList = tempUser.getFriendList();
                                    ArrayList<FriendData> recyclerFriendList = new ArrayList<FriendData>();
                                    for (FriendData friend: friendList){
                                        if (friend.getStatus().equals("Friend")){
                                            recyclerFriendList.add(friend);
                                        }
                                    }
                                    // Update friends recyclerview with new list, reloading list, notifies data change
                                    friendsAdapter.setData(recyclerFriendList);
                                    Toast.makeText(friends.this, "Refreshed",Toast.LENGTH_LONG).show();

                                    // Count the number of friends in friend list
                                    int numFriend = 0;
                                    for (FriendData friend: user.getFriendList()){
                                        if (friend.getStatus().equals("Friend")){
                                            numFriend += 1;
                                        }
                                    }
                                    /*noFriendsText = findViewById(R.id.noFriendsText);
                                    if (numFriend == 0){
                                        noFriendsText.setVisibility(View.VISIBLE);
                                        noFriendsText.setText("No friends for now :(");
                                    }*/
                                }
                            }
                            // Catch case if friends recyclerview is not instantiated yet when refresh button pressed
                            // Update 30/7/2023: After changes, pretty sure catch case is redundant
                            catch (Exception e){
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                // Pull updated user info
                                                UserData tempUser = snapshot.getValue(UserData.class);
                                                user.setFriendList(tempUser.getFriendList());
                                                user.setFriendReqList(tempUser.getFriendReqList());

                                                Log.v(null, "CATCH------------------");
                                                Toast.makeText(friends.this, "Refreshed",Toast.LENGTH_LONG).show();

                                                //ArrayList<FriendRequest> friendRequests = myDBHandler.findFriendReqList(user);
                                                //user.setFriendReqList(friendRequests);
                                                //setContentView(R.layout.activity_friends);

                                                // Setting friends recyclerview
                                                RecyclerView recyclerView = findViewById(R.id.friendsRecycler);
                                                friendsAdapter =
                                                        new FriendsAdapter(friends.this, user, myDBHandler);
                                                LinearLayoutManager mLayoutManager =
                                                        new LinearLayoutManager(friends.this);
                                                recyclerView.setLayoutManager(mLayoutManager);
                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                recyclerView.setAdapter(friendsAdapter);

                                                // Count the number of friends in friend list
                                                int numFriend = 0;
                                                for (FriendData friend: user.getFriendList()){
                                                    if (friend.getStatus().equals("Friend")){
                                                        numFriend += 1;
                                                    }
                                                }
                                                /*noFriendsText = findViewById(R.id.noFriendsText);
                                                if (numFriend == 0){
                                                    noFriendsText.setVisibility(View.VISIBLE);
                                                    noFriendsText.setText("No friends for now :(");
                                                }*/
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // Acts same as mainmainmain, on fresh, updates user info in firebase
                try{
                    // Setting copy of user object
                    UserData fbUser = myDBHandler.findUser(user.getUsername());
                    fbUser.setTaskList(myDBHandler.findTaskList(user));
                    fbUser.setFriendList(myDBHandler.findFriendList(user));
                    fbUser.setFriendReqList(myDBHandler.findFriendReqList(user));
                    fbUser.setInventoryList(myDBHandler.findInventoryList(user));
                    fbUser.setProfilePicturePath(String.valueOf(SaveSharedPreference.getProfilePic(friends.this)));

                    // Set friends and friend request list based on Firebase, not SQLite
                    Query query1 = myRef.orderByChild("username").equalTo(user.getUsername());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    UserData tempUser = snapshot.getValue(UserData.class);

                                    // Clear existing friend and friend request and inventory items data in local SQLite database
                                    myDBHandler.removeAllFriends(user);
                                    myDBHandler.removeAllFriendRequests(user);

                                    // Add new friends data to local SQLite database and inventory items
                                    for (FriendData friend : tempUser.getFriendList()) {
                                        myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                                    }
                                    for (FriendRequest req : tempUser.getFriendReqList()) {
                                        myDBHandler.addFriendReq(req.getFriendReqName(), user, req.getReqStatus());
                                    }

                                    // Update copy of user info with updated friends, requests, and inventory
                                    fbUser.setFriendList(tempUser.getFriendList());
                                    fbUser.setFriendReqList(tempUser.getFriendReqList());
                                    fbUser.setInventoryList(user.getInventoryList());
                                    for (FriendData friend: fbUser.getFriendList()){
                                        Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                                    }
                                }
                            }
                            // Updating user info in firebase
                            myRef.child(user.getUsername()).setValue(fbUser);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
                catch (Exception e){
                    // do nth ( account has been deleted, therefore no list )
                }
            }
        });
    }

    // Filter the list of user when text is entered into search bar for searching of friends
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

    // Interface to handle asynchronous behaviour of firebase data retrieval
    public interface FirebaseDataListener {
        void onDataLoaded(ArrayList<String> firebaseList);
    }

    // Firebase list of users for searching of friends to add
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

    // Interface to handle asynchronous behaviour of firebase data retrieval
    public interface FirebaseRequestDataListener {
        void onRequestDataLoaded(ArrayList<String> firebaseRequestList);
    }

    // Firebase list of friend requests for current user
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

    // From friend request adapter, when accept request button is pressed, update friends list with new friend
    @Override
    public void onFriendRequestAccepted(String friendName) {
        friendsAdapter.updateFriendList(); // Call a method in the FriendsAdapter to update its data
    }
}