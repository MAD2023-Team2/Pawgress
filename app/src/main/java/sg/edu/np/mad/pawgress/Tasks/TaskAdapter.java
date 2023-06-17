package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> taskList;
    Context context;
    String THIS = "Adapter";
    UserData user;
    MyDBHandler mDataBase;
    public TaskAdapter(UserData userData, MyDBHandler mDatabase, Context context){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateEmptyView(){
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            for (Task task : taskList){
                if(task.getStatus().equals("In Progress")){
                    count+=1;
                }
            }
        }
        Log.v(THIS, "Count " + count);
        if (count > 0){ emptyTasktext.setVisibility(INVISIBLE); }
        else emptyTasktext.setVisibility(VISIBLE);
    }

    @Override
    public int getItemViewType(int position)
    {
        Task task = taskList.get(position);
        if (taskList.size() == 0) { return 0; }
        else if (task.getStatus().equals("In Progress")) return 1;
        else return 2; // completed tasks (saved in list/database but wont be shown)
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.v(THIS, "View Type " + viewType);
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Log.i(THIS, "onbind");
        if (holder.getItemViewType() == 1){
            Task task = taskList.get(position);
            int id = task.getTaskID();
            Task finalTask = mDataBase.findTask(id, mDataBase.findTaskList(user));
            holder.category.setText(task.getCategory());
            holder.name.setText(task.getTaskName());
            // view individual task
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    Bundle info = new Bundle();
                    info.putParcelable("User", user);
                    info.putParcelable("Task", finalTask); // send individual task
                    Log.v(THIS, "ID " + id);
                    viewTask.putExtras(info);
                    context.startActivity(viewTask);
                }
            });
            // edit individual task
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editTask = new Intent(context, EditTask.class);
                    Bundle info = new Bundle();
                    Log.v(THIS, "id " + id);
                    info.putParcelable("User", user);
                    info.putParcelable("Task", finalTask); // send id of task to edit this task only
                    editTask.putExtras(info);
                    context.startActivity(editTask);
                }
            });
            // delete individual task
            // same problem as complete task, use "Deleted" Status
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Deleting Task: " + task.getTaskName());
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            Log.v(THIS, "Deleting task");
                            finalTask.setStatus("Deleted");
                            mDataBase.updateTask(finalTask);
                            taskList.remove(task);
                            user.setTaskList(taskList);
                            notifyItemRemoved(taskList.indexOf(task));
                            notifyItemRangeChanged(taskList.indexOf(task), taskList.size());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            // complete task
            // problem: deletes task from list, if its saved into db before then its ok(remove taskStatus and conditions), if not then use "Completed" Status
            holder.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Complete: " + task.getTaskName());
                    builder.setMessage("Is it really completed?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            Log.v(THIS, "Completed task");
                            finalTask.setStatus("Completed");
                            mDataBase.updateTask(finalTask);
                            taskList.remove(task);
                            user.setTaskList(taskList);
                            notifyItemRemoved(taskList.indexOf(task));
                            notifyItemRangeChanged(taskList.indexOf(task), taskList.size());
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
        }
        else {
            holder.card.setVisibility(INVISIBLE);
            holder.card.setMaxHeight(0);
            holder.card.setMaxWidth(0);
        }
    }
}
