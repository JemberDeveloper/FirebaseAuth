package com.jemberdeveloper.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if (user == null){
            Intent kelogin = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(kelogin);
            finish();
        }
    }
}
