package intern.mohitmalhotra.internassignment.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import intern.mohitmalhotra.internassignment.activities.DashboardActivity;
import intern.mohitmalhotra.internassignment.activities.MainActivity;
import intern.mohitmalhotra.internassignment.modals.User;


public class ConfirmSignupFragment extends Fragment {


    private View view;

    private ProgressBar loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_confirm_signup, container, false);

        final ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        final Button btnSignUp = (Button) view.findViewById(R.id.btnSignup);
        final TextView linkToSignIn = (TextView) view.findViewById(R.id.linkToSignin);
        loadingBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadingBar.setIndeterminate(true);




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).previousPage();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);

                ((SignupFragment)AuthActivity.getFragment(1)).updateUserProfile();

                loadingBar.setVisibility(View.GONE);

                startActivity(new Intent(getActivity(), DashboardActivity.class));
                getActivity().finish();
            }
        });

        linkToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).previousPage();
                ((AuthActivity)getActivity()).previousPage();
            }
        });

        return this.view;
    }




}
