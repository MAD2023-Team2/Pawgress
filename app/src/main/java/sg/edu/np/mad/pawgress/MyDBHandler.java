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
    public static String COLUMN_USERID = "UserId";
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
    public static String COLUMN_TASK_TIMESPENT = "TimeSpent";
    public static String COLUMN_TASK_DUEDATE = "DueDate";
    public static String COLUMN_TASK_DATECREATED = "DateCreated";
    public static String COLUMN_TASK_DATECOMPLETED = "DateCompleted";
    public static String COLUMN_PET_TYPE = "PetType";
    public static String COLUMN_PET_DESIGN = "PetDesign";
    public static String COLUMN_TARGET_SEC = "TargetSec";
    public static String COLUMN_DAILY_CHALLENGE = "DailyChallenge";
    public static String COLUMN_TASK_PRIORITY = "Priority";
    public static String FRIENDS = "Friends";
    public static String COLUMN_FRIENDNAME = "FriendName";
    public static String COLUMN_FRIEND_STATUS = "FriendStatus";
    public static String FRIENDREQUEST = "FriendRequest";
    public static String COLUMN_FRIENDREQ_NAME = "FriendReqName";
    public static String COLUMN_FRIENDREQ_STATUS = "FriendReqStatus";




    //public static String COLUMN_ACTUAL_USERNAME = "ActualUserName";

    public ArrayList<Task> taskList = new ArrayList<>();

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // One table for accounts (users)
        String CREATE_ACCOUNT_TABLE = "Create TABLE " + ACCOUNTS + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STREAK + " INTEGER," +
                COLUMN_CURRENCY + " INTEGER," +
                COLUMN_LOGIN + " TEXT," +
                COLUMN_PET_TYPE + " TEXT," +
                COLUMN_PET_DESIGN + " INTEGER)";
        db.execSQL(CREATE_ACCOUNT_TABLE);
        Log.i(title, CREATE_ACCOUNT_TABLE);

        // One table for tasks, with username from user table as foreign key
        String CREATE_TASK_TABLE = "Create TABLE " + TASKS + "(" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TASK_NAME + " TEXT," +
                COLUMN_TASK_STATUS + " TEXT," +
                COLUMN_TASK_CATEGORY + " TEXT," +
                COLUMN_TASK_TIMESPENT + " INTEGER," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_TARGET_SEC + " INTERGER," +
                COLUMN_TASK_DUEDATE + " TEXT," +
                COLUMN_TASK_DATECREATED + " TEXT," +
                COLUMN_TASK_DATECOMPLETED + " TEXT," +
                COLUMN_DAILY_CHALLENGE + " INTEGER," +
                COLUMN_TASK_PRIORITY + " INTEGER)";

        db.execSQL(CREATE_TASK_TABLE);
        Log.i(title, CREATE_TASK_TABLE);

        String CREATE_FRIENDS_TABLE = "Create TABLE " + FRIENDS + "(" +
                COLUMN_FRIENDNAME + " TEXT," +
                COLUMN_FRIEND_STATUS + " TEXT," +
                COLUMN_USERNAME + " TEXT)";

        db.execSQL(CREATE_FRIENDS_TABLE);
        Log.i(title, CREATE_FRIENDS_TABLE);

        String CREATE_FRIENDREQUEST_TABLE = "Create TABLE " + FRIENDREQUEST + "(" +
                COLUMN_FRIENDREQ_NAME + " TEXT," +
                COLUMN_FRIENDREQ_STATUS + " TEXT," +
                COLUMN_USERNAME + " TEXT)";

        db.execSQL(CREATE_FRIENDREQUEST_TABLE);
        Log.i(title, CREATE_FRIENDREQUEST_TABLE);
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
        values.put(COLUMN_PET_TYPE, userData.getPetType());
        values.put(COLUMN_PET_DESIGN, userData.getPetDesign());

        SQLiteDatabase db = this. getWritableDatabase();
        db.insert(ACCOUNTS, null, values);
        Log.i(title, " Inserted/Created user" + values);
//        db.close();
    }
    public void addTask(Task task, UserData userData){ // used for creating task

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_CATEGORY, task.getCategory());
        values.put(COLUMN_TASK_TIMESPENT, task.getTimeSpent());
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_TARGET_SEC, task.getTargetSec());
        values.put(COLUMN_TASK_DUEDATE, task.getDueDate());
        values.put(COLUMN_TASK_DATECREATED, task.getDateCreated());
        values.put(COLUMN_TASK_DATECOMPLETED, task.getDateComplete());
        values.put(COLUMN_DAILY_CHALLENGE, task.getDailyChallenge());
        values.put(COLUMN_TASK_PRIORITY, task.getPriority());
        db.insert(TASKS, null, values);
        taskList.add(task); // adds new task into task list
        userData.setTaskList(taskList); // new task list assigned to user that was passed in
        Log.i(title, "Inserted Task");
