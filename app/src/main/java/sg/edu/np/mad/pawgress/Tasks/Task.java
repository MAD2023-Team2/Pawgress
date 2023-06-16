package sg.edu.np.mad.pawgress.Tasks;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Task implements Parcelable {
    private int taskID;
    private String taskName;
    private LocalDateTime creationDate;
    private LocalDateTime dueDate;
    private String status;
    private String category;

    public Task() {
    }

    // add in date of creation and duedate(can accept null)
    public Task(String taskName, String status, String category) {
        this.taskName = taskName;
        this.status = status;
        this.category = category;
    }

    protected Task(Parcel in) {
        taskName = in.readString();
        status = in.readString();
        category = in.readString();
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
        dest.writeString(taskName);
        dest.writeString(status);
        dest.writeString(category);
    }

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
}