package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;
// Parent reyclerview - shows the categories of the tasks there are (daily challenges + prioritised tasks included)
public class ParentTaskAdapter extends RecyclerView.Adapter<ParentTaskViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> taskList, recyclerTaskList;
    ArrayList<String> categoryList, categories;
    Context context;
    String THIS = "Adapter";
    UserData user;
    MyDBHandler mDataBase;
    TasksFragment fragment;
    public ParentTaskAdapter(UserData userData, MyDBHandler mDatabase, Context context, TasksFragment fragment, ArrayList<String> categories){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
        this.fragment = fragment;
        this.categories = categories;

    }
    @NonNull
    @Override
    public int getItemViewType(int position){
        String category = categoryList.get(position);
        return 1;
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // shows that there are currently no tasks to work on if there are no tasks in progress found in the database for this user
    public void updateEmptyView(){
        // add the categories to a list for display
        categoryList = new ArrayList<>();
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            // loops through all tasks in the taskList saved to the user in database
            for (Task task : taskList){
                // if the category list does not contain the task's category and the task is in progress
                if (!categoryList.contains(task.getCategory()) && task.getStatus().equals("In Progress") && task.getCategory().equals("Daily Challenge"))
                {
                    // if the task is a daily challenge, it adds this into the list first
                    // so that daily challenge will always be on top
                    categoryList.add(task.getCategory());
                    count+=1;
                }
            }
            for (Task task : taskList){
                if (!categoryList.contains("Prioritised Tasks") && !categoryList.contains(task.getCategory()) && task.getStatus().equals("In Progress") && task.getPriority() == 1 ){
                    // followed by prioritised tasks if there are
                    categoryList.add("Prioritised Tasks");
                    count+=1;
                }
            }
            // this is for the remaining task categories where it is not daily challenge or prioritised
            for (Task task : taskList){
                if (!categoryList.contains(task.getCategory()) && task.getStatus().equals("In Progress") && task.getPriority()!=1) {
                        categoryList.add(task.getCategory());
                        count+=1;
                }
            }
        }
        if (count > 0){ emptyTasktext.setVisibility(INVISIBLE); }
        else emptyTasktext.setVisibility(VISIBLE);
    }

    public void updateFilteredView(){
        categoryList = new ArrayList<>();
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            for (Task task:taskList){
                if (!categoryList.contains(task.getCategory()) && task.getStatus().equals("In Progress") && task.getDailyChallenge()==1){
                    categoryList.add("Daily Challenge");
                }
            }
            for (String s:categories){
                categoryList.add(s);
            }
        }
    }

    @Override
    public ParentTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new ParentTaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ParentTaskViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.category.setText(category);
        // creates the child adapter that will show the tasks that belong to each category
        ChildTaskAdapter mAdapter = new ChildTaskAdapter(user, mDataBase, context, category, fragment);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mAdapter.updateList();
        holder.childList.setLayoutManager(mLayoutManager);
        holder.childList.setAdapter(mAdapter);
    }
}
