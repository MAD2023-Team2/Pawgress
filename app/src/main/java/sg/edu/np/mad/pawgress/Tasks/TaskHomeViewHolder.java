package sg.edu.np.mad.pawgress.Tasks;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class TaskHomeViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    RelativeLayout card2;
    CheckBox complete;
    ImageView urgent;

    public TaskHomeViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.tasktitlecard);
        card2 = itemView.findViewById(R.id.taskCard2);
        complete = itemView.findViewById(R.id.checkBoxcard);
        urgent = itemView.findViewById(R.id.urgent);
    }
}
