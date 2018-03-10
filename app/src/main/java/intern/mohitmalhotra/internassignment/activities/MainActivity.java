package intern.mohitmalhotra.internassignment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.fragments.CoursesFragment;
import intern.mohitmalhotra.internassignment.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    public static final String HOME_FRAG = "home_frag";

    private Button btnJoin;

    @Override
    protected void onStart() {
        super.onStart();
        validateUserAuthentication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJoin = (Button) findViewById(R.id.btnJoin);

        // setting the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.add(R.id.container, new CoursesFragment(), HOME_FRAG);

        ft.commit();

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });
    }

    private void validateUserAuthentication(){

        SharedPreferences sp = getSharedPreferences(getString(R.string.sp_users), MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean(getString(R.string.sp_is_user_signedin), false);

        if(isLoggedIn){
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }
    }
}
