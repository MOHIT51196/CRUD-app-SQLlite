package intern.mohitmalhotra.internassignment.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.adapters.CourseAdapter;
import intern.mohitmalhotra.internassignment.dao.CourseDBHelper;
import intern.mohitmalhotra.internassignment.dao.UserCourseMappingDBHelper;
import intern.mohitmalhotra.internassignment.modals.Course;
import intern.mohitmalhotra.internassignment.utils.DbUtils;

import static intern.mohitmalhotra.internassignment.R.id.btnApply;
import static intern.mohitmalhotra.internassignment.R.id.progressBar;

public class HomeFragment extends Fragment {

    private View view;
    private CourseAdapter adapter;
    private UserCourseMappingDBHelper mappingDBHelper;
    private ProgressBar progressBar;

    private ArrayList<Course> courses;

    {
        this.courses = new ArrayList<>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        final GridView gvCourses = view.findViewById(R.id.gvCourses);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mappingDBHelper = new UserCourseMappingDBHelper(getActivity().getApplicationContext(), DbUtils.TBL_USER_COURSE_MAPPING, DbUtils.TBL_VERSION_1);

        adapter = new CourseAdapter(getActivity(), courses, this, true);
        gvCourses.setAdapter(adapter);

        loadData();

        return this.view;
    }

    public void loadData(){
        new CourseLoaderTask().execute();
    }

    private class CourseLoaderTask extends AsyncTask<Void, Integer, Void> {

        private String uid;
        private CourseDBHelper courseDBHelper;

        @Override
        protected void onPreExecute() {
            courseDBHelper = new CourseDBHelper(getActivity(), DbUtils.TBL_COURSES, DbUtils.TBL_VERSION_1);

            progressBar.setVisibility(View.VISIBLE);
            SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.sp_users), Context.MODE_PRIVATE);
            uid = sp.getString(getString(R.string.sp_uid), null);

        }

        @Override
        protected Void doInBackground(Void... params) {

            if(uid != null) {
                Log.d("IDENTITY CHECK", "uid=" + uid);
                courses.clear();

                final ArrayList<String> courseIdList = mappingDBHelper.getAllAppliedCourses(uid);

                for(String cid : courseIdList) {
                    Course c = courseDBHelper.readCourseById(cid);
                    if(c != null) {
                        courses.add(c);
                    }
                }
            } else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Unable to fetch synced data", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressBar.setVisibility(View.GONE);

            if(courses != null) {
                if(!courses.isEmpty()) {
                    adapter.notifyDataSetChanged();
                }
            } else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Unable to fetch courses", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }


    }
}
