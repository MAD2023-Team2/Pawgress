package sg.edu.np.mad.pawgress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.np.mad.pawgress.Tasks.Task;

public class MyDBHandler extends SQLiteOpenHelper{

    String title = "MyDBhandler";

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "accountDB.db";
    public static String ACCOUNTS = "Accounts";
    public static String COLUMN_USERNAME = "UserName";
    public static String COLUMN_PASSWORD = "Password";
    public static String COLUMN_DATE = "CreateDate";
    public static String COLUMN_STREAK = "Streak";
    public static String COLUMN_CURRENCY = "Currency";
    public static String COLUMN_LOGIN = "LogInStatus";
    public static String TASKS = "Tasks";
    public static String COLUMN_TASK_ID = "TaskID";
    public static String COLUMN_TASK_NAME = "TaskName";
    public static String COLUMN_TASK_STATUS = "TaskStatus";
    public static String COLUMN_TASK_CATEGORY = "TaskCategory";
    public static String COLUMN_PET_TYPE = "PetType";
    public static String COLUMN_PET_DESIGN = "PetDesign";

    public ArrayList<Task> taskList = new ArrayList<>();

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ACCOUNT_TABLE = "Create TABLE " + ACCOUNTS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STREAK + " INTEGAR," +
                COLUMN_CURRENCY + " INTEGAR," +
                COLUMN_LOGIN + " TEXT," +
                COLUMN_PET_TYPE + " TEXT," +
                COLUMN_PET_DESIGN + " INTEGER)";
        db.execSQL(CREATE_ACCOUNT_TABLE);
        Log.i(title, CREATE_ACCOUNT_TABLE);

        String CREATE_TASK_TABLE = "Create TABLE " + TASKS + "(" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TASK_NAME + " TEXT," +
                COLUMN_TASK_STATUS + " TEXT," +
                COLUMN_TASK_CATEGORY + " TEXT," +
                COLUMN_USERNAME + " TEXT)";
        db.execSQL(CREATE_TASK_TABLE);
        Log.i(title, CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        onCreate(db);
    }

    public void addUser(UserData userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_PASSWORD, userData.getPassword());
        values.put(COLUMN_DATE, userData.getLastLogInDate());
        values.put(COLUMN_STREAK, userData.getStreak());
        values.put(COLUMN_CURRENCY, userData.getCurrency());
        values.put(COLUMN_LOGIN, userData.getLoggedInTdy());

        SQLiteDatabase db = this. getWritableDatabase();
        db.insert(ACCOUNTS, null, values);
        Log.i(title, " Inserted/Created user" + values);
        db.close();
    }
    public void addTask(Task task, UserData userData){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_CATEGORY, task.getCategory());
        values.put(COLUMN_USERNAME, userData.getUsername());
        db.insert(TASKS, null, values);
        taskList.add(task);
        Log.v(title, "taskList size " + taskList.size());
        userData.setTaskList(taskList);
        Log.i(title, "Inserted Task");
        db.close();
    }
    public void updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_CATEGORY, task.getCategory());
        Log.v(title, "Task ID/NAME" + task.getTaskID() + task.getTaskName());
        db.update(TASKS, values, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(task.getTaskID())});
        Log.i(title, "Updated Task");
        db.close();
    }

    public void updateData(String username, String logIn, int streak, int currency, String loggedIn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, logIn);
        values.put(COLUMN_STREAK, streak);
        values.put(COLUMN_CURRENCY, currency);
        values.put(COLUMN_LOGIN, loggedIn);

        db.update(ACCOUNTS, values,COLUMN_USERNAME + "=?", new String[]{username});

        Log.i(title, "Data updated");
        db.close();
    }

    public void savePetDesign(String username, String petType, int petDesign) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_TYPE, petType);
        values.put(COLUMN_PET_DESIGN, petDesign);
        db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
    }

    public UserData findUser(String username){
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + "=\'" + username + "\'";
        Log.i(title, "Query :" + query);

        UserData queryResult = new UserData();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.i(title, "Cursor");
        if (cursor.moveToFirst()){
            queryResult.setUsername(cursor.getString(0));
            queryResult.setPassword(cursor.getString(1));
            queryResult.setTaskList(taskList);
            queryResult.setLastLogInDate(cursor.getString(2));
            queryResult.setStreak(cursor.getInt(3));
            queryResult.setCurrency(cursor.getInt(4));
            queryResult.setLoggedInTdy(cursor.getString(5));
            queryResult.setPetType(cursor.getString(6));
            queryResult.setPetDesign(cursor.getInt(7));
            cursor.close();
        }
        else{
            Log.i(title, "Null");
            queryResult = null;
        }
        db.close();
        return queryResult;
    }

    public Task findTask(int id, ArrayList<Task> newTaskList){
        for (Task task : newTaskList){
            if (task.getTaskID() == id){
                Log.w(title, "Task: " + task.getTaskName());
                return task;
            }
        }
        return null;
    }
    public ArrayList<Task> findTaskList(UserData userData){
        String query = "SELECT * FROM " + TASKS + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        Log.i(title, "Query :" + query);
        ArrayList<Task> NewtaskList = new ArrayList<>();
        Task queryResult = new Task();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            queryResult.setTaskID(cursor.getInt(0));
            queryResult.setTaskName(cursor.getString(1));
            queryResult.setStatus(cursor.getString(2));
            queryResult.setCategory(cursor.getString(3));
            Log.w(title, "TaskName/TaskStatus " + queryResult.getTaskName() + queryResult.getStatus());
            NewtaskList.add(queryResult);
            while (cursor.moveToNext()) {
                Task task = new Task();
                task.setTaskID(cursor.getInt(0));
                task.setTaskName(cursor.getString(1));
                task.setStatus(cursor.getString(2));
                task.setCategory(cursor.getString(3));
                Log.w(title, "TaskName/TaskStatus " + task.getTaskName() + task.getStatus());
                NewtaskList.add(task);
            }
            cursor.close();
            userData.setTaskList(NewtaskList);
        }
        else{
            userData.setTaskList(NewtaskList);
        }
        db.close();
        return userData.getTaskList();
    }


}
