package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

// Child recyclerview to show the tasks in each of the different categories
public class ChildTaskAdapter extends RecyclerView.Adapter<ChildTaskViewHolder>{
    ArrayList<Task> taskList, recyclerTaskList;
    Context context;
    UserData user;
    MyDBHandler mDataBase;
    String category;
    TasksFragment fragment;
    ArrayList<String> categoryList;
    int filter;
    public ChildTaskAdapter(UserData userData, MyDBHandler mDatabase, Context context, String category, ArrayList<String> categoryList, int filter, TasksFragment fragment){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
        this.category = category;
        this.categoryList = categoryList;
        this.filter = filter;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public int getItemViewType(int position){
        Task task = recyclerTaskList.get(position);
        int dailyChallenge = task.getDailyChallenge();
        if (dailyChallenge == 1) { return 1; } // viewType 1 for daily challenge
        else { return 0;} // default viewType 0 for normal tasks
    }
    @Override
    public int getItemCount() {
        return recyclerTaskList.size();
    }

    // shows that there are currently no tasks to work on if there are no tasks in progress found in the database for this user
    public void updateList(){
        // list of the tasks that are to be shown for each category
        recyclerTaskList = new ArrayList<>();
        ArrayList<Task> removeList = new ArrayList<>();
        for (Task task : taskList){
            // if task is in progress +
            // if task category is the same as the category passed from the parent recyclerview or
            // if the task is prioritised
            // adds into list to show in recyclerView
            if(task.getStatus().equals("In Progress") && (task.getCategory().equals(category) || task.getPriority() == 1)){
                recyclerTaskList.add(task);
            }
            // removes task if task is prioritised but not under the "Prioritised Task" card from parent adapter
            if(!category.equals("Prioritised Tasks") && recyclerTaskList.size()!= 0 && !task.getCategory().equals(category) && task.getPriority()==1){
                recyclerTaskList.remove(task);
            }
        }
        // if the list has a task that is supposed to be prioritised but it is also shown under its original category
        // task added to separate list: removeList
        for (Task task : recyclerTaskList){
            if(!category.equals("Prioritised Tasks") && task.getPriority() == 1){
                removeList.add(task);
            }
        }
        // for filtered view, remove tasks that are not in the current category
        for (Task task : recyclerTaskList){
            if(filter == 1 && !categoryList.contains(task.getCategory())){
                removeList.add(task);
            }
        }
        // remove all tasks previously mentioned
        if (!removeList.isEmpty()){
            recyclerTaskList.removeAll(removeList);
        }
    }

    @Override
    public ChildTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 1){
            return new ChildTaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_daily_challenge,parent, false));
        }
        else{
            return new ChildTaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ChildTaskViewHolder holder, int position){
        Task task = recyclerTaskList.get(position);
        holder.name.setText(task.getTaskName());
        if (task.getDueDate() != null){
            holder.duedate.setText("Due on: " + task.getDueDate());
        }
        else holder.duedate.setVisibility(INVISIBLE);

        // if it is daily challenge
        if (getItemViewType(position) == 1) {
            holder.card.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(task.getColorCode().get(0))));
            holder.name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(task.getColorCode().get(1))));
            holder.duedate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(task.getColorCode().get(0))));
        }
        // if it is not daily challenge
        if (getItemViewType(position) == 0){
            // view task
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    Bundle info = new Bundle();
                    info.putParcelable("User", user);
                    info.putParcelable("Task", task); // send individual task
                    viewTask.putExtras(info);
                    context.startActivity(viewTask);
                }
            });
        }
        // complete task
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Complete: " + task.getTaskName());
                builder.setMessage("Is it really completed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, h:mm");
                        String newDayDate = formatter.format(new Date().getTime());
                        task.setStatus("Completed");
                        task.setDateComplete(newDayDate);
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);
                        // notify adapter about changes to list
                        notifyDataSetChanged();
                        if (recyclerTaskList.size() == 0) {
                            fragment.refreshTaskRecyclerView();
                        }

                        // after completing any task, gain 5 currency
                        // for every 30 minutes spent on the task, an additional 1 currency is added
                        // if timespent > target time, an additional 1 currency is added
                        int additonal_currency;
                        int task_seconds = task.getTimeSpent();
                        int task_minutes = task_seconds / 60;
                        int task_minutes_30 = task_minutes / 30;
                        int current_currency = user.getCurrency();
                        int target_sec = task.getTargetSec();
                        if (task_seconds >= target_sec){
                            additonal_currency = 1;
                        }
                        else{
                            additonal_currency = 0;
                        }
                        int new_currency = current_currency + 5 + task_minutes_30 + additonal_currency;
                        user.setCurrency(new_currency);
                        mDataBase.updateCurrency(user.getUsername(), new_currency);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        holder.complete.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.complete.setChecked(false); // ensures all checkboxes are not checked before clicking it


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        String lastInDate = user.getLastLogInDate();
        // sends user back to daily login when new day starts while app is running
        if (!lastInDate.equals(newDayDate)) {
            Log.i("ChildTaskAdapter","new day, send to dalylogin");
            Intent intent = new Intent(context, DailyLogIn.class);
            intent.putExtra("User", user);
            intent.putExtra("tab", "home_tab");
            context.startActivity(intent);
        }

        // to check for overdue tasks
        int currentDay = Integer.parseInt(newDayDate.substring(0,2));
        int currentMonth = Integer.parseInt(newDayDate.substring(3,5));
        int currentYear = Integer.parseInt(newDayDate.substring(6));
        if (task.getDueDate()!=null){
            int day = Integer.parseInt(task.getDueDate().substring(0,2));
            int month = Integer.parseInt(task.getDueDate().substring(3,5));
            int year = Integer.parseInt(task.getDueDate().substring(6));
            if (currentDay > day && currentMonth <= month && currentYear<=year){
                holder.warn.setVisibility(View.VISIBLE);
            }
            else if (currentMonth > month || currentYear > year){
                holder.warn.setVisibility(View.VISIBLE);
            }
        }
        // warning icon for tasks that are overdue, default visibility is GONE
        if (holder.warn != null){
            holder.warn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning");
                    builder.setMessage("This task is overdue. Currency will be deducted.");
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }
}
