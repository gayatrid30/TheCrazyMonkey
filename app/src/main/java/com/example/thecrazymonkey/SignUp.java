package com.example.thecrazymonkey;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static final String TAG1 = "TAG";
    public static final String TAG = "TAG";
    EditText name, Email, password;
    Button signup,login;
    //TextView loginlink;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        //if(fAuth.getCurrentUser()!= null){
        //     startActivity(new Intent(getApplicationContext(),MainActivity.class));
        //   finish();
        //}

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = Email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                final String fullName = name.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(Password) || Password.length() < 6) {
                    password.setError("Password cannot be less than 6 or empty");
                    return;
                }
                //Register user in firebase

                fAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                 public void onSuccess(Void aVoid) {
                                 Toast.makeText(SignUp.this,"Email verification link is sent.", Toast.LENGTH_SHORT).show();
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                     Log.d(TAG, "onFailure: onFailure: Email InValid ."+e.getMessage());
                              }
                            });

                            Toast.makeText(SignUp.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                            UserID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Users").document(UserID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname", name);
                            user.put("EMail", Email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User Profile Created for " + UserID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUp.this, "This Email/User already exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}
