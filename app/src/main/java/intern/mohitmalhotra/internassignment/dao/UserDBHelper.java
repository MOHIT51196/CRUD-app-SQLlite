package intern.mohitmalhotra.internassignment.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import intern.mohitmalhotra.internassignment.modals.User;

import static android.R.attr.version;
import static intern.mohitmalhotra.internassignment.R.string.email;
import static intern.mohitmalhotra.internassignment.R.string.username;

/**
 * Created by MOHIT MALHOTRA on 07-03-2018.
 * This is a DAO for Users Table
 */

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserDBHelper";

    public static final String COL_ID = "uid";
    public static final String COL_USERNAME = "uname";
    public static final String COL_NAME = "name";
    public static final String COL_PASSWORD = "pass";
    public static final String COL_EMAIL = "email";

    private Context context;
    private String tableName;

    public UserDBHelper(Context context, String tableName, int version) {
        super(context, tableName, null, version);
        this.context = context;
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the database table
        String createQuery = getCreateTableQuery();

        try {
            db.execSQL(createQuery);
        } catch (SQLException e){
            Log.d(TAG, "Unable to create the USER table");
            showErrorToast();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(getDropTableQuery());
            this.onCreate(db);

        } catch (SQLException e){
            showErrorToast();
        }

    }

    public boolean addUser(User user, final String password){

        // conventional query
//        String addQuery = getInsertUserQuery(user, password);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_PASSWORD, password);
        values.put(COL_NAME, user.getName());
        values.put(COL_EMAIL, user.getEmail());

        // sql api supported method to insert content values
        long resCode = db.insert(this.tableName, null, values);

        if(resCode == -1){
            return false;
        }

        return true;
    }

    public boolean isUserExist(final String username){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + this.tableName + " WHERE " + COL_USERNAME + "=?", new String[]{username});
        boolean isExist = false;

        if (cursor != null){
            isExist = cursor.moveToFirst();
            cursor.close();
        }

        return isExist;
    }

    public boolean isEmailRegistered(final String email){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + this.tableName + " WHERE " + COL_EMAIL + "=?", new String[]{email});
        boolean isExist = false;

        if (cursor != null){
            isExist = cursor.moveToFirst();
            cursor.close();
        }

        return isExist;
    }

    public boolean authUser(final String username,final String password){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + this.tableName + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});
        boolean isExist = false;

        if (cursor != null){
            isExist = cursor.moveToFirst();
            cursor.close();
        }

        return isExist;
    }

    public boolean authUserWithEmail(final String email,final String password){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + this.tableName + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean isExist = false;

        if (cursor != null){
            isExist = cursor.moveToFirst();
            cursor.close();
        }

        return isExist;
    }

    public User readUserByUsername(final String username){
        User user = null;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "
                + COL_ID + ","
                + COL_USERNAME + ","
                + COL_NAME + ","
                + COL_EMAIL
                + " FROM " + this.tableName + " WHERE " + COL_USERNAME + "=?", new String[]{username});

        if (cursor != null){
            if(cursor.moveToFirst()){
                user = new User();
                user.setUid(String.valueOf(cursor.getInt(0)));
                user.setUsername(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setEmail(cursor.getString(3));
            }
            cursor.close();
        }

        return user;
    }

    public User readUserByEmail(final String email){
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "
                + COL_ID + ","
                + COL_USERNAME + ","
                + COL_NAME + ","
                + COL_EMAIL
                + " FROM " + this.tableName + " WHERE " + COL_EMAIL + "=?", new String[]{email});

        if (cursor != null){
            if(cursor.moveToFirst()){
                user = new User();
                user.setUid(String.valueOf(cursor.getInt(0)));
                user.setUsername(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setEmail(cursor.getString(3));
            }
            cursor.close();
        }

        return user;
    }

    // this ia conventional query method that can be used fo any SQL Database
    @Deprecated
    private String getInsertUserQuery(User user, final String password){
        String query = "INSERT INTO " + tableName + "( "
                + COL_USERNAME + ", "
                + COL_PASSWORD + ", "
                + COL_NAME + ", "
                + COL_EMAIL
                + " )" + " VALUES ("
                + user.getUsername() + ","
                + password + ","
                + user.getName() + ","
                + user.getEmail() + ","
                + " )";

        return query;
    }

    // SQL QUERY GENERATING METHODS
    private String getCreateTableQuery(){
        String query = "CREATE TABLE " + tableName + " ( "
                + COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + COL_USERNAME + " TEXT NOT NULL, "
                + COL_PASSWORD + " TEXT NOT NULL, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_EMAIL + " VARCHAR(255) "
                + ");";

        return query;
    }

    private String getDropTableQuery(){
        return "DROP TABLE IF EXIST" + tableName;
    }

    private void showErrorToast(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Oops! Some internal error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
