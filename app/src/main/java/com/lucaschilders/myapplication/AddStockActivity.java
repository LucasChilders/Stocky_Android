package com.lucaschilders.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class AddStockActivity extends AppCompatActivity {

    Button addStock;
    EditText stockSymbol;
    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        addStock = (Button) findViewById(R.id.addStock);
        stockSymbol = (EditText) findViewById(R.id.stockEntry);
        stockSymbol.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        db = new MySQLiteHelper(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> stocks = db.getAllStocks();

                if (stocks.contains(stockSymbol.getText().toString().toUpperCase())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You already have " + stockSymbol.getText().toString().toUpperCase() + " added!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    Stock stock = StockFetcher.getStock(stockSymbol.getText().toString());

                    if (stock.getName().equals("N/A")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "The stock " + stockSymbol.getText().toString().toUpperCase() + " doesn't exist!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else {
                        db.addStock(stockSymbol.getText().toString().toUpperCase());
                        Toast toast = Toast.makeText(getApplicationContext(), stock.getName() + " has been added!", Toast.LENGTH_SHORT);
                        toast.show();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        finish();
                    }
                }
            }
        });
    }
}
