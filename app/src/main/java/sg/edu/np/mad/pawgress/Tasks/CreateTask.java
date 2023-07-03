package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.pawgress.CreateAccount;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class CreateTask extends AppCompatActivity {
    UserData user;
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    String title = "Create Task";
    int hr,min,sec;
    DatePicker datePicker;
    String dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        Log.i(title, "Creating task");

        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        Button createButton = findViewById(R.id.button6);
        Button cancelButton = findViewById(R.id.button5);

        NumberPicker ethr = (NumberPicker) findViewById(R.id.hourPicker);
        ethr.setMaxValue(999);
        ethr.setMinValue(0);
        ethr.setValue(0);
        ethr.setWrapSelectorWheel(true);

        NumberPicker etmin = (NumberPicker) findViewById(R.id.minPicker);
        etmin.setMaxValue(999);
        etmin.setMinValue(0);
        etmin.setValue(0);
        etmin.setWrapSelectorWheel(true);

        NumberPicker etsec = (NumberPicker) findViewById(R.id.secPicker);
        etsec.setMaxValue(999);
        etsec.setMinValue(0);
        etsec.setValue(0);
        etsec.setWrapSelectorWheel(true);

        /*DatePicker datePicker = findViewById(R.id.datePicker1);
        String day = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth());
        if (day.length() != 2){
            day = "0" + day;
        }
        if (month.length()!=2){
            month = "0" + month;
        }
        dueDate = day+month+datePicker.getYear();*/

        ethr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hr = i1;
            }
        });

        etmin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = i1;
            }
        });

        etsec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                sec = i1;
            }
        });
        Button pickDateBtn = findViewById(R.id.btnPickDate);
        TextView selectedDate = findViewById(R.id.selectedDate);

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTask.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day1 = String.valueOf(dayOfMonth);
                                String month1 =String.valueOf(monthOfYear + 1);
                                if (day1.length() != 2){
                                    day1 = "0" +day1;
                                }
                                if (month1.length() != 2){
                                    month1 = "0" + month1;
                                }
                                selectedDate.setText(day1 + "/" + month1 + "/" + year);
                                dueDate = day1+month1+year;
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etname = findViewById(R.id.editTitle);
                String name = etname.getText().toString();
                EditText etcat = findViewById(R.id.editCat);
                // do not accept blank task title or category
                int totalSeconds = (hr * 3600) + (min * 60) + sec;
                if (etname.length() > 0 && etname.getText().charAt(0) != ' ') {
                    String cat = etcat.getText().toString();
                    if (cat.length()==0 || etcat.getText().charAt(0) == ' '){
                        cat = "Uncategorised";
                    }

                    Task task = new Task(1, name, "In Progress", cat ,0, totalSeconds, dueDate);
                    Log.v(title,"dueDate "  + dueDate);
                    myDBHandler.addTask(task, user);
                    Intent newTask = new Intent(CreateTask.this, MainMainMain.class);
                    newTask.putExtra("New Task List", user);
                    newTask.putExtra("User", user);
                    newTask.putExtra("tab", "tasks_tab");
                    startActivity(newTask);
                    Log.i(title, "task added");
                    Log.i(title, String.valueOf(totalSeconds));
                    finish();
                }
                else{
                    Toast.makeText(CreateTask.this, "Invalid Title & Category", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask(user);
            }
        });
    }
    private void cancelTask(UserData user){
        Log.i(title, "discarding");
        Intent back = new Intent(CreateTask.this, MainMainMain.class);
        back.putExtra("New Task List", user);
        back.putExtra("User", user);
        back.putExtra("tab", "tasks_tab");
        startActivity(back);
        finish();
    }

    @Override
    public void onBackPressed(){
        cancelTask(user);
    }

}