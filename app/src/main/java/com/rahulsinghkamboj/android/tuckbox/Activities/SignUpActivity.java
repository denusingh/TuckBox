package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rahulsinghkamboj.android.tuckbox.R;
import com.rahulsinghkamboj.android.tuckbox.Utils.TextUtils;


public class SignUpActivity extends AppCompatActivity {

    private EditText eEdtEmailSignUp;
    private EditText eEdtPasswordSignUp;
    private EditText eEdtCPasswordSignUp;
    private Button mBtnSignUp;
    private String mPasswordSignUp;
    private String mCPasswordSignUp;
    private String mEmailSignUp;
    private FirebaseAuth auth;
    private TextInputLayout mtilEmail;
    private TextInputLayout mtilPassword;
    private TextInputLayout mtilCPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");
        auth = FirebaseAuth.getInstance();

        eEdtPasswordSignUp = (EditText) findViewById(R.id.textPassword);
        eEdtCPasswordSignUp = (EditText) findViewById(R.id.textCPassword);
        eEdtEmailSignUp = (EditText) findViewById(R.id.textEmail);
        mtilEmail= (TextInputLayout) findViewById(R.id.til_textEmail);
        mtilPassword = (TextInputLayout) findViewById(R.id.til_textPassword);
        mtilCPassword = (TextInputLayout) findViewById(R.id.til_textCPassword);
        mBtnSignUp = (Button) findViewById(R.id.SignUpButton);



        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailSignUp = eEdtEmailSignUp.getText().toString().trim();
                mPasswordSignUp = eEdtPasswordSignUp.getText().toString().trim();
                mCPasswordSignUp = eEdtCPasswordSignUp.getText().toString().trim();

                if (mEmailSignUp.isEmpty())
                {
                    mtilEmail.setError("Please fill this Field");
                }
                else if (!TextUtils.isValidEmail(mEmailSignUp)){
                    mtilEmail.setError("Not a valid Email address.");
                }
                else if (mPasswordSignUp.isEmpty())
                {
                    mtilEmail.setError(null);
                    mtilPassword.setError("Please fill this Field");
                }
                else if (mCPasswordSignUp.isEmpty())
                {
                    mtilEmail.setError(null);
                    mtilPassword.setError(null);
                    mtilCPassword.setError("Please fill this Field");
                }
                else if (!mCPasswordSignUp.equals(mPasswordSignUp))
                {
                    mtilEmail.setError(null);
                    mtilPassword.setError("Password does not match confirm password.");
                    mtilCPassword.setError("Password does not match confirm password.");
                }
                else{
                    mtilEmail.setError(null);
                    mtilPassword.setError(null);
                    mtilCPassword.setError(null);
                                                                                        //create user
                    auth.createUserWithEmailAndPassword(mEmailSignUp, mPasswordSignUp)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


//                                    progressBar.setVisibility(View.GONE);

                                                                            // If sign in fails, display a message to the user. If sign in succeeds
                                                                            // the auth state listener will be notified and logic to handle the
                                                                            // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
//                                        Toast.makeText(SignUpActivity.this, "Authentication failed." ,
//                                                Toast.LENGTH_SHORT).show();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                SignUpActivity.this);
                                        builder.setTitle("Warning");
                                        builder.setMessage("User already exists with the same Email address.");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.show();

                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));

                                    }
                                }
                            });
                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
