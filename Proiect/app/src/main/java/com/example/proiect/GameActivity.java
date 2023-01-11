package com.example.proiect;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    EditText ed00, ed01, ed02, ed03, ed04,
            ed10, ed11, ed12, ed13, ed14,
            ed20, ed21, ed22, ed23, ed24,
            ed30, ed31, ed32, ed33, ed34,
            ed40, ed41, ed42, ed43, ed44;
    Button btnOk, btnDelete, btnPopBack, btnPopRestart, btnBack;
    ArrayList<EditText> positions = new ArrayList<>();
    int index = 0, level = 5, maxLevel = 25, numberOfTries = 0;
    boolean gameOver = false;
    String gameStatus = "";
    HashMap<Integer, String> words = new HashMap<>();
    HashMap<Integer, String> targetWord = new HashMap<>();
    Dialog myDialog;
    TextView dialogX, popUpMessage;
    ArrayList<String> history = new ArrayList<>();
    private final String FILENAME = "history.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ed00 = findViewById(R.id.ed00);
        ed01 = findViewById(R.id.ed01);
        ed02 = findViewById(R.id.ed02);
        ed03 = findViewById(R.id.ed03);
        ed04 = findViewById(R.id.ed04);
        ed10 = findViewById(R.id.ed10);
        ed11 = findViewById(R.id.ed11);
        ed12 = findViewById(R.id.ed12);
        ed13 = findViewById(R.id.ed13);
        ed14 = findViewById(R.id.ed14);
        ed20 = findViewById(R.id.ed20);
        ed21 = findViewById(R.id.ed21);
        ed22 = findViewById(R.id.ed22);
        ed23 = findViewById(R.id.ed23);
        ed24 = findViewById(R.id.ed24);
        ed30 = findViewById(R.id.ed30);
        ed31 = findViewById(R.id.ed31);
        ed32 = findViewById(R.id.ed32);
        ed33 = findViewById(R.id.ed33);
        ed34 = findViewById(R.id.ed34);
        ed40 = findViewById(R.id.ed40);
        ed41 = findViewById(R.id.ed41);
        ed42 = findViewById(R.id.ed42);
        ed43 = findViewById(R.id.ed43);
        ed44 = findViewById(R.id.ed44);
        btnOk = findViewById(R.id.btnOk);
        btnDelete = findViewById(R.id.btnDelete);
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);
        dialogX = myDialog.findViewById(R.id.dialogX);
        popUpMessage = myDialog.findViewById(R.id.textView);
        btnPopBack = myDialog.findViewById(R.id.button25);
        btnPopRestart = myDialog.findViewById(R.id.button14);
        btnBack = findViewById(R.id.button);

        positions.add(ed00);
        positions.add(ed01);
        positions.add(ed02);
        positions.add(ed03);
        positions.add(ed04);
        positions.add(ed10);
        positions.add(ed11);
        positions.add(ed12);
        positions.add(ed13);
        positions.add(ed14);
        positions.add(ed20);
        positions.add(ed21);
        positions.add(ed22);
        positions.add(ed23);
        positions.add(ed24);
        positions.add(ed30);
        positions.add(ed31);
        positions.add(ed32);
        positions.add(ed33);
        positions.add(ed34);
        positions.add(ed40);
        positions.add(ed41);
        positions.add(ed42);
        positions.add(ed43);
        positions.add(ed44);
        loadDictionary();
        getTargetWord();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameOver == false){
                    if ((index == level || index == 24) && level <= maxLevel) {
                        String word = "";
                        for (int i = level - 5; i < level; i++) {
                            word += positions.get(i).getText().toString();
                        }
                        if (checkWord(word.toLowerCase())) {
                            changeText();
                            numberOfTries++;
                            if(level == 25) {
                                gameOver = true;
                                popUpMessage.setText("Try again! The word was : "+ getTarget());
                                myDialog.show();
                                gameStatus = "Lost";
                                load();
                                writeToFile();
                            }
                            else level += 5;
                        } else
                            Toast.makeText(GameActivity.this, "Word doesn't exist!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, "Word isn't long enough!", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameOver) {
                    if (index >= level - 5 && index <= level && level < maxLevel) {
                        positions.get(index).setText("");
                        if (index != level - 5) index--;
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu2.class);
                startActivity(intent);
            }
        });

        dialogX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        btnPopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu();
            }
        });

        btnPopRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
    }

    public void writeLetter(View v){
        if(gameOver == false){
            if(index < level && level <= maxLevel) {
                Button clickedButton = (Button) v;
                positions.get(index).append(clickedButton.getText());
                if(index != 24 ) index++;
            }
        }
    }

    public boolean checkWord(String word){
        if(words.containsValue(word)) return true;
        return false;
    }

    public void loadDictionary(){
        BufferedReader reader = null;
        int i = 0;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getBaseContext().getResources().openRawResource(R.raw.words)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                words.put(i, mLine);
                i++;
            }
        } catch (IOException e) {
            //log the exception
            Toast.makeText(this, "CAN'T OPEN FILE!", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    public void getTargetWord(){
        Random rand = new Random();

        int randomNum = rand.nextInt((5757) + 1);

        for (int i = 0; i < 5; i++){
            targetWord.put(i, String.valueOf(words.get(randomNum).charAt(i)));
        }
    }

    public void changeText(){
        String aux;
        int indexTarget = 0, correctLetters = 0;
        for (int i = level - 5; i < level; i++){
            positions.get(i).setTypeface(null, Typeface.BOLD);
            aux = positions.get(i).getText().toString().toLowerCase();
            if(targetWord.containsValue(aux)) {
                if (targetWord.get(indexTarget).equals(aux)){
                    positions.get(i).setTextColor(Color.GREEN);
                    correctLetters++;
                }
                else positions.get(i).setTextColor(Color.YELLOW);
            }
            indexTarget++;
        }

        if(correctLetters == 5) {
            gameOver = true;
            myDialog.show();
            gameStatus = "Won";
            load();
            writeToFile();
        }
    }

    public void backToMainMenu(){
        myDialog.dismiss();
        Intent intent = new Intent(this, MainMenu2.class);
        startActivity(intent);
    }

    public void restartGame(){
        myDialog.dismiss();
        gameOver = false;
        index = 0;
        numberOfTries = 0;
        level = 5;
        gameStatus = "";
        for(int i = 0; i < positions.size(); i++){
            positions.get(i).setText("");
            positions.get(i).setTypeface(null, Typeface.NORMAL);
            positions.get(i).setTextColor(Color.BLACK);
        }
        getTargetWord();
    }

    public String getTarget(){
        String aux = "";
        for (int i = 0; i < targetWord.size(); i++){
            aux += targetWord.get(i);
        }
        return aux;
    }

    public void writeToFile(){
        String text = MainActivity.email.getText().toString().trim()
                + " ," +gameStatus + " ,word :" + getTarget() + " ,tries: " + numberOfTries
                + "\n" + historyToString();

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(){
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String savedText = "";

            while((savedText=bufferedReader.readLine()) != null){
                stringBuilder.append(savedText).append("\n");
                history.add(savedText + "\n");
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

    public String historyToString(){
        String aux = "";
        for (int i = 0; i < history.size();i++){
            aux += history.get(i) + "\n";
        }
        return aux;
    }
}