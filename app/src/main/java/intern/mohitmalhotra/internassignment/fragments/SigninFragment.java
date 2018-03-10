package intern.mohitmalhotra.internassignment.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.activities.AuthActivity;
import intern.mohitmalhotra.internassignment.activities.DashboardActivity;
import intern.mohitmalhotra.internassignment.activities.MainActivity;
import intern.mohitmalhotra.internassignment.dao.UserDBHelper;
import intern.mohitmalhotra.internassignment.modals.User;
import intern.mohitmalhotra.internassignment.utils.DbUtils;

import static android.media.MediaCodec.MetricsConstants.MODE;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_USERS;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_VERSION_1;


public class SigninFragment extends Fragment {

    private View view;
    private Button btnSignIn;
    private TextView etEmail, etPswd;
    private TextView linkToSignUp;
    private ProgressBar loadingBar;

    private UserDBHelper userDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_signin, container, false);

        etEmail = (TextView) view.findViewById(R.id.etEmail);
        etPswd = (TextView) view.findViewById(R.id.etPassword);
        btnSignIn = (Button) view.findViewById(R.id.btnSignin);
        linkToSignUp = (TextView) view.findViewById(R.id.linkToSignup);
        loadingBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadingBar.setIndeterminate(true);


        userDBHelper = new UserDBHelper(getActivity().getApplicationContext(), TBL_USERS, TBL_VERSION_1);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValid()){
                    loadingBar.setVisibility(View.VISIBLE);

                    doSignIn(etEmail.getText().toString().trim(), etPswd.getText().toString().trim());

                    if(loadingBar.getVisibility() == View.VISIBLE) {
                        loadingBar.setVisibility(View.GONE);
                    }
                } else{
                    Toast.makeText(getActivity(), "Please fill the required details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).nextPage();
            }
        });
        return this.view;
    }

    // to perform login authentication on the app
    private void doSignIn(String usernameOrEmail, String password) {

        if(userDBHelper.authUser(usernameOrEmail, password) || userDBHelper.authUserWithEmail(usernameOrEmail, password)){
            setSharedPreferences(usernameOrEmail);
            startActivity(new Intent(getActivity(), DashboardActivity.class));
            loadingBar.setVisibility(View.GONE);
            getActivity().finish();
        } else{
            Toast.makeText(getActivity(), "OOPS! Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSharedPreferences(String usernameOrEmail) {

        User user = userDBHelper.readUserByUsername(usernameOrEmail);
        if(user == null){
            user = userDBHelper.readUserByEmail(usernameOrEmail);
        }

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.sp_users), Context.MODE_PRIVATE).edit();
        editor.putString(getString(R.string.sp_uid), user.getUid());
        editor.putBoolean(getString(R.string.sp_is_user_signedin),true);

        editor.apply();
    }

    // to check if the necessary fields are filled.
    private boolean isFormValid() {
        return !(TextUtils.isEmpty(etEmail.getText().toString().trim()) || TextUtils.isEmpty(etPswd.getText().toString().trim()));
    }

}
