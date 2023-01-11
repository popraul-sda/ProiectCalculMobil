package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenu2 extends AppCompatActivity implements View.OnClickListener{

    ImageView g, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu2);

        g = findViewById(R.id.imageView);
        h = findViewById(R.id.imageView2);
        g.setOnClickListener(this);
        h.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageView:
                startActivity(new Intent(this, GameActivity.class));
                break;
            case R.id.imageView2:
                startActivity(new Intent(this, History.class));
                break;
        }
    }
}