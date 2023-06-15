package sg.edu.np.mad.pawgress.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>{
    ArrayList<Task> taskList;
    Context context;
    String THIS = "Adapter";
    public TaskAdapter(ArrayList<Task> taskList, Context context){
        this.taskList = taskList;
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        Log.i(THIS, "Get Item View Type");
        Task task = taskList.get(position);
        if (taskList.size() == 0) { return 0; }
        else if (task.getStatus().equals("In Progress")) return 1;
        else return 2; // completed tasks (saved in list/database but wont be shown)
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.v(THIS, "View Type " + viewType);
        if (viewType == 1){
            Log.w(THIS, "Tasks present");
            return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent, false));
        }
        else return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_none,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Log.i(THIS, "onbind");
        if (holder.getItemViewType() == 1){
            Task task = taskList.get(position);
            holder.category.setText(task.getCategory());
            holder.name.setText(task.getTaskName());
            // view individual task
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    viewTask.putExtra("Task", task);
                    context.startActivity(viewTask);
                }
            });
            // edit individual task
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editTask = new Intent(context, EditTask.class);
                    Bundle info = new Bundle();
                    info.putParcelableArrayList("Task List", taskList);
                    info.putInt("Task Index", taskList.indexOf(task)); // send position of task to edit this task only
                    editTask.putExtras(info);
                    Log.v(THIS, "TaskID = " + task.getTaskID());
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
                            taskList.remove(taskList.get(taskList.indexOf(task)));
                            Log.i(THIS, "Task List new size = " + taskList.size());
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
                            taskList.remove(taskList.get(taskList.indexOf(task)));
                            Log.i(THIS, "Task List new size = " + taskList.size());
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
        else holder.none.setText("No tasks to work on for now :)"); //this shit works only when i don't want it to ;(
    }
}
