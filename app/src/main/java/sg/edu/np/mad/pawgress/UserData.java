package sg.edu.np.mad.pawgress;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Tasks.Task;

public class UserData {
    private String username;
    private String password;

    private ArrayList<Task> taskList;

    public UserData(String username, String password/*, ArrayList<Task> taskList*/) {
        this.username = username;
        this.password = password;
        //this.taskList = taskList;
    }

    public UserData(){
    };

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

    //public ArrayList<Task> getTaskList() { return taskList; }

    // public void setTaskList(ArrayList<Task> taskList) { this.taskList = taskList; }
}
