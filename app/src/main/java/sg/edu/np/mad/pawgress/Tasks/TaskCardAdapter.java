package sg.edu.np.mad.pawgress.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.R;

public class TaskCardAdapter extends RecyclerView.Adapter<TaskCardViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> taskList;
    Context context;

    public TaskCardAdapter(ArrayList<Task> taskList, Context context){
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateEmptyView() {
        if (taskList.isEmpty()){
            emptyTasktext.setVisibility(View.VISIBLE);
        }
        else{
            emptyTasktext.setVisibility(View.INVISIBLE);
        }
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
    public TaskCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 1){
            return new TaskCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.taskcard,parent, false));
        }
        else return new TaskCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.taskcard,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskCardViewHolder holder, int position){
        if (holder.getItemViewType() == 1){
            Task task = taskList.get(position);
            holder.name.setText(task.getTaskName());
            // view individual task
            holder.card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    viewTask.putExtra("Task", task);
                    context.startActivity(viewTask);
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
                            taskList.remove(taskList.get(taskList.indexOf(task)));
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
    }
}
