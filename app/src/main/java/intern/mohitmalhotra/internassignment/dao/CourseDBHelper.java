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

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.modals.Course;
import intern.mohitmalhotra.internassignment.utils.DbUtils;

import static intern.mohitmalhotra.internassignment.dao.UserCourseMappingDBHelper.COL_COURSE_ID;
import static intern.mohitmalhotra.internassignment.dao.UserCourseMappingDBHelper.COL_USER_ID;

/**
 * Created by MOHIT MALHOTRA on 07-03-2018.
 * This is a DAO for Courses Table
 */

public class CourseDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "CourseDBHelper";

    public static final String COL_ID = "cid";
    public static final String COL_NAME = "cname";
    public static final String COL_DESC = "cdesc";
    public static final String COL_IMAGE_ID = "cimage";

    private Context context;
    private final String tableName;

    public CourseDBHelper(Context context, String tableName, int version) {
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
            Log.d(TAG, "Created the COURSE table");
        } catch (SQLException e){
            Log.d(TAG, "Unable to create the COURSE table");
            showErrorToast();
        }

        if(addDummyCourse(db)){
            Log.d(TAG, "Courses added");
        } else {
            Log.d(TAG, "Courses can't be added");
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

    public Course readCourseById(final String id){
        Course c = null;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "
                + COL_ID + ","
                + COL_NAME + ","
                + COL_DESC + ","
                + COL_IMAGE_ID
                + " FROM " + this.tableName + " WHERE " +  COL_ID + "=?", new String[]{id});

        if (cursor != null){
            if(cursor.moveToFirst()){
                c = new Course();
                c.setCid(String.valueOf(cursor.getInt(0)));
                c.setName(cursor.getString(1));
                c.setDesc(cursor.getString(2));
                c.setImageId(cursor.getInt(3));
            }
            cursor.close();
        }

        return c;
    }

    public ArrayList<Course> readAllCourses(){
        ArrayList<Course> courses = new ArrayList<>();

        Course c = null;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "
                + COL_ID + ","
                + COL_NAME + ","
                + COL_DESC + ","
                + COL_IMAGE_ID
                + " FROM " + this.tableName, null);


        if (cursor != null){
            if(cursor.moveToFirst()){
                do{
                    c = new Course();
                    c.setCid(String.valueOf(cursor.getInt(0)));
                    c.setName(cursor.getString(1));
                    c.setDesc(cursor.getString(2));
                    c.setImageId(cursor.getInt(3));

                    courses.add(c);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return courses;
    }

    // SQL QUERY GENERATING METHODS
    private String getCreateTableQuery(){
        String query = "CREATE TABLE " + tableName + "( "
                + COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_DESC + " TEXT, "
                + COL_IMAGE_ID + " INTEGER NOT NULL "
                + ");";

        return query;
    }

    public boolean addDummyCourse(SQLiteDatabase db){

        ContentValues cpp = new ContentValues();
        cpp.put(COL_NAME, "C++");
        cpp.put(COL_DESC, "Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        cpp.put(COL_IMAGE_ID, R.drawable.ic_cpp_200);

        long isAdded = db.insert(tableName, null, cpp);


        ContentValues c = new ContentValues();
        c.put(COL_NAME, "C");
        c.put(COL_DESC, "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        c.put(COL_IMAGE_ID, R.drawable.ic_c_200);

        isAdded += db.insert(tableName, null, c);

        ContentValues java = new ContentValues();
        java.put(COL_NAME, "Java");
        java.put(COL_DESC, "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        java.put(COL_IMAGE_ID, R.drawable.ic_java_200);

        isAdded += db.insert(tableName, null, java);

        ContentValues py = new ContentValues();
        py.put(COL_NAME, "Python");
        py.put(COL_DESC, "Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        py.put(COL_IMAGE_ID, R.drawable.ic_python_200);

        isAdded += db.insert(tableName, null, py);

        return !(isAdded < 0);

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
