package intern.mohitmalhotra.internassignment.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.adapters.CourseAdapter;
import intern.mohitmalhotra.internassignment.dao.CourseDBHelper;
import intern.mohitmalhotra.internassignment.modals.Course;

import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_COURSES;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_VERSION_1;


public class CoursesFragment extends Fragment {

    private View view;
    private CourseDBHelper dbHelper;
    private CourseAdapter adapter;
    private ArrayList<Course> courseList;

    private boolean isLoggedIn;

    private ProgressBar progressBar;

    {
        this.courseList = new ArrayList<>();
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_courses, container, false);
        final GridView gvCourses = (GridView) view.findViewById(R.id.gvCourses);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        dbHelper = new CourseDBHelper(getActivity().getApplicationContext(), TBL_COURSES , TBL_VERSION_1);

        adapter = new CourseAdapter(getActivity(), courseList, this, isLoggedIn);
        gvCourses.setAdapter(adapter);

        loadData();


        return this.view;
    }

    public void loadData(){
        new ListLoaderTask().execute();
    }



    private class ListLoaderTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            courseList.clear();
            courseList.addAll(dbHelper.readAllCourses());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            if(courseList != null && !courseList.isEmpty()) {
                adapter.notifyDataSetChanged();
            }
        }
    }



}
