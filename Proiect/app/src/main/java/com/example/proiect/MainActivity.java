package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button register;
    public static EditText email;
    public EditText password;
    private Button login;
    CheckBox remember;
    SharedPreferences mySharedPref;
    SharedPreferences.Editor mySharedPrefEditor;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.button36);
        register.setOnClickListener(this);

        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2);
        login = findViewById(R.id.button35);
        login.setOnClickListener(this);

        remember = findViewById(R.id.checkBox);

        mAuth = FirebaseAuth.getInstance();

        mySharedPref = MainActivity.this.getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        mySharedPrefEditor = mySharedPref.edit();

        String autoFill = mySharedPref.getString("rememberUser", "crocozaur");

        if (autoFill.equals("True")) {
            email.setText(mySharedPref.getString("user", "nil"));
            password.setText(mySharedPref.getString("pass", "nil"));
            remember.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button36:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.button35:
                userLogin();
                break;
        }
    }

    public void Switch(){
        startActivity(new Intent(this, MainMenu2.class));
    }

    private void userLogin() {
        String em = email.getText().toString().trim(), pass = password.getText().toString().trim();

        if (em.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Email is not valid!");
            email.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("Email is not valid!");
            password.requestFocus();
            return;
        }

        if (pass.length() < 6){
            password.setError("Password too short");
            password.requestFocus();
            return;
        }

        if (remember.isChecked()) {
            mySharedPrefEditor.putString("user", email.getText().toString());
            mySharedPrefEditor.putString("pass", password.getText().toString());
            mySharedPrefEditor.putString("rememberUser", "True");
            mySharedPrefEditor.commit();
        } else {
            mySharedPrefEditor.putString("rememberUser", "False");
            mySharedPrefEditor.commit();
        }

        mAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Switch();
                }else Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}