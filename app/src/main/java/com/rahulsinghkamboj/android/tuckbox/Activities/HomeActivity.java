package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rahulsinghkamboj.android.tuckbox.R;


public class HomeActivity extends AppCompatActivity{

    private Button mPalmerston;
    private Button mFeilding;
    private Button mAshhurst;
    private Button mLongbeach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Select Location");

        mPalmerston = (Button) findViewById(R.id.location_pn);
        mFeilding = (Button) findViewById(R.id.location_fg);
        mAshhurst = (Button) findViewById(R.id.location_at);
        mLongbeach = (Button) findViewById(R.id.location_lb);

        mPalmerston.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation("Palmerston North");
            }
        });
        mFeilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation("Feilding");
            }
        });
        mAshhurst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation("Ashhurst");
            }
        });
        mLongbeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation("Long Beach");
            }
        });

    }


    public void getLocation(String location){
        Intent intent= new Intent(HomeActivity.this, OrderActivity.class);
        intent.putExtra("LOCATION",location);                                 //sending data between 2 activities
        startActivity(intent);

    }

    public void logoutUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            startActivity(intent);

                        }
                    });
                }
            }
        });
        auth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_recent_orders){
            // do something
            startActivity(new Intent(HomeActivity.this,RecentOrdersActivity.class));
        }
        else if(id == R.id.action_settings){
            // do something
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
