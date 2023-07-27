package sg.edu.np.mad.pawgress.Tasks;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class TaskCardViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    RelativeLayout card2;
    CheckBox complete;
    ImageView urgent;

    public TaskCardViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.tasktitlecard);
        card2 = itemView.findViewById(R.id.taskCard2);
        complete = itemView.findViewById(R.id.checkBoxcard);
        urgent = itemView.findViewById(R.id.urgent);
    }
}
