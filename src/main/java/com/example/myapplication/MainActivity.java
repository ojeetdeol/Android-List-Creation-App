package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    ListView listView;
    ArrayList<String> reminders;
    ArrayAdapter<String> adapter;
    Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView  = findViewById(R.id.listview);
        reminders = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,reminders);
        listView.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.add_reminder_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                EditText editText = (EditText) findViewById(R.id.enter_message);
                String task = editText.getText().toString();
                reminders.add(task);
                listView.setAdapter(adapter);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,PopUpWindow.class);
                intent.putExtra(EXTRA_MESSAGE,reminders.get(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                create_toast("Removed: " + reminders.get(position));
                delete_item(position);
                return false;
            }
        });
    loadContent();
    }
    private void create_toast(String message){
        if(t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        t.show();
    }
    private void delete_item(int i){
        reminders.remove(i);
        listView.setAdapter(adapter);
    }

    public void loadContent(){
        File path = getApplicationContext().getFilesDir();
        File read = new File(path,"list_app_file.txt");
        byte[] content = new byte[(int) read.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(read);
            fis.read(content);

            String s = new String(content);
            s = s.substring(1,s.length()-1);
            String split[] = s.split(", ");
            reminders = new ArrayList<>(Arrays.asList(split));
            adapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,reminders);
            listView.setAdapter(adapter);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        String filename = "list_app_file.txt";
        File file = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(file,filename));
            writer.write(reminders.toString().getBytes());
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
    
}