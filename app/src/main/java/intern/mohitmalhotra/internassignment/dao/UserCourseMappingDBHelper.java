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

import java.util.ArrayList;

import intern.mohitmalhotra.internassignment.modals.Course;
import intern.mohitmalhotra.internassignment.utils.DbUtils;

import static android.graphics.BlurMaskFilter.Blur.INNER;
import static intern.mohitmalhotra.internassignment.dao.CourseDBHelper.COL_DESC;
import static intern.mohitmalhotra.internassignment.dao.CourseDBHelper.COL_IMAGE_ID;
import static intern.mohitmalhotra.internassignment.dao.CourseDBHelper.COL_NAME;
import static intern.mohitmalhotra.internassignment.dao.UserDBHelper.COL_EMAIL;
import static intern.mohitmalhotra.internassignment.dao.UserDBHelper.COL_PASSWORD;
import static intern.mohitmalhotra.internassignment.dao.UserDBHelper.COL_USERNAME;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_COURSES;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_USERS;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_USER_COURSE_MAPPING;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_VERSION_1;

/**
 * Created by MOHIT MALHOTRA on 07-03-2018.
 * This is an additional mapping table DAO
 * so as to maintain the BCNF for Database
 */

public class UserCourseMappingDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MappingDBHelper";

    public static final String COL_MAPPING_ID = "ucid";
    public static final String COL_USER_ID = "uid";
    public static final String COL_COURSE_ID = "cid";

    private Context context;
    private String tableName;

    public UserCourseMappingDBHelper(Context context, String tableName, int version) {
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
            Log.d(TAG, "Unable to create the MAPPING table");
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

    public boolean isCourseApplied(final String uid){
        boolean isExist = false;

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + tableName + " WHERE " + COL_COURSE_ID + "=?", new String[]{uid});

        if(c != null){
            isExist = c.moveToFirst();
            c.close();
        }

        return isExist;
    }

    public boolean addAppliedCourse(final int uid, final int cid){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, uid);
        values.put(COL_COURSE_ID, cid);


        // sql api supported method to insert content values
        long resCode = db.insert(this.tableName, null, values);
        db.close();

        if(resCode == -1){
            return false;
        }



        return true;


    }

    public ArrayList<String> getAllAppliedCourses(final String uid){

        ArrayList<String> courses = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT "
                        + COL_COURSE_ID
                        + " FROM "
                        + DbUtils.TBL_USER_COURSE_MAPPING
                        + " WHERE "
                        + COL_USER_ID + "=?"
                , new String[]{uid});

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{

                    courses.add(String.valueOf(cursor.getInt(0)));
                } while(cursor.moveToNext());
            }

            cursor.close();
        }

        return courses;
    }


    public boolean removeAppliedCourse(final String id){
        int rows = this.getWritableDatabase().delete(tableName, COL_COURSE_ID + "=?", new String[]{id});
        return rows > 0;
    }

    // SQL QUERY GENERATING METHODS
    private String getCreateTableQuery(){
        String query = "CREATE TABLE " + tableName + " ( "
                + COL_MAPPING_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + COL_USER_ID + " INTEGER NOT NULL, "
                + COL_COURSE_ID + " INTEGER NOT NULL "
                + ")";

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
