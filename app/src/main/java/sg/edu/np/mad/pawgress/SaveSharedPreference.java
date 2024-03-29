package sg.edu.np.mad.pawgress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_PROFILE_PIC = "profilePic";
    static final String PREF_SEEN_FRIENDREQ = "seenFriendReq";
    static final String PREF_OLD_REQLISTSIZE = "oldReqListSize";
    static final String PREF_QUOTE = "quote";
    static final String PREF_AUTHOR = "author";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //-----------------------------Setters-----------------------------//
    public static void setOldReqlistsize(Context ctx, int size)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_OLD_REQLISTSIZE, size);
        editor.commit();
    }

    public static void setSeenFriendReq(Context ctx, String seen)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SEEN_FRIENDREQ, seen);
        editor.commit();
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setProfilePic(Context ctx, int profilePic)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_PROFILE_PIC, profilePic);
        editor.commit();
    }

    public static void setQuote(Context ctx, String quote)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_QUOTE, quote);
        editor.commit();
    }

    public static void setAuthor(Context ctx, String author)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_AUTHOR, author);
        editor.commit();
    }
    //-----------------------------Setters-----------------------------//

    //-----------------------------Getters-----------------------------//
    public static int getOldReqlistsize(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_OLD_REQLISTSIZE, 0);
    }

    public static String getSeenFriendReq(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SEEN_FRIENDREQ, "");
    }

    public static int getProfilePic(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_PROFILE_PIC, R.drawable.corgi_sunglasses);
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getQuote(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_QUOTE, "null");
    }

    public static String getAuthor(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_AUTHOR, "null");
    }
    //-----------------------------Getters-----------------------------//

    //------------------------------Clear------------------------------//
    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_NAME); //clear username stored data
        editor.commit();
    }

    public static void clearSeenFriendReq(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_SEEN_FRIENDREQ); //clear seen friend request stored data
        editor.commit();
    }
    //------------------------------Clear------------------------------//
}
