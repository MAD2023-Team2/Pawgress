package sg.edu.np.mad.pawgress.Tasks;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import sg.edu.np.mad.pawgress.R;

public class ChildTaskViewHolder extends RecyclerView.ViewHolder {

    TextView name, duedate;
    RelativeLayout card;
    CheckBox complete;
    ImageView warn;

    public ChildTaskViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.textView4); // task name
        card = itemView.findViewById(R.id.taskCard); // individual task
        complete = itemView.findViewById(R.id.checkBox); // checkbox for task completion
        duedate = itemView.findViewById(R.id.textView21); // task due date
        warn = itemView.findViewById(R.id.warn); // indication for overdue tasks
    }
}