//        db.close();
    }
    public void updateTask(Task task, String username){ // used for editing/completing/deleting task
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_STATUS, task.getStatus());
        values.put(COLUMN_TASK_CATEGORY, task.getCategory());
        values.put(COLUMN_TASK_TIMESPENT, task.getTimeSpent());
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_TARGET_SEC, task.getTargetSec());
        values.put(COLUMN_TASK_DUEDATE, task.getDueDate());
        values.put(COLUMN_TASK_DATECREATED, task.getDateCreated());
        values.put(COLUMN_TASK_DATECOMPLETED, task.getDateComplete());
        values.put(COLUMN_DAILY_CHALLENGE, task.getDailyChallenge());
        values.put(COLUMN_TASK_PRIORITY, task.getPriority());

        db.update(TASKS, values, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(task.getTaskID())});
        Log.i(title, "Updated Task");
//        db.close();
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
//        db.close();
    }

    public void savePetDesign(String username, String petType, int petDesign) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_TYPE, petType);
        values.put(COLUMN_PET_DESIGN, petDesign);
        db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{username});
        Log.i(title, "User pet updated" + petType);
//        db.close();
    }

    public UserData findUser(String username){
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + "=\'" + username + "\'";
        Log.i(title, "Query :" + query);

        UserData queryResult = new UserData();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.i(title, "Cursor");
        if (cursor.moveToFirst()){
            queryResult.setUserId(cursor.getInt(0));
            queryResult.setUsername(cursor.getString(1));
            queryResult.setPassword(cursor.getString(2));
            queryResult.setTaskList(taskList);
            queryResult.setLastLogInDate(cursor.getString(3));
            queryResult.setStreak(cursor.getInt(4));
            queryResult.setCurrency(cursor.getInt(5));
            queryResult.setLoggedInTdy(cursor.getString(6));
            queryResult.setPetType(cursor.getString(7));
            queryResult.setPetDesign(cursor.getInt(8));
            cursor.close();
        }
        else{
            Log.i(title, "Null");
            queryResult = null;
        }
//        db.close();
        return queryResult;
    }

    public Task findTask(int id, ArrayList<Task> newTaskList){ // uses task id to find
        for (Task task : newTaskList){
            if (task.getTaskID() == id){
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
        if (cursor.moveToFirst()){ // goes to first row if not null
            queryResult.setTaskID(cursor.getInt(0));
            queryResult.setTaskName(cursor.getString(1));
            queryResult.setStatus(cursor.getString(2));
            queryResult.setCategory(cursor.getString(3));
            queryResult.setTimeSpent(cursor.getInt(4));
            queryResult.setTargetSec(cursor.getInt(6));
            queryResult.setDueDate(cursor.getString(7));
            queryResult.setDateCreated(cursor.getString(8));
            queryResult.setDateComplete(cursor.getString(9));
            queryResult.setDailyChallenge(cursor.getInt(10));
            queryResult.setPriority(cursor.getInt(11));
            NewtaskList.add(queryResult);
            while (cursor.moveToNext()) { // goes to 2nd row and continues all the way till end
                Task task = new Task();
                task.setTaskID(cursor.getInt(0));
                task.setTaskName(cursor.getString(1));
                task.setStatus(cursor.getString(2));
                task.setCategory(cursor.getString(3));
                task.setTimeSpent(cursor.getInt(4));
                task.setTargetSec(cursor.getInt(6));
                task.setDueDate(cursor.getString(7));
                task.setDateCreated(cursor.getString(8));
                task.setDateComplete(cursor.getString(9));
                task.setDailyChallenge(cursor.getInt(10));
                task.setPriority(cursor.getInt(11));
                NewtaskList.add(task);
            }
            cursor.close();
            userData.setTaskList(NewtaskList);
        }
        else{
            userData.setTaskList(NewtaskList);
        }
//        db.close();
        return userData.getTaskList();
    }

    // updates user data accordingly for edit profile (username and password)
    public void updateUser(String username, String newPassword, String oldUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // new username and password are added to the ContentValues object
        values.put(COLUMN_PASSWORD, newPassword);
        values.put(COLUMN_USERNAME, username);
        // Only perform the update if there are changes to be made (values size > 0)
        if (values.size() > 0) {
            System.out.println("Here");
            db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{oldUserName});
            Log.i(title, "User updated: " + username);
        } else {
            Log.i(title, "No changes to update for user: " + username);
        }

//        db.close();
    }


    public String findUsername(String newUsername) {
        String currentUsername = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_USERNAME}; // Specify the column you want to retrieve

        //queries the database and retrieves the username from the cursor if it exists
        Cursor cursor = db.query(ACCOUNTS, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_USERNAME);

            if (columnIndex != -1) {
                currentUsername = cursor.getString(columnIndex);
            }

            cursor.close();
        }

//        db.close();

        // closes the cursor and database before returning the retrieved username
        return currentUsername;
    }

    //likewise, finds existing password of users and i intend to display it when they open edit profile
    // retrieves a password from the database given a username
    public String findPassword(String username) {
        String currentPassword = null;

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};

        // queries the database with a selection argument to retrieve the password associated with the given username
        Cursor cursor = db.query(ACCOUNTS, projection, selection, selectionArgs, null, null, null);

        //  retrieves the password from the cursor if it exists
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);

            if (columnIndex != -1) {
                currentPassword = cursor.getString(columnIndex);
            }

            cursor.close();
        }

