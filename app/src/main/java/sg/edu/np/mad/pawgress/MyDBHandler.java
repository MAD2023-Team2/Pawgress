package sg.edu.np.mad.pawgress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static String TASKS = "Tasks";
    public static String COLUMN_TASK_ID = "TaskID";
    public static String COLUMN_TASK_NAME = "TaskName";
    public static String COLUMN_TASK_STATUS = "TaskStatus";
    public static String COLUMN_TASK_CATEGORY = "TaskCategory";
    public ArrayList<Task> taskList = new ArrayList<>();

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ACCOUNT_TABLE = "Create TABLE " + ACCOUNTS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD + " TEXT)";
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
            cursor.close();
        }
        else{
            Log.i(title, "Null");
            queryResult = null;
        }
        db.close();
        return queryResult;
    }
    public ArrayList<Task> findTaskList(UserData userData){
        String query = "SELECT * FROM " + TASKS + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        Log.i(title, "Query :" + query);

        Task queryResult = new Task();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            queryResult.setTaskName(cursor.getString(1));
            queryResult.setStatus(cursor.getString(2));
            queryResult.setCategory(cursor.getString(3));
            taskList.add(queryResult);
            while (cursor.moveToNext()) {
                Task task = new Task();
                task.setTaskName(cursor.getString(1));
                task.setStatus(cursor.getString(2));
                task.setCategory(cursor.getString(3));
                taskList.add(task);
            }
            cursor.close();
            userData.setTaskList(taskList);
        }
        else{
            userData.setTaskList(taskList);
        }
        db.close();
        return userData.getTaskList();
    }
}
