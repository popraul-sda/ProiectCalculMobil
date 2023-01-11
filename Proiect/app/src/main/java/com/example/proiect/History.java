package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    TextView text;
    Button back;
    private final String FILENAME = "history.txt";
    ListView listView;
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        listView = findViewById(R.id.listView);
        text = findViewById(R.id.textView3);
        back = findViewById(R.id.button33);

        myAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(myAdapter);

        load();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu2.class);
                startActivity(intent);
            }
        });
    }

    public void load() {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String savedText = "";

            while((savedText=bufferedReader.readLine()) != null){
                stringBuilder.append(savedText).append("\n");
                if (!savedText.equals("")){
                    names.add(savedText);
                    myAdapter.notifyDataSetChanged();
                }
                //text2.setText(text2.getText() + "\n" + savedText + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}