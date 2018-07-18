package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rahulsinghkamboj.android.tuckbox.R;

public class OrderActivity extends AppCompatActivity {
    private ImageView mGreenSalad;
    private ImageView mLambKorma;
    private ImageView mOpenChicken;
    private ImageView mBeefNoodle;
    private RadioGroup mGSL;
    private RadioGroup mLK;
    private RadioGroup mOPS;
    private RadioGroup mBNS;
    private Button mbtnNext;
    private RadioButton radioButton;
    private String LOCATION;
    private String mstrMenuOption = "";
    private String mstrMenuCustomisationOption = "";
    private RadioGroup mFlag = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Menu. Tap on image for options.");
        LOCATION = getIntent().getStringExtra("LOCATION");


        mBeefNoodle = (ImageView) findViewById(R.id.bns);
        mLambKorma = (ImageView) findViewById(R.id.lk);
        mOpenChicken = (ImageView) findViewById(R.id.ops);
        mGreenSalad = (ImageView) findViewById(R.id.gsl);
        mbtnNext = (Button) findViewById(R.id.btn_next);
        mGSL = (RadioGroup) findViewById(R.id.GSL);
        mLK = (RadioGroup) findViewById(R.id.LK);
        mOPS = (RadioGroup) findViewById(R.id.OPS);
        mBNS = (RadioGroup) findViewById(R.id.BNS);

        mBeefNoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBNS.setVisibility(View.VISIBLE);
                enableRadioGroup(mBNS);

                mBeefNoodle.setPadding(0,0,0,0);
                mGreenSalad.setPadding(20,20,20,20);
                mLambKorma.setPadding(20,20,20,20);
                mOpenChicken.setPadding(20,20,20,20);

                mFlag = mBNS;
                mstrMenuOption = "Beef Noodle Salad";
                mstrMenuCustomisationOption = "";               //Radio Buttons Visibility after clicking on food items
                mbtnNext.setVisibility(View.VISIBLE);
                mLK.setVisibility(View.GONE);
                disableRadioGroup(mLK);
                mOPS.setVisibility(View.GONE);
                disableRadioGroup(mOPS);
                mGSL.setVisibility(View.GONE);
                disableRadioGroup(mGSL);
            }
        });

        mLambKorma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLK.setVisibility(View.VISIBLE);
                enableRadioGroup(mLK);

                mLambKorma.setPadding(0,0,0,0);
                mGreenSalad.setPadding(20,20,20,20);
                mBeefNoodle.setPadding(20,20,20,20);
                mOpenChicken.setPadding(20,20,20,20);

                mFlag = mLK;
                mstrMenuOption = "Lamb Korma";
                mstrMenuCustomisationOption = "";

                mbtnNext.setVisibility(View.VISIBLE);
                mGSL.setVisibility(View.GONE);
                disableRadioGroup(mGSL);
                mOPS.setVisibility(View.GONE);
                disableRadioGroup(mOPS);
                mBNS.setVisibility(View.GONE);
                disableRadioGroup(mBNS);
            }
        });

        mOpenChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOPS.setVisibility(View.VISIBLE);
                enableRadioGroup(mOPS);

                mOpenChicken.setPadding(0,0,0,0);
                mGreenSalad.setPadding(20,20,20,20);
                mBeefNoodle.setPadding(20,20,20,20);
                mLambKorma.setPadding(20,20,20,20);

                mFlag = mOPS;
                mstrMenuOption = "Open Chicken Sandwich";
                mstrMenuCustomisationOption = "";

                mbtnNext.setVisibility(View.VISIBLE);
                mLK.setVisibility(View.GONE);
                disableRadioGroup(mLK);
                mGSL.setVisibility(View.GONE);
                disableRadioGroup(mGSL);
                mBNS.setVisibility(View.GONE);
                disableRadioGroup(mBNS);
            }
        });

        mGreenSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGSL.setVisibility(View.VISIBLE);
                enableRadioGroup(mGSL);

                mGreenSalad.setPadding(0,0,0,0);
                mOpenChicken.setPadding(20,20,20,20);
                mBeefNoodle.setPadding(20,20,20,20);
                mLambKorma.setPadding(20,20,20,20);

                mFlag = mGSL;
                mstrMenuOption = "Green Salad Lunch";
                mstrMenuCustomisationOption = "";

                mbtnNext.setVisibility(View.VISIBLE);
                mLK.setVisibility(View.GONE);
                disableRadioGroup(mLK);
                mOPS.setVisibility(View.GONE);
                disableRadioGroup(mOPS);
                mBNS.setVisibility(View.GONE);
                disableRadioGroup(mBNS);
            }
        });

        mbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mstrMenuOption.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please select an option from the menu above.",Toast.LENGTH_LONG);
                }
                else{
                    if (mFlag!=null) {
                        mstrMenuCustomisationOption = getRadioButtonText(mFlag);
                        if (!mstrMenuCustomisationOption.isEmpty()) {
                            Intent intent = new Intent(OrderActivity.this, TimingsActivity.class);
                            intent.putExtra("MEAL_NAME", mstrMenuOption);
                            intent.putExtra("MEAL_OPTION", mstrMenuCustomisationOption);
                            intent.putExtra("LOCATION", LOCATION);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

    }

    private String getRadioButtonText(RadioGroup radioGroup){
        int selectedId=radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    OrderActivity.this);
            builder.setTitle("Error");
            builder.setMessage("Please select a valid meal option from the menu.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        else{
            radioButton=(RadioButton)findViewById(selectedId);
            return radioButton.getText().toString();
        }
        return "";
    }
    public void disableRadioGroup(RadioGroup radioGroup){
        for (int i = 0; i <radioGroup.getChildCount(); i++)
        {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    public void enableRadioGroup(RadioGroup radioGroup){
        for (int i = 0; i <radioGroup.getChildCount(); i++)
        {
            radioGroup.getChildAt(i).setEnabled(true);
        }
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
            startActivity(new Intent(OrderActivity.this,RecentOrdersActivity.class));
        }
        else if(id == R.id.action_settings){
            // do something
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

}