//        db.close();

        // closes the cursor and database before returning the retrieved password
        return currentPassword;
    }
    public int getTaskTargetSec(int taskId){
        String query = "SELECT " + COLUMN_TARGET_SEC + " FROM " + TASKS + " WHERE " + COLUMN_TASK_ID + " = " + taskId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int targetSec = -1; // Default value if the task is not found
        if (cursor != null && cursor.moveToFirst()) {
            int targetSecIndex = cursor.getColumnIndex(COLUMN_TARGET_SEC);
            targetSec = cursor.getInt(targetSecIndex);

            cursor.close();
        }
//        db.close();

        return targetSec;
    }
    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public ArrayList<FriendData> findFriendList(UserData userData){
        String query = "SELECT * FROM " + FRIENDS + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        Log.i(title, "Query :" + query);
        ArrayList<FriendData> friendList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        FriendData queryResult = new FriendData();
        if (cursor.moveToFirst()){ // goes to first row if not null
            queryResult.setFriendName(cursor.getString(0));
            queryResult.setStatus(cursor.getString(1));
            friendList.add(queryResult);
            while (cursor.moveToNext()) { // goes to 2nd row and continues all the way till end
                FriendData friend = new FriendData();
                friend.setFriendName(cursor.getString(0));
                friend.setStatus(cursor.getString(1));
                friendList.add(friend);
            }
            cursor.close();
            userData.setFriendList(friendList);
        }
        else{
            userData.setFriendList(friendList);
        }
//        db.close();
        return userData.getFriendList();
    }

    public void addFriend(String friendName, UserData userData, String status){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIENDNAME, friendName);
        values.put(COLUMN_FRIEND_STATUS, status);
        values.put(COLUMN_USERNAME, userData.getUsername());

        db.insert(FRIENDS, null, values);

        Log.i(title, "Inserted Friend");
//        db.close();
    }

    public void removeFriend(String friendName, UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FRIENDNAME, friendName);
        values.put(COLUMN_FRIEND_STATUS, "Unfriended");
        values.put(COLUMN_USERNAME, userData.getUsername());


        db.update(FRIENDS, values,COLUMN_FRIENDNAME + "='" + friendName + "' AND " + COLUMN_USERNAME + "='" + userData.getUsername() + "'", null);

        Log.i(title, "Friend Status updated");


//        db.close();
    }



    public ArrayList<FriendRequest> findFriendReqList(UserData userData){
        String query = "SELECT * FROM " + FRIENDREQUEST + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        Log.i(title, "Query :" + query);
        ArrayList<FriendRequest> friendReqList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        FriendRequest queryResult = new FriendRequest();
        if (cursor.moveToFirst()){ // goes to first row if not null
            queryResult.setFriendReqName(cursor.getString(0));
            queryResult.setReqStatus(cursor.getString(1));
            friendReqList.add(queryResult);
            while (cursor.moveToNext()) { // goes to 2nd row and continues all the way till end
                FriendRequest friend = new FriendRequest();
                friend.setFriendReqName(cursor.getString(0));
                friend.setReqStatus(cursor.getString(1));
                friendReqList.add(friend);
            }
            cursor.close();
            userData.setFriendReqList(friendReqList);
        }
        else{
            userData.setFriendReqList(friendReqList);
        }
//        db.close();
        return userData.getFriendReqList();
    }

    public void addFriendReq(String friendReqName, UserData userData, String reqStatus){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIENDREQ_NAME, friendReqName);
        values.put(COLUMN_FRIENDREQ_STATUS, reqStatus);
        values.put(COLUMN_USERNAME, userData.getUsername());

        db.insert(FRIENDREQUEST, null, values);

        Log.i(title, "Inserted Friend Request");
//        db.close();
    }

    public void removeFriendReq(String friendReqName, UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        /*
        values.put(COLUMN_FRIENDREQ_NAME, friendReqName);
        values.put(COLUMN_FRIENDREQ_STATUS, "Rejected");
        values.put(COLUMN_USERNAME, userData.getUsername());
        */
        db.delete(FRIENDREQUEST, COLUMN_FRIENDREQ_NAME + "='" + friendReqName + "' AND " + COLUMN_USERNAME + "='" + userData.getUsername() + "'", null);
        //db.update(FRIENDREQUEST, values,COLUMN_FRIENDREQ_NAME + "='" + friendReqName + "' AND " + COLUMN_USERNAME + "='" + userData.getUsername() + "'", null);

        Log.i(title, "Friend Request removed");
//        db.close();
    }
    public void updateCurrency(String username, int currency){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CURRENCY, currency);

        db.update(ACCOUNTS, values,COLUMN_USERNAME + "=?", new String[]{username});

        Log.i(title, "Currency has been updated");
    }

    public void updatePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);

        db.update(ACCOUNTS, values,COLUMN_USERNAME + "=?", new String[]{username});

        Log.i(title, "Password has been updated");
    }

    public void updateUsername(String username, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newName);

        db.update(ACCOUNTS, values,COLUMN_USERNAME + "=?", new String[]{username});

        Log.i(title, "Username has been updated");
    }

}
