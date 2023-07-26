package sg.edu.np.mad.pawgress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryItem;
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
    public static String COLUMN_TASK_DATESTARTED = "DateStarted";
    public static String COLUMN_TASK_DATECOMPLETED = "DateCompleted";
    public static String COLUMN_PET_TYPE = "PetType";
    public static String COLUMN_PET_DESIGN = "PetDesign";
    public static String COLUMN_TARGET_SEC = "TargetSec";
    public static String COLUMN_DAILY_CHALLENGE = "DailyChallenge";
    public static String COLUMN_TASK_PRIORITY = "Priority";
    public static String COLUMN_PROFILE_PICTURE = "ProfilePicture";
    public static String FRIENDS = "Friends";
    public static String COLUMN_FRIENDNAME = "FriendName";
    public static String COLUMN_FRIEND_STATUS = "FriendStatus";
    public static String FRIENDREQUEST = "FriendRequest";
    public static String COLUMN_FRIENDREQ_NAME = "FriendReqName";
    public static String COLUMN_FRIENDREQ_STATUS = "FriendReqStatus";
    public static String QUOTE = "Quote";
    public static String COLUMN_QUOTE_TEXT = "QuoteText";
    public static String COLUMN_AUTHOR = "Author";
    public static String INVENTORY = "Inventory";
    public static String COLUMN_ITEM_NAME = "ItemName";
    public static String COLUMN_ITEM_QUANTITY = "Quantity";
    public static String COLUMN_ITEM_CATEGORY = "Category";
    public static String IMAGE_URL = "Image_URL";
    public static String COLUMN_IMAGE_NAME = "ImageName";
    public static String COLUMN_IMAGE_URL = "ImageURL";
    public static String COLUMN_TOP_LEFT = "TopLeft";
    public static String COLUMN_TOP_RIGHT = "TopRight";

    public ArrayList<Task> taskList = new ArrayList<>();

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // One table for accounts (users)
        String CREATE_ACCOUNT_TABLE = "Create TABLE " + ACCOUNTS + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STREAK + " INTEGER," +
                COLUMN_CURRENCY + " INTEGER," +
                COLUMN_LOGIN + " TEXT," +
                COLUMN_PET_TYPE + " TEXT," +
                COLUMN_PET_DESIGN + " INTEGER," +
                COLUMN_PROFILE_PICTURE + " TEXT," +
                COLUMN_TOP_LEFT + " TEXT," +
                COLUMN_TOP_RIGHT + " TEXT)";
        db.execSQL(CREATE_ACCOUNT_TABLE);
        Log.i(title, CREATE_ACCOUNT_TABLE);

        // One table for tasks, with username from user table as foreign key
        String CREATE_TASK_TABLE = "Create TABLE " + TASKS + "(" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TASK_NAME + " TEXT," +
                COLUMN_TASK_STATUS + " TEXT," +
                COLUMN_TASK_CATEGORY + " TEXT," +
                COLUMN_TASK_TIMESPENT + " INTEGER," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_TARGET_SEC + " INTERGER," +
                COLUMN_TASK_DUEDATE + " TEXT," +
                COLUMN_TASK_DATECREATED + " TEXT," +
                COLUMN_TASK_DATESTARTED + " TEXT, " +
                COLUMN_TASK_DATECOMPLETED + " TEXT," +
                COLUMN_DAILY_CHALLENGE + " INTEGER," +
                COLUMN_TASK_PRIORITY + " INTEGER)";

        db.execSQL(CREATE_TASK_TABLE);
        Log.i(title, CREATE_TASK_TABLE);


        String CREATE_INVENTORY_TABLE = "Create TABLE " + INVENTORY + "(" +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_ITEM_NAME + " TEXT," +
                COLUMN_ITEM_QUANTITY + " INTEGER," +
                COLUMN_ITEM_CATEGORY + " TEXT)";

        db.execSQL(CREATE_INVENTORY_TABLE);
        Log.i(title, CREATE_INVENTORY_TABLE);

        String CREATE_IMAGE_URL_TABLE = "Create TABLE " + IMAGE_URL + "(" +
                COLUMN_IMAGE_NAME + " TEXT," +
                COLUMN_IMAGE_URL + " TEXT)";

        db.execSQL(CREATE_IMAGE_URL_TABLE);
        Log.i(title, CREATE_IMAGE_URL_TABLE);

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

        String CREATE_QUOTE_TABLE = "Create TABLE " + QUOTE + "(" +
                COLUMN_QUOTE_TEXT + " TEXT," +
                COLUMN_AUTHOR + " TEXT," +
                COLUMN_USERNAME + " TEXT)";

        db.execSQL(CREATE_QUOTE_TABLE);
        Log.i(title, CREATE_QUOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_URL);
        onCreate(db);
    }

    public void addUser(UserData userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERID, userData.getUserId());
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_PASSWORD, userData.getPassword());
        values.put(COLUMN_DATE, userData.getLastLogInDate());
        values.put(COLUMN_STREAK, userData.getStreak());
        values.put(COLUMN_CURRENCY, userData.getCurrency());
        values.put(COLUMN_LOGIN, userData.getLoggedInTdy());
        values.put(COLUMN_PET_TYPE, userData.getPetType());
        values.put(COLUMN_PET_DESIGN, userData.getPetDesign());
        values.put(COLUMN_TOP_LEFT, userData.getTopLeft());
        values.put(COLUMN_TOP_RIGHT, userData.getTopRight());

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
        values.put(COLUMN_TASK_DATESTARTED, task.getDateStart());
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
        values.put(COLUMN_TASK_DATESTARTED, task.getDateStart());
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

    public void setTopLeft(String username, String topLeft) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOP_LEFT, topLeft);
        db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{username});
        Log.i(title, "Top Left Image updated");
