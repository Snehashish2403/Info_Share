package com.crazydevstuff.infoshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    TextView register;
    EditText email;
    EditText password;
    Button login;

    public static String username;
    FirebaseUser firebaseuser;
    List<RegisterModel> users=new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.buttonLogin);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users-list");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    void login(){
        final String emailId=email.getText().toString();
        String pass=password.getText().toString();

        if(!emailId.equals("")&&!pass.equals("")){
            firebaseAuth.signInWithEmailAndPassword(emailId,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseuser=task.getResult().getUser();
                        searchUser(firebaseuser.getUid());
                    }else{
                        Toast.makeText(getApplicationContext(),"Please enter correct details",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    void searchUser(final String userId){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterModel currentUser=snapshot.child(userId).getValue(RegisterModel.class);
                launchMainActivity(currentUser.getEmail(),currentUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void registerUser(){
        startActivity(new Intent(Login.this, SignUp.class));
        finish();
    }
    public void launchMainActivity(String email,String passw){
        Intent intent =new Intent(this,MainActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("username",passw);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseuser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseuser!=null){
            Toast.makeText(getApplicationContext(),"Please wait logging you in",Toast.LENGTH_LONG).show();
            searchUser(firebaseuser.getUid());
        }
    }
}