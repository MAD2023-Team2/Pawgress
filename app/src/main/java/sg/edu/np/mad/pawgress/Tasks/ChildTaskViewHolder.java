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

    String title = "ViewHolder!";
    TextView name, category, duedate;
    RelativeLayout card;
    ImageButton edit,delete;
    CheckBox complete;
    RecyclerView childList;
    ImageView warn;

    public ChildTaskViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.textView4);
        card = itemView.findViewById(R.id.taskCard); // individual task
        edit = itemView.findViewById(R.id.button3);
        complete = itemView.findViewById(R.id.checkBox);
        duedate = itemView.findViewById(R.id.textView21);
        warn = itemView.findViewById(R.id.warn);
    }
}