//        db.close();
    }
    public void setTopRight(String username, String topRight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOP_RIGHT, topRight);
        db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{username});
        Log.i(title, "Top Left Image updated");
//        db.close();
    }

    public void removeInventoryItem(InventoryItem inventoryItem, UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INVENTORY,COLUMN_USERNAME + "=? AND " + COLUMN_ITEM_NAME + "=?", new String[]{userData.getUsername(), inventoryItem.getItemName()});
        Log.i(title, "Deleted Inventory Item");
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
            queryResult.setTopLeft(cursor.getString(10));
            queryResult.setTopRight(cursor.getString(11));
            int profilePictureIndex = cursor.getColumnIndex(COLUMN_PROFILE_PICTURE);
            String profilePicturePath = cursor.getString(profilePictureIndex);
            queryResult.setProfilePicturePath(profilePicturePath);
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
            queryResult.setDateStart(cursor.getString(9));
            queryResult.setDateComplete(cursor.getString(10));
            queryResult.setDailyChallenge(cursor.getInt(11));
            queryResult.setPriority(cursor.getInt(12));
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
                task.setDateStart(cursor.getString(9));
                task.setDateComplete(cursor.getString(10));
                task.setDailyChallenge(cursor.getInt(11));
                task.setPriority(cursor.getInt(12));
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

    public String getImageURL(String imageName) {
        String query = "SELECT " + COLUMN_IMAGE_URL + " FROM " + IMAGE_URL + " WHERE " + COLUMN_IMAGE_NAME + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{imageName});

        String imageURL = ""; // Default value if the URL is not found
        if (cursor != null && cursor.moveToFirst()) {
            int imageURLIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);
            imageURL = cursor.getString(imageURLIndex);

            cursor.close();
        }

        return imageURL;
    }
    public String getTopLeft(String username) {
        String query = "SELECT " + COLUMN_TOP_LEFT + " FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username});

        String topLeft = ""; // Default value if the URL is not found
        if (cursor != null && cursor.moveToFirst()) {
            int topLeftIndex = cursor.getColumnIndex(COLUMN_TOP_LEFT);
            topLeft = cursor.getString(topLeftIndex);

            cursor.close();
        }

        return topLeft;
    }
    public String getTopRight(String username) {
        String query = "SELECT " + COLUMN_TOP_RIGHT + " FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username});

        String topRight = ""; // Default value if the URL is not found
        if (cursor != null && cursor.moveToFirst()) {
            int topRightIndex = cursor.getColumnIndex(COLUMN_TOP_RIGHT);
            topRight = cursor.getString(topRightIndex);

            cursor.close();
        }

        return topRight;
    }

    public int getTaskTargetSec(int taskId){
        String query = "SELECT " + COLUMN_TARGET_SEC + " FROM " + TASKS + " WHERE " + COLUMN_TASK_ID + " = " + taskId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int targetSec = -1; // Default value if the targetSec is not found
        if (cursor != null && cursor.moveToFirst()) {
            int targetSecIndex = cursor.getColumnIndex(COLUMN_TARGET_SEC);
            targetSec = cursor.getInt(targetSecIndex);

            cursor.close();
        }
//        db.close();

        return targetSec;
    }

    public String getProfilePicturePath(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String profilePicturePath = null;

        String query = "SELECT " + COLUMN_PROFILE_PICTURE + " FROM " + ACCOUNTS +
                " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            profilePicturePath = cursor.getString(0);
        }

        cursor.close();
        return profilePicturePath;
    }

    public void saveProfilePicture(String username, String profilePicturePath) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Retrieve the previous profile picture path
        String previousProfilePicturePath = getProfilePicturePath(username);

        // Log the previous profile picture path
        Log.i("MyDBHandler", "Previous profile picture path: " + previousProfilePicturePath);

        // Update the profile picture path in the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PICTURE, profilePicturePath);
        db.update(ACCOUNTS, values, COLUMN_USERNAME + "=?", new String[]{username});



        // Log the updated profile picture path
        Log.i("MyDBHandler", "Profile picture saved for user: " + username);
        Log.i("MyDBHandler", "Updated profile picture path: " + profilePicturePath);
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

    public ArrayList<InventoryItem> findInventoryList(UserData userData){
        String query = "SELECT * FROM " + INVENTORY + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";

        Log.i(title, "Query :" + query);
        ArrayList<InventoryItem> inventoryItemList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        InventoryItem queryResult = new InventoryItem();
        if (cursor.moveToFirst()){ // goes to first row if not null
            queryResult.setItemName(cursor.getString(1));
            queryResult.setQuantity(cursor.getInt(2));
            queryResult.setItemCategory(cursor.getString(3));
            inventoryItemList.add(queryResult);
            while (cursor.moveToNext()) { // goes to 2nd row and continues all the way till end
                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setItemName(cursor.getString(1));
                inventoryItem.setQuantity(cursor.getInt(2));
                inventoryItem.setItemCategory(cursor.getString(3));
                inventoryItemList.add(inventoryItem);
            }
            cursor.close();
            userData.setInventoryList(inventoryItemList);
        }
        else{
            userData.setInventoryList(inventoryItemList);
        }
//        db.close();
        return userData.getInventoryList();
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

    public void removeAllFriends(UserData user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FRIENDS, COLUMN_USERNAME + "=?", new String[]{String.valueOf(user.getUsername())});

    }

    public void removeAllFriendRequests(UserData user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FRIENDREQUEST, COLUMN_USERNAME + "=?", new String[]{String.valueOf(user.getUsername())});
    }

    public void addImageURL(String ImageName, String Image_URL){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, ImageName);
        values.put(COLUMN_IMAGE_URL, Image_URL);

        db.insert(IMAGE_URL, null, values);
        Log.i(title, "Inserted ImageURL");
        //        db.close();
    }

    public void addInventoryItem(InventoryItem inventoryItem, UserData userData){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_ITEM_NAME, inventoryItem.getItemName());
        values.put(COLUMN_ITEM_QUANTITY, inventoryItem.getQuantity());
        values.put(COLUMN_ITEM_CATEGORY, inventoryItem.getItemCategory());

        db.insert(INVENTORY, null, values);
        Log.i(title, "Inserted InventoryItem");
        //        db.close();
    }

    public void updateInventoryQuantity(InventoryItem inventoryItem, UserData userData, int newQuantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        Log.i(title, "Password has been updated");
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_ITEM_NAME, inventoryItem.getItemName());
        values.put(COLUMN_ITEM_QUANTITY, newQuantity);
        values.put(COLUMN_ITEM_CATEGORY, inventoryItem.getItemCategory());


        db.update(INVENTORY, values,COLUMN_USERNAME + "=? AND " + COLUMN_ITEM_NAME + "=?", new String[]{userData.getUsername(), inventoryItem.getItemName()});
        Log.i(title, "Inventory Item Quantity Updated");

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

    public void updateUsername(String userID, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newName);

        db.update(ACCOUNTS, values,COLUMN_USERID + "=?", new String[]{userID});

        Log.i(title, "Username has been updated");
    }

    public void updateQuoteAndAuthor(String quoteText, String author, UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{
            db.delete(QUOTE, COLUMN_USERNAME + "=?", new String[]{String.valueOf(userData.getUsername())});

            values.put(COLUMN_QUOTE_TEXT, quoteText);
            values.put(COLUMN_AUTHOR, author);
            values.put(COLUMN_USERNAME, userData.getUsername());
        }catch (Exception e){
            values.put(COLUMN_QUOTE_TEXT, quoteText);
            values.put(COLUMN_AUTHOR, author);
            values.put(COLUMN_USERNAME, userData.getUsername());
        }
        db.insert(QUOTE, null, values);

        Log.i(title, "Quote updated");
//        db.close();
    }

    public String getQuote(UserData userData){
        String query = "SELECT * FROM " + QUOTE + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String quote = "Quote";
        if (cursor != null && cursor.moveToFirst()) {
            int quoteIndex = cursor.getColumnIndex(COLUMN_QUOTE_TEXT);
            quote = cursor.getString(quoteIndex);
            cursor.close();
        }
//        db.close();

        return quote;
    }

    public String getAuthor(UserData userData){
        String query = "SELECT * FROM " + QUOTE + " WHERE " + COLUMN_USERNAME + "=\'" + userData.getUsername() + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String author = "Author";
        if (cursor != null && cursor.moveToFirst()) {
            int authorIndex = cursor.getColumnIndex(COLUMN_AUTHOR);
            author = cursor.getString(authorIndex);
            cursor.close();
        }
//        db.close();

        return author;
    }

    public void findHighestID(String oldUniqueId) {
        final String[] newID = new String[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        // Query the users collection and sort by the unique ID field in descending order
        Query query = myRef.orderByChild("userID");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int highestUniqueId = -1;
                    // Iterate over the result and retrieve the highest unique ID
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserData user = snapshot.getValue(UserData.class);
                        int currentUniqueId = user.getUserId() + 1;
                        if (currentUniqueId > highestUniqueId) {
                            highestUniqueId = currentUniqueId;
                            newID[0] = String.valueOf(highestUniqueId);
                        }
                    }
                createNewRowWithUniqueID(oldUniqueId,newID[0]);
                } else {
                    // Handle the case when there are no users in the database
                    Log.i(title, "No users found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error if the database operation was canceled
                Log.e(title, "Database error: " + databaseError.getMessage());
            }
        });

    }
    public void createNewRowWithUniqueID(String oldUniqueId,String newID){
        // Do something with the highest unique ID
        Log.i(title, "Highest Unique ID: " + newID);
        SQLiteDatabase db = this.getWritableDatabase();

        // Copy data from the old row to the new row
        ContentValues newValues = new ContentValues();
        Cursor cursor = db.query(ACCOUNTS, null, COLUMN_USERID + "=?", new String[]{oldUniqueId}, null, null, null);
        if (cursor.moveToFirst()) {
            DatabaseUtils.cursorRowToContentValues(cursor, newValues);
            newValues.put(COLUMN_USERID, newID);
            db.insert(ACCOUNTS, null, newValues);
        }
        cursor.close();

        // Delete the old row
        db.delete(ACCOUNTS, COLUMN_USERID + "=?", new String[]{oldUniqueId});

        Log.i(title, "Created a new row with a desired unique ID and deleted the old row");
    }

    public int countTasks(ArrayList<Task> taskList){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());
        int count = 0;
        for (Task task : taskList){
            if (task.getStatus().equals("In Progress") && task.getDueDate() != null && task.getDueDate().equals(currentDate)){
                count+=1;
            }
        }
        Log.i(title, "Number of tasks : " + count);
        return count;
    }

}
