package intern.mohitmalhotra.internassignment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.dao.CourseDBHelper;
import intern.mohitmalhotra.internassignment.dao.UserCourseMappingDBHelper;
import intern.mohitmalhotra.internassignment.fragments.ConfirmSignupFragment;
import intern.mohitmalhotra.internassignment.fragments.SigninFragment;
import intern.mohitmalhotra.internassignment.fragments.SignupFragment;
import intern.mohitmalhotra.internassignment.utils.DbUtils;
import intern.mohitmalhotra.internassignment.widgets.CustomViewPager;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

/**
 * This is the authentication scree activity
 * which allows the user to sign in and sign up on the application.
 */

public class AuthActivity extends AppCompatActivity{

    private ImageView bgImageview;
    private CustomViewPager pager;
    private FragmentAdapter fragmentAdapter;
    private static ArrayList<Fragment> fragments;

    @Override
    protected void onStart() {
        super.onStart();
        validateUserAuthentication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        setContentView(R.layout.activity_auth);

        castWidgets();

        Glide.with(this)
                .load(R.drawable.bg_theme1)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bgImageview);


        initFragmentsWithPager();


    }

    private void validateUserAuthentication(){

        SharedPreferences sp = getSharedPreferences(getString(R.string.sp_users), MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean(getString(R.string.sp_is_user_signedin), false);

        if(isLoggedIn){
            startActivity(new Intent(AuthActivity.this, DashboardActivity.class));
            finish();
        }
    }



    private void initFragmentsWithPager() {
        fragments = new ArrayList<>();
        fragments.add(new SigninFragment());
        fragments.add(new SignupFragment());
        fragments.add(new ConfirmSignupFragment());

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(fragmentAdapter);
        pager.setPagingEnabled(false);
    }

    public static Fragment getFragment(int index) {
        return fragments.get(index);
    }

    private void castWidgets() {
        bgImageview = (ImageView) findViewById(R.id.bgImage);
        pager = (CustomViewPager) findViewById(R.id.fragmentViewPager);
    }

    public void nextPage(){
        if(pager.getChildCount() > pager.getCurrentItem()) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    public void previousPage() {
        if (pager.getCurrentItem() > 0) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() > 0){
            this.previousPage();
        } else {
            super.onBackPressed();
        }
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragments;

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
