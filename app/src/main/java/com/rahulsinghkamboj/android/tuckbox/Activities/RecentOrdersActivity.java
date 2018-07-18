package com.rahulsinghkamboj.android.tuckbox.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahulsinghkamboj.android.tuckbox.Adapter.RecentOrderAdapter;
import com.rahulsinghkamboj.android.tuckbox.Model.Orders;
import com.rahulsinghkamboj.android.tuckbox.R;

import java.util.ArrayList;
import java.util.List;

public class RecentOrdersActivity extends AppCompatActivity {


    private DatabaseReference rootRef;
    private FirebaseUser firebaseUser;
    private List<Orders> ordersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecentOrderAdapter recentOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Recent Orders");                    //top bar header

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                                //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

        recentOrderAdapter = new RecentOrderAdapter(ordersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recentOrderAdapter);


        rootRef.child("orders").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Orders orders = data.getValue(Orders.class);
                        ordersList.add(orders);
                    }
                    recentOrderAdapter.notifyDataSetChanged();
                } else {
                                                                // User does not exist at this point.
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


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
            startActivity(new Intent(RecentOrdersActivity.this, RecentOrdersActivity.class));
        }
        else if(id == R.id.action_settings){
            // do something
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
