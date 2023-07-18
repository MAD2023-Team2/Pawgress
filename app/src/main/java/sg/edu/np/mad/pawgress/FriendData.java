package sg.edu.np.mad.pawgress;

import sg.edu.np.mad.pawgress.Fragments.Profile.FriendsAdapter;

public class FriendData {
    private String friendName;
    private String status;

    public FriendData(String friendName, String status){
        this.friendName = friendName;
        this.status = status;
    }

    public  FriendData(){};

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
