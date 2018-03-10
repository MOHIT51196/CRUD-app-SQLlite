package intern.mohitmalhotra.internassignment.fragments;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import intern.mohitmalhotra.internassignment.R;
import intern.mohitmalhotra.internassignment.activities.AuthActivity;
import intern.mohitmalhotra.internassignment.dao.UserDBHelper;
import intern.mohitmalhotra.internassignment.modals.User;

import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_USERS;
import static intern.mohitmalhotra.internassignment.utils.DbUtils.TBL_VERSION_1;

public class SignupFragment extends Fragment {

    private View view;
    private TextView etUsername, etName, etEmail, etPswd, etConfirmPswd;
    private UserDBHelper userDBHelper;
    private ProgressBar loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_signup, container, false);

        etName = (TextView) view.findViewById(R.id.etName);
        etUsername = (TextView) view.findViewById(R.id.etUsername);
        etEmail = (TextView) view.findViewById(R.id.etEmail);
        etPswd = (TextView) view.findViewById(R.id.etPassword);
        etConfirmPswd = (TextView) view.findViewById(R.id.etConfirmPassword);
        loadingBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadingBar.setIndeterminate(true);


        final Button btnNext = (Button) view.findViewById(R.id.btnNext);
        final ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        final TextView linkToSignIn = (TextView) view.findViewById(R.id.linkToSignin);

        userDBHelper = new UserDBHelper(getActivity().getApplicationContext(), TBL_USERS, TBL_VERSION_1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).previousPage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValid()) {
                    if(isPasswordConfirmed()) {
                        loadingBar.setVisibility(View.VISIBLE);
                        doNext(etUsername.getText().toString().trim(), etPswd.getText().toString().trim());
                        loadingBar.setVisibility(View.GONE);
                    } else{
                        Toast.makeText(getActivity(), "Password is not confirmed", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getActivity(), "Please fill the required details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linkToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.callOnClick();
            }
        });

        return this.view;
    }

    private boolean isPasswordConfirmed() {
        return etPswd.getText().toString().trim().equals(etConfirmPswd.getText().toString().trim());
    }


    // to perform registeration on the app
    private void doNext(String username, String password) {

        if(userDBHelper.isUserExist(username)){
            Toast.makeText(getActivity(), "OOPS! This username already exists", Toast.LENGTH_SHORT).show();
            return;
        } else{
            ((AuthActivity)getActivity()).nextPage();
        }

    }

    public boolean updateUserProfile() {

        String email = etEmail.getText().toString().trim();
        final String pass = etPswd.getText().toString().trim();

        if(userDBHelper.isEmailRegistered(email)){
            Toast.makeText(getActivity(), "OOPS! This email id is already registered", Toast.LENGTH_SHORT).show();

        } else {
            User u = new User();
            u.setUsername(etUsername.getText().toString().trim());
            u.setName(etName.getText().toString().trim());
            u.setEmail(email);

            boolean isAdded = userDBHelper.addUser(u, pass);

            if(isAdded) {
                setSharedPreferences(u.getUsername());
            }

            return isAdded;
        }

        return false;

    }

    private void setSharedPreferences(String username) {

        User user = userDBHelper.readUserByUsername(username);

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.sp_users), Context.MODE_PRIVATE).edit();
        editor.putString(getString(R.string.sp_uid), user.getUid());
        editor.putBoolean(getString(R.string.sp_is_user_signedin),true);

        editor.apply();
    }

    // to check if the necessary fields are filled.
    private boolean isFormValid() {
        return !(TextUtils.isEmpty(etName.getText().toString().trim())
                || TextUtils.isEmpty(etUsername.getText().toString().trim())
                || TextUtils.isEmpty(etEmail.getText().toString().trim())
                || TextUtils.isEmpty(etPswd.getText().toString().trim())
                || TextUtils.isEmpty(etConfirmPswd.getText().toString().trim()) );
    }
}
