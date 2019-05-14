package com.jemberdeveloper.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private TextInputLayout input_username,input_password;
    private Button btn_login;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        loading = findViewById(R.id.loading);
        btn_login =findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = input_username.getEditText().getText().toString();
                String password = input_password.getEditText().getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    lanjutLogin(username,password);
                } else {
                    Toast.makeText(LoginActivity.this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void lanjutLogin(String username, final String password) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);
        DatabaseReference ref = database.getReference();
        ref.child("list_nim").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String email = dataSnapshot.child("email").getValue().toString();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Intent kemain = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(kemain);
                            finish();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    loading.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Username tidak terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
