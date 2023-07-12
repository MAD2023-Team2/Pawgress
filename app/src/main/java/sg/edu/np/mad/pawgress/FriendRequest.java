package sg.edu.np.mad.pawgress;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class FriendRequest {
    private String friendReqName;
    private String reqStatus;

    public  FriendRequest(){}

    public FriendRequest(String friendReqName, String reqStatus) {
        this.friendReqName = friendReqName;
        this.reqStatus = reqStatus;
    }

    public String getFriendReqName() {
        return friendReqName;
    }

    public void setFriendReqName(String friendReqName) {
        this.friendReqName = friendReqName;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }
}
