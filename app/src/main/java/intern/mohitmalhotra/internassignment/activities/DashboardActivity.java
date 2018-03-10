package intern.mohitmalhotra.internassignment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.adapters.FragmentAdapter;
import intern.mohitmalhotra.internassignment.dao.CourseDBHelper;
import intern.mohitmalhotra.internassignment.fragments.CoursesFragment;
import intern.mohitmalhotra.internassignment.fragments.HomeFragment;
import intern.mohitmalhotra.internassignment.modals.Course;

import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_COURSES;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_VERSION_1;

public class DashboardActivity extends AppCompatActivity {


    private FragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;

    private ImageButton btnLogout;

    private ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabbedLayout);

        // create courses DB
        initDB();

        initFragmentsWithPager();

        tabLayout.setupWithViewPager(mViewPager);

        initTabIcons();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.sp_users), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.sp_uid), null);
                editor.putBoolean(getString(R.string.sp_is_user_signedin), false);

                editor.apply();

                startActivity(new Intent(DashboardActivity.this, AuthActivity.class));
                finish();
            }
        });
    }

    private void initDB() {
        new CourseDBHelper(getApplicationContext(), TBL_COURSES , TBL_VERSION_1);
    }

    private void initTabIcons() {
        int[] icons = {
                R.drawable.ic_home,
                R.drawable.ic_collection
        };

        for(int i=0; i<fragmentAdapter.getCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null) {
                tab.setIcon(icons[i]);
            }
        }
    }


    private void initFragmentsWithPager() {
        fragments = new ArrayList<>();

        fragments.add(new HomeFragment());

        CoursesFragment cf = new CoursesFragment();
        cf.setLoggedIn(true);   // that user is logged in
        fragments.add(cf);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }

    public void syncData(Fragment f){
        if(f != null){

            if(f instanceof CoursesFragment) {
                ((HomeFragment) fragments.get(0)).loadData();
            } else {
                ((CoursesFragment) fragments.get(1)).loadData();
            }
        }
    }

}
