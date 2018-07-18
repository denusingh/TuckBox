package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rahulsinghkamboj.android.tuckbox.R;
import com.rahulsinghkamboj.android.tuckbox.Utils.TextUtils;

public class SignInActivity extends AppCompatActivity {

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnSignIn;
    private String mEmail;
    private String mPassword;
    private FirebaseAuth auth;
    private SignInButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;             //code returned on successful authentication with google sign in
    private TextInputLayout mtilEmail;
    private TextInputLayout mtilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign In");
        mEdtEmail = (EditText) findViewById(R.id.textEmailSignIn);
        mEdtPassword = (EditText) findViewById(R.id.textPasswordSignIn);
        mBtnSignIn = (Button) findViewById(R.id.loginButton);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mtilEmail = (TextInputLayout) findViewById(R.id.til_Email);
        mtilPassword = (TextInputLayout) findViewById(R.id.til_Password);


                                                            // Configure sign-in to request the user's ID, email address, and basic
                                                            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        //        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                                                            // Build a GoogleApiClient with access to the Google Sign-In API and the
                                                            // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(SignInActivity.this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // your code here
                        Toast.makeText(getApplicationContext(),"Connection Failed.",Toast.LENGTH_LONG);
                    }
                }/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

                                                                        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEdtEmail.getText().toString();
                mPassword = mEdtPassword.getText().toString();
                if (mEmail.isEmpty()) {
                    mtilEmail.setErrorEnabled(true);
                    mtilEmail.setError("This field cannot be empty.");
                }
                else if (!TextUtils.isValidEmail(mEmail)) {
                    mtilEmail.setErrorEnabled(true);
                    mtilEmail.setError("Invalid Email Address.");
                }
                else if(mPassword.isEmpty()){
                    mtilEmail.setError(null);
                    mtilEmail.setErrorEnabled(false);
                    mtilPassword.setErrorEnabled(true);
                    mtilPassword.setError("This field cannot be empty.");
                }
                else{
                    mtilEmail.setError(null);
                    mtilEmail.setErrorEnabled(false);
                    mtilPassword.setError(null);
                    mtilPassword.setErrorEnabled(false);
                    //authenticate user
                    auth.signInWithEmailAndPassword(mEmail, mPassword)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                            // If sign in fails, display a message to the user. If sign in succeeds
                                                            // the auth state listener will be notified and logic to handle the
                                                            // signed in user can be handled in the listener.
//                                    progressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                SignInActivity.this);
                                        builder.setTitle("Error");
                                        builder.setMessage("Something went wrong. Please check your details and try again.");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.show();

                                    } else {
                                        Toast.makeText(getApplicationContext(),"Login Successful!", Toast.LENGTH_LONG);
                                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                                                        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Intent intent= new Intent(SignInActivity.this,HomeActivity.class);
                            startActivity(intent);
                        } else {
                                                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

//     [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
                                                                // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }
    }
}
