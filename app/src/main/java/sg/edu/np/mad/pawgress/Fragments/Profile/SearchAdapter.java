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
        String name = firebaseList.get(position);
        holder.searchFriendName.setText(name);

        holder.searchAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendRequestSent) {
                    Toast.makeText(context, "Friend request already sent", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if request has already been sent
                }
                myDBHandler.addFriendReq(name, user, "Outgoing Pending");
                firebaseList.remove(name);
                notifyDataSetChanged();

                // Flag to indicate that the friend request has been sent
                friendRequestSent = true;
                Toast.makeText(context, "Friend request sent to " + name, Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");
                Query query = myRef.orderByChild("username").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Friend request already exists, update its status
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserData receivingUser = snapshot.getValue(UserData.class);
                                ArrayList<FriendRequest> reqList = receivingUser.getFriendReqList();
                                found:{
                                    for (FriendRequest req: reqList){
                                        if (req.getFriendReqName().equals(user.getUsername())){
                                            req.setReqStatus("Incoming Pending");
                                            break found;
                                        }
                                    }
                                    FriendRequest req = new FriendRequest(user.getUsername(), "Incoming Pending");
                                    reqList.add(req);
                                    receivingUser.setFriendReqList(reqList);
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

    }

    @Override
    public int getItemCount() {
        return firebaseList.size();
    }
}
