package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rahulsinghkamboj.android.tuckbox.Model.Orders;
import com.rahulsinghkamboj.android.tuckbox.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimingsActivity extends AppCompatActivity {

    private String LOCATION;
    private String MEAL_NAME;
    private String MEAL_OPTION;
    private TextView mtvAddress;
    private RadioGroup mTimings;
    private RadioButton mSelectedTime;
    private String mAddress;
    private String mSelectedTimings;
    private CreditCardForm form;
    private String cardNo = "";
    private String cardDate = "";
    private Button mPlaceOrder;
    private RadioButton radioButton;
    private Date Cutofftime ;
    private Date CurrentTime;
    private String cardSecurity = "";
    private FirebaseUser firebaseUser;
                                                                            //Credit Card Validations

    CardValidCallback cardValidCallback = new CardValidCallback() {
        @Override
        public void cardValid(CreditCard card) {
            Log.d("TAG", "valid card: " + card);
            Toast.makeText(TimingsActivity.this, "Card valid and complete", Toast.LENGTH_LONG).show();
            cardNo = card.getCardNumber();
            cardDate = card.getExpDate();
            cardSecurity = card.getSecurityCode();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Order Details");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        LOCATION = getIntent().getStringExtra("LOCATION");
        MEAL_NAME = getIntent().getStringExtra("MEAL_NAME");
        MEAL_OPTION = getIntent().getStringExtra("MEAL_OPTION");
        mPlaceOrder = (Button) findViewById(R.id.place_order);
        mTimings = (RadioGroup) findViewById(R.id.timings);
        form = (CreditCardForm) findViewById(R.id.credit_card_form);
        form.setOnCardValidCallback(cardValidCallback);

        mtvAddress = (TextView) findViewById(R.id.address);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName());
                mtvAddress.setText(place.getAddress());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

                                                                              //Order Placement Restriction after 10:00am
        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddress = mtvAddress.getText().toString();
                mSelectedTimings = getRadioButtonText(mTimings);
                try {

                    Calendar cal = Calendar.getInstance();
                    Date currentLocalTime = cal.getTime();
                    DateFormat date = new SimpleDateFormat("HH:mm ");
                    String localTime = date.format(currentLocalTime);
                    SimpleDateFormat formatter = new SimpleDateFormat( "HH:mm");
                    CurrentTime = formatter.parse(localTime);
                    Cutofftime = formatter.parse("10:00");


                }catch (Exception e){
                    e.printStackTrace();
                }
                if (CurrentTime.compareTo(Cutofftime) != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            TimingsActivity.this);
                    builder.setTitle("Oops");
                    builder.setMessage("Orders after 10 A.M. are not accepted.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
                else {
                    if (mAddress.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TimingsActivity.this);
                        builder.setTitle("Warning");
                        builder.setMessage("Please enter address.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    } else if (mSelectedTimings.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TimingsActivity.this);
                        builder.setTitle("Warning");
                        builder.setMessage("Please select delivery window.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    } else if (cardNo.isEmpty() || cardDate.isEmpty() || cardSecurity.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TimingsActivity.this);
                        builder.setTitle("Warning");
                        builder.setMessage("Please enter Credit Card details");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    } else if (!mAddress.isEmpty() && !mSelectedTimings.isEmpty() && !cardDate.isEmpty() && !cardNo.isEmpty()) {

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("orders").child(firebaseUser.getUid()).push().setValue(new Orders(MEAL_NAME, MEAL_OPTION, LOCATION, mAddress, mSelectedTimings, cardNo + " " + cardDate + " " + cardSecurity));

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TimingsActivity.this);
                        builder.setTitle("Order Placed.");
                        builder.setMessage(MEAL_NAME + " - " + MEAL_OPTION + "\n" + LOCATION + "\n" + mAddress + "\n" + mSelectedTimings);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(TimingsActivity.this, RecentOrdersActivity.class));
                                    }
                                });
                        builder.show();


                    }
                }
          }
        });

    }

    private String getRadioButtonText(RadioGroup radioGroup){
        int selectedId=radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    TimingsActivity.this);
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
            radioButton =(RadioButton)findViewById(selectedId);
            return radioButton.getText().toString();
        }
        return "";
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
            startActivity(new Intent(TimingsActivity.this,RecentOrdersActivity.class));
        }
        else if(id == R.id.action_settings){
            // do something
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
