package com.example.thecrazymonkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email, pass;
    Button login;
    //TextView cAccount;
    FirebaseAuth fAuth;
    String userID;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        fAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        //cAccount = findViewById(R.id.textview2);

        userID = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();

        if(user.isEmailVerified())
        {
         Toast.makeText(MainActivity.this,"User Email verified",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick7(View view) {
        {
            String Email = email.getText().toString().trim();
            String Pass = pass.getText().toString().trim();

            if (TextUtils.isEmpty(Email)) {
                email.setError("Email is Required");
                return;
            }

            if (TextUtils.isEmpty(Pass)) {
                pass.setError("Password is Required");
                return;
            }

            if (Pass.length() < 4) {
                pass.setError("Password must be 4 characters or long ");
                return;
            }


            fAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(MainActivity.this, "This User Not Found , Create A New Account", Toast.LENGTH_SHORT).show();
                    }
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(MainActivity.this, "The Password Is Invalid, Please Try Valid Password", Toast.LENGTH_SHORT).show();
                    }
                    if (e instanceof FirebaseNetworkException) {
                        Toast.makeText(MainActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
    public void onBackPressed() {
        //put the AlertDialog code here
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // exit
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to logout
                    }
                })
                .show();
    }


    public void onClick6(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));


    }
}