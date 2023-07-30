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

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    Context context;
    ArrayList<String> firebaseList;
    MyDBHandler myDBHandler;

    UserData user;

    private boolean friendRequestSent = false;

    public void setFilteredList(ArrayList<String> filteredList){
        this.firebaseList = filteredList;
        notifyDataSetChanged();
    }

    public SearchAdapter(Context context, ArrayList<String> firebaseList, MyDBHandler myDBHandler, UserData user){
        this.firebaseList = firebaseList;
        this.context = context;
        this.myDBHandler = myDBHandler;
        this.user = user;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_friend,
                parent,
                false);

        return new SearchViewHolder(item);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");
        String name = firebaseList.get(position);
        holder.searchFriendName.setText(name);

        // Setting profile pic of users in search friends dialog
        Query query3 = myRef.child(name).child("profilePicturePath");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Getting profile pic of each user
                    String profilePicturePath = dataSnapshot.getValue(String.class);
                    int profilePicturePathInt = Integer.parseInt(profilePicturePath);

                    // Setting of profile pic
                    switch (profilePicturePathInt) {
                        case 1: holder.searchProfilePic.setImageResource(R.drawable.corgi_sunglasses); break;
                        case 2: holder.searchProfilePic.setImageResource(R.drawable.corgi); break;
                        case 3: holder.searchProfilePic.setImageResource(R.drawable.corgi_bone_toy); break;
                        case 4: holder.searchProfilePic.setImageResource(R.drawable.golden_retriever); break;
                        case 5: holder.searchProfilePic.setImageResource(R.drawable.retriever_sunglasses); break;
                        case 6: holder.searchProfilePic.setImageResource(R.drawable.retriever_bone_toy); break;
                        case 7: holder.searchProfilePic.setImageResource(R.drawable.grey_cat); break;
                        case 8: holder.searchProfilePic.setImageResource(R.drawable.grey_sunglasses_cat); break;
                        case 9: holder.searchProfilePic.setImageResource(R.drawable.grey_fish_cat); break;
                        case 10: holder.searchProfilePic.setImageResource(R.drawable.orange_cat); break;
                        case 11: holder.searchProfilePic.setImageResource(R.drawable.orange_sunglasses_cat); break;
                        case 12: holder.searchProfilePic.setImageResource(R.drawable.orange_fish_cat); break;
                        default: holder.searchProfilePic.setImageResource(R.drawable.corgi_sunglasses); break;
                    }
                }
                else{
                    holder.searchProfilePic.setImageResource(R.drawable.corgi_sunglasses);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error

            }
        });

        // Send friend request button
        holder.searchAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDBHandler.addFriendReq(name, user, "Outgoing Pending");
                firebaseList.remove(name);
                notifyDataSetChanged();

                // Toast to indicate successful request sent
                Toast.makeText(context, "Friend request sent to " + name, Toast.LENGTH_SHORT).show();

                // Updating request list of request recipient
                Query query = myRef.orderByChild("username").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Friend request already exists, update its status
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);

                                // Setting user request status as "Incoming Pending"
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                found:{
                                    for (FriendRequest req: reqList){
                                        if (req.getFriendReqName().equals(user.getUsername())){
                                            req.setReqStatus("Incoming Pending");
                                            break found;
                                        }
                                    }
                                    // Add request to recipient's request list
                                    FriendRequest req = new FriendRequest(user.getUsername(), "Incoming Pending");
                                    reqList.add(req);
                                    receivingUser.setFriendReqList(reqList);
                                }

                                // Setting recipient status as "Outgoing Pending" in current user's request list
                                ArrayList<FriendRequest> thisUserReqList = user.getFriendReqList();
                                found:
                                {
                                    for (FriendRequest req : thisUserReqList) {
                                        if (req.getFriendReqName().equals(receivingUser.getUsername())) {
                                            thisUserReqList.remove(req);

                                            FriendRequest newFriendReq = new FriendRequest(receivingUser.getUsername(), "Outgoing Pending");
                                            thisUserReqList.add(newFriendReq);
                                            user.setFriendReqList(thisUserReqList);
                                            break found;
                                        }
                                    }
                                    // Add outgoing request to request list
                                    FriendRequest req = new FriendRequest(receivingUser.getUsername(), "Outgoing Pending");
                                    thisUserReqList.add(req);
                                    user.setFriendReqList(thisUserReqList);
                                }
                                // Update user and recipient info in firebase
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
        return firebaseList.size();
    }
}
