package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rahulsinghkamboj.android.tuckbox.R;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private Button mBtnSignUp;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
                                                                                //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
                                                                                //Check session if user is logged in.
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }

        mBtnLogin = (Button) findViewById(R.id.loginBtn);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        mBtnSignUp = (Button) findViewById(R.id.signUp);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
