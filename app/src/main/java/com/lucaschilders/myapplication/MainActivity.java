package com.lucaschilders.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Stock stock;
    ArrayList<String> stocks;
    FloatingActionButton addButton, deleteButton;
    ListAdapter adapter;
    MySQLiteHelper db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        stocks = new ArrayList<>();
        db = new MySQLiteHelper(this);

        loadStockList();

        setContentView(R.layout.activity_main);
        this.setTitle("Stocky");

        input = (EditText) findViewById(R.id.symbolInput);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent intent = new Intent(MainActivity.this, OutputActivity.class);
                    intent.putExtra("symbol", String.valueOf(input.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStockActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoveStockActivity.class);
                startActivity(intent);
            }
        });

        refreshAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loadStockList();
        refreshAdapter();
    }

    public void loadStockList() {
        stocks.clear();
        stocks = db.getAllStocks();
    }

    public void refreshAdapter() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stockSymbol = String.valueOf(parent.getItemAtPosition(position));
                Intent intent = new Intent(MainActivity.this, OutputActivity.class);
                intent.putExtra("symbol", String.valueOf(stockSymbol));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
