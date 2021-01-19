package com.crazydevstuff.infoshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText email,password,confirmPass,username,phNo;
    Button signUpButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        confirmPass=findViewById(R.id.editTextTextConfirmPassword);
        username=findViewById(R.id.editTextTextUsername);
        phNo=findViewById(R.id.editTextTextUserPhone);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users-list");

        signUpButton=findViewById(R.id.buttonLogin);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }
    void saveUser(RegisterModel user,String uid){
        databaseReference.child(uid).setValue(user);
    }
    void signUp(){
        String emailId=email.getText().toString();
        String pass=password.getText().toString();
        String confirmPassword=confirmPass.getText().toString();
        String user=username.getText().toString();
        String phNumber=phNo.getText().toString();

        if(!emailId.equals("")&&
        !pass.equals("")&&
        !confirmPassword.equals("")&&
        !user.equals("")&&
        !phNumber.equals("")){
            if(emailId.contains("@")){
                if(pass.equals(confirmPassword)&&pass.length()>8){
                    final RegisterModel userObj=new RegisterModel(user,emailId,phNumber);
                    firebaseAuth.createUserWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser user=task.getResult().getUser();
                                Log.i("TEST", user.getUid());
                                saveUser(userObj, user.getUid());
                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                finish();
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"An unexpected error occurred! Please try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    password.setError("Please enter a valid password of length greater than 8 and and confirm your password");
                }
            }else{
                email.setError("Please enter a valid Email");
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
        }
    }
}