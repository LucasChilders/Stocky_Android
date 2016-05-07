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

public class RemoveStockActivity extends AppCompatActivity {

    Button removeButton;
    EditText stockSymbol;
    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_stock);

        removeButton = (Button) findViewById(R.id.removeButton);
        stockSymbol = (EditText) findViewById(R.id.stockSymbolLookup);
        stockSymbol.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        db = new MySQLiteHelper(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> stocks = db.getAllStocks();

                if (stocks.contains(stockSymbol.getText().toString().toUpperCase())) {
                    db.deleteStock(stockSymbol.getText().toString().toUpperCase());
                    Toast toast = Toast.makeText(getApplicationContext(), "The stock " + stockSymbol.getText().toString().toUpperCase() + " was deleted.", Toast.LENGTH_SHORT);
                    toast.show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    finish();
                }

                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "The stock " + stockSymbol.getText().toString().toUpperCase() + " was not found.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
