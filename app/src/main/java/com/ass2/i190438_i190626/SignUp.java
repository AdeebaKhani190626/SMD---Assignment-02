package com.ass2.i190438_i190626;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText name,email,password;
    private CheckBox agree;
    ImageView g1,g2,g3;
    String gender="";
    String terms="";
    String n,em,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_pass);
        agree = findViewById(R.id.checkbox);
        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terms = "true";
            }
        });

        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
            }
        });

        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
            }
        });

        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "unknown";
            }
        });

        final Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Sign Up here
                        if (RegisterUser() && GenderAgree()) {

                            mAuth.createUserWithEmailAndPassword(em,pass)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        User user = new User(n,em,pass,gender);
                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(SignUp.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });
                            startActivity(intent);
                        }
                    }
                },10);
            }
        });

        final Button signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                },10);
            }
        });
    }

    private Boolean GenderAgree() {
        boolean check = true;

        if (gender.equals("male") || gender.equals("female") || gender.equals("unknown")) {
            // Gender = Male or Female or Unknown
            // Selecting the image
        }
        else {
            Toast.makeText(this,"Specify your gender.",Toast.LENGTH_LONG).show();
            check = false;
        }
        if (terms.equals("true")) {
            // Agreed to the terms and conditions
            // Thorugh CheckBox
        }
        else {
            Toast.makeText(this,"Kindly agree to the terms.",Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }

    private Boolean RegisterUser() {

        n = name.getText().toString().trim();
        em = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        boolean checks = true;

        // Applying multiple checks
        if (n.isEmpty()) {
            name.setError("Full name is required!");
            name.requestFocus();
            checks = false;
        }
        if (em.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            checks = false;
        }
        if (pass.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            checks = false;
        }
        if (pass.length() < 6) {
            password.setError("Min Password length should be 6 characters!");
            password.requestFocus();
            checks = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Please provide valid email!");
            email.requestFocus();
            checks = false;
        }
        // returns true if all checks satisfied
        return checks;
    }
}