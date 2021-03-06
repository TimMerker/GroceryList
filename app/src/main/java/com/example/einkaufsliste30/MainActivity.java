package com.example.einkaufsliste30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mAddButton;
    private RecyclerView mRecView;


    private ImageButton mPlusButton;
    private TextView mCounter;
    private ArrayList<Item> items;
    ItemsRecViewAdapter adapter;
    public final String sharedPreferencesKey = "grocery list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.item_editText);
        mAddButton = findViewById(R.id.add_button);
        adapter = new ItemsRecViewAdapter(MainActivity.this);
        mRecView = findViewById(R.id.itemsRecyclerView);
        mRecView.setAdapter(adapter);
        mRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
        loadData();
        adapter.setItems(items);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditText.getText().toString().equals("")) {
                    Item item = new Item(mEditText.getText().toString());
                    items.add(item);
                    adapter.setItems(items);
                    if (mEditText.length() > 0) {
                        TextKeyListener.clear(mEditText.getText());
                    }
                }
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString(sharedPreferencesKey, json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(sharedPreferencesKey,null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        items = gson.fromJson(json,type);

        if (items == null){
            items = new ArrayList<>();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAllBtn:
                items.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "All items deleted...",Toast.LENGTH_SHORT).show();
                saveData();
                break;
        }
        return true;
    }

}