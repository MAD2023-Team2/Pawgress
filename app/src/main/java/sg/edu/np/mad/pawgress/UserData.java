package sg.edu.np.mad.pawgress;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryItem;
import sg.edu.np.mad.pawgress.Tasks.Task;

public class UserData implements Parcelable{
    private String username;
    private String password;
    private ArrayList<Task> taskList;
    private String lastLogInDate;
    private int streak;
    private int currency;
    private String loggedInTdy;
    private String petType;
    private int petDesign;
    private int petDesignInitial;
    private String capyMode;
    private String actualUserName;
    private int userId;
    private String profilePicturePath;
    private int ResId;
    private ArrayList<FriendData> friendList;
    private ArrayList<FriendRequest> friendReqList;
    private ArrayList<InventoryItem> inventoryList;
    private String topLeft;
    private String topRight;
    private String topMiddle;


    public UserData(int userId, String username, String password, ArrayList<Task> taskList, String lastLogInDate,
                    int streak, int currency, String loggedInTdy, String petType, int petDesign,
                    ArrayList<FriendData> friendList, ArrayList<FriendRequest> friendReqList, ArrayList<InventoryItem> inventoryList,
                    String topLeft, String topRight, String topMiddle, int petDesignInitial, String capyMode){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.taskList = taskList;
        this.lastLogInDate = lastLogInDate;
        this.streak = streak;
        this.currency = currency;
        this.loggedInTdy = loggedInTdy;
        this.petType = petType;
        this.petDesign = petDesign;
        this.friendList = friendList;
        this.friendReqList = friendReqList;
        this.inventoryList = inventoryList;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.topMiddle = topMiddle;
        this.petDesignInitial = petDesignInitial;
        this.capyMode = capyMode;
    }

    public UserData(){
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeList(taskList);
        dest.writeString(lastLogInDate);
        dest.writeInt(streak);
        dest.writeInt(currency);
        dest.writeString(loggedInTdy);
        dest.writeString(petType);
        dest.writeInt(petDesign);
        dest.writeString(topLeft);
        dest.writeString(topRight);
        dest.writeString(topMiddle);
        dest.writeInt(petDesignInitial);
        dest.writeString(capyMode);
    }
    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }
        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
    private UserData(Parcel in) {
        userId = in.readInt();
        username = in.readString();
        password = in.readString();
        taskList = new ArrayList<>();
        in.readList(taskList, Task.class.getClassLoader());
        lastLogInDate = in.readString();
        streak = in.readInt();
        currency = in.readInt();
        loggedInTdy = in.readString();
        petType = in.readString();
        petDesign = in.readInt();
        topLeft = in.readString();
        topRight = in.readString();
        topMiddle = in.readString();
        petDesignInitial = in.readInt();
        capyMode = in.readString();
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public void updateUsername(String newUsername){ this.username = newUsername; }
    public void updatePassword(String newPassword){ this.password = newPassword;}
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public ArrayList<Task> getTaskList() { return taskList; }
    public void setTaskList(ArrayList<Task> taskList) { this.taskList = taskList; }
    public String getLastLogInDate(){return lastLogInDate;}
    public void setLastLogInDate(String lastLogInDate){this.lastLogInDate = lastLogInDate;}
    public int getStreak(){return streak;}
    public void setStreak(int streak){this.streak = streak;}
    public int getCurrency(){return currency;}
    public void setCurrency(int currency){this.currency = currency;}
    public String getLoggedInTdy(){return loggedInTdy;}
    public void setLoggedInTdy(String loggedInTdy){this.loggedInTdy = loggedInTdy;}
    public String getPetType(){ return petType; }
    public void setPetType(String petType){ this.petType = petType; }
    public int getPetDesign(){ return petDesign; }
    public void setPetDesign(int petDesign) { this.petDesign = petDesign; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }
    public String getProfilePicturePath() { return profilePicturePath; }
    public ArrayList<FriendData> getFriendList() {return this.friendList; }
    public void setFriendList(ArrayList<FriendData> friendList) { this.friendList = friendList; }
    public ArrayList<FriendRequest> getFriendReqList() {return friendReqList;}
    public void setFriendReqList(ArrayList<FriendRequest> friendReqList) {this.friendReqList = friendReqList;}
    public ArrayList<InventoryItem> getInventoryList() { return inventoryList; }
    public void setInventoryList(ArrayList<InventoryItem> inventoryList) { this.inventoryList = inventoryList; }
    public void setTopLeft(String topLeft){ this.topLeft = topLeft; }
    public void setTopRight(String topRight){ this.topRight = topRight; }
    public void setTopMiddle(String topMiddle){ this.topMiddle = topMiddle; }
    public String getTopLeft(){ return topLeft; }
    public String getTopRight(){ return topRight; }
    public String getTopMiddle(){ return topMiddle; }
    public int getPetDesignInitial(){ return petDesignInitial; }
    public void setPetDesignInitial(int petDesignInitial) { this.petDesignInitial = petDesignInitial; }
    public String getCapyMode(){ return capyMode; }
    public void setCapyMode(String capyMode){ this.capyMode = capyMode; }
}
