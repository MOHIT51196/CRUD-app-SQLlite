package intern.mohitmalhotra.internassignment.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.activities.AuthActivity;
import intern.mohitmalhotra.internassignment.activities.DashboardActivity;
import intern.mohitmalhotra.internassignment.dao.UserCourseMappingDBHelper;
import intern.mohitmalhotra.internassignment.fragments.HomeFragment;
import intern.mohitmalhotra.internassignment.modals.Course;
import intern.mohitmalhotra.internassignment.utils.DbUtils;

/**
 * Created by MOHIT MALHOTRA on 07-03-2018.
 */

public class CourseAdapter extends BaseAdapter{

    private Context context;
    private Fragment fragment;
    private ArrayList<Course> courses;
    private LayoutInflater inflater;

    private Button btnApply;

    private boolean isLoggedIn;

    private UserCourseMappingDBHelper mappingDBHelper;

    public CourseAdapter(Context context, ArrayList<Course> courses, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.courses = courses;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mappingDBHelper = new UserCourseMappingDBHelper(context, DbUtils.TBL_USER_COURSE_MAPPING, DbUtils.TBL_VERSION_1);

    }

    // constructor chaining
    public CourseAdapter(Context context, ArrayList<Course> courses, Fragment fragment, boolean isLoggedIn){
        this(context, courses, fragment);
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.layout_course, parent, false);

        btnApply = (Button) view.findViewById(R.id.btnApply);
        final ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        final TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);


        final Course c = courses.get(position);

        ivIcon.setImageResource(c.getImageId());
        tvTitle.setText(c.getName());
        tvDesc.setText(c.getDesc());

        if(mappingDBHelper.isCourseApplied(c.getCid())){
            setAppliedState();
        }


        btnApply.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isLoggedIn){
                doApply(c);
            } else {
                context.startActivity(new Intent(context, AuthActivity.class));
            }
            }
        });

        return view;
    }

    private void doApply(Course c) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getString(R.string.sp_users), Context.MODE_PRIVATE);
        String uid = sp.getString(context.getString(R.string.sp_uid), null);

        if(uid != null) {
            if(isApplied()) {
                mappingDBHelper.removeAppliedCourse(c.getCid());
                setUnappliedState();
            } else {
                mappingDBHelper.addAppliedCourse(Integer.valueOf(uid), Integer.valueOf(c.getCid()));
                setAppliedState();
            }

            // to slice down the current list in home fragment
            if(fragment instanceof HomeFragment){
                courses.remove(c);
                notifyDataSetChanged();
            }

            // to sync the data across fragments
            if(context instanceof DashboardActivity){
                ((DashboardActivity)context).syncData(CourseAdapter.this.fragment);
            }
        } else{
            Toast.makeText(context, "Unable to apply for the course", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAppliedState(){
        btnApply.setText("Applied");
        btnApply.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
    }

    private void setUnappliedState(){
        btnApply.setText("Apply");
        btnApply.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
    }

    private boolean isApplied(){
        return btnApply.getText().toString().trim().equalsIgnoreCase("Applied");
    }
}
