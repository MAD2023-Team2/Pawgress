package sg.edu.np.mad.pawgress.Tasks;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Date;

public class Task implements Parcelable {
    private int dailyChallenge;
    private int taskID;
    private String taskName;
    private String status;
    private String category;
    private int timeSpent;
    private int targetSec;
    private String dueDate;
    private String dateCreated;
    private String dateComplete;


    public Task() {
    }

    // add in date of creation and duedate(can accept null)
    public Task(int taskID, String taskName, String status, String category, int timeSpent, int targetSec, String dueDate, String dateCreated, String dateComplete, int dailyChallenge) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.status = status;
        this.category = category;
        this.timeSpent = timeSpent;
        this.targetSec = targetSec;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.dateComplete = dateComplete;
        this.dailyChallenge = dailyChallenge;
    }

    protected Task(Parcel in) {
        taskID = in.readInt();
        taskName = in.readString();
        status = in.readString();
        category = in.readString();
        timeSpent = in.readInt();
        targetSec = in.readInt();
        dueDate = in.readString();
        dateCreated = in.readString();
        dateComplete = in.readString();
        dailyChallenge = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(taskID);
        dest.writeString(taskName);
        dest.writeString(status);
        dest.writeString(category);
        dest.writeInt(timeSpent);
        dest.writeInt(targetSec);
        dest.writeString(dueDate);
        dest.writeString(dateCreated);
        dest.writeString(dateComplete);
        dest.writeInt(dailyChallenge);
    }
    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) { this.taskID = taskID; }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public int getTargetSec() { return targetSec; }
    public void setTargetSec(int targetSec) { this.targetSec = targetSec; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
    public String getDateComplete() { return dateComplete; }
    public void setDateComplete(String dateComplete) { this.dateComplete = dateComplete; }
    public int getDailyChallenge() { return dailyChallenge; }
    public void setDailyChallenge(int dailyChallenge) { this.dailyChallenge = dailyChallenge;}
}