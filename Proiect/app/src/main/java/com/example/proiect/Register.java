package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText name, phone, email, password;
    private Button btn, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.editTextTextPersonName9);
        phone = findViewById(R.id.editTextTextPersonName10);
        email = findViewById(R.id.editTextTextPersonName11);
        password = findViewById(R.id.editTextTextPersonName12);
        btn = findViewById(R.id.button37);
        btnBack = findViewById(R.id.button38);
        btnBack.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button37:
                registerUser();
                break;
            case R.id.button38:
                goBack();
                break;
        }
    }

    private void goBack() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void registerUser() {
        String nume = name.getText().toString().trim();
        String telefon = phone.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (nume.isEmpty()) {
            name.setError("Full name is required!");
            name.requestFocus();
            return;
        }

        if (telefon.isEmpty()) {
            phone.setError("Phone is required!");
            phone.requestFocus();
            return;
        }

        if (em.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            password.setError("Password should be at least 6 characters");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(em, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(nume, telefon, em);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "User has been registered", Toast.LENGTH_SHORT).show();

                                            } else
                                                Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else
                            Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}