package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.R;

public class EditTask extends AppCompatActivity {

    String Edit = "EditTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Log.w(Edit, "In edit mode");
        Intent receivingEnd = getIntent();
        int index = receivingEnd.getIntExtra("Task Index", 0);
        ArrayList<Task> taskList = receivingEnd.getParcelableArrayListExtra("Task List");
        Log.v(Edit, "Task Index = " + index);
        EditText etname = findViewById(R.id.editTextText);
        EditText etcat = findViewById(R.id.editTextText2);

        Button confirmButton = findViewById(R.id.button);
        Button cancelButton = findViewById(R.id.button2);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Edit, "Editing Task");
                taskList.get(index).setTaskName(etname.getText().toString());
                taskList.get(index).setCategory(etcat.getText().toString());
                Intent etTask = new Intent(EditTask.this, TaskList.class);
                etTask.putParcelableArrayListExtra("New Task List", taskList);
                startActivity(etTask);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Edit, "discarding");
                Intent back = new Intent(EditTask.this, TaskList.class);
                back.putParcelableArrayListExtra("New Task List", taskList);
                startActivity(back);
                finish();
            }
        });

    }
}