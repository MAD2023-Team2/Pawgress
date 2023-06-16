package sg.edu.np.mad.pawgress;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Tasks.Task;

public class UserData implements Parcelable{
    private String username;
    private String password;
    private ArrayList<Task> taskList;

    public UserData(String username, String password, ArrayList<Task> taskList) {
        this.username = username;
        this.password = password;
        this.taskList = taskList;
    }

    public UserData(){
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeList(taskList);
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
        username = in.readString();
        password = in.readString();
        taskList = new ArrayList<>();
        in.readList(taskList, Task.class.getClassLoader());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Task> getTaskList() { return taskList; }

    public void setTaskList(ArrayList<Task> taskList) { this.taskList = taskList; }
}
