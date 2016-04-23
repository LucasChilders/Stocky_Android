package com.lucaschilders.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OutputActivity extends AppCompatActivity {

    TextView tickerLabel, outputLabel, priceChangeLabel, stockVolumeLabel, stockLowHighLabel, stockPrevCloseLabel;
    private NumberFormat nf = NumberFormat.getInstance();
    DecimalFormat df = new DecimalFormat("#0.00");
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        tickerLabel = (TextView) findViewById(R.id.ticker);
        outputLabel = (TextView) findViewById(R.id.price);
        priceChangeLabel = (TextView) findViewById(R.id.priceChange);
        stockVolumeLabel = (TextView) findViewById(R.id.stockVolume);
        stockLowHighLabel = (TextView) findViewById(R.id.stockLowHigh);
        stockPrevCloseLabel = (TextView) findViewById(R.id.stockPrevClose);

        Bundle bundle = getIntent().getExtras();
        String symbol = bundle.getString("symbol");

        Stock stock = StockFetcher.getStock(symbol);

        double today = stock.getPrice();
        double yesterday = stock.getPreviousClose();
        double change = today - yesterday;
        String changeStr;

        this.setTitle("My Stocks - " + stock.getSymbol());

        tickerLabel.setText(stock.getName() + "\n" + stock.getSymbol());
        outputLabel.setText(String.valueOf(stock.getPrice()));

        if (stock.getPreviousClose() > stock.getPrice()) {
            outputLabel.setTextColor(Color.RED);
        }
        else if (stock.getPreviousClose() < stock.getPrice()) {
            outputLabel.setTextColor(Color.parseColor("#0ac300"));
        }

        if (change < 0) {
            priceChangeLabel.setText("-" + df.format(change * -1) + " (-" + df.format((change * -1) / stock.getPrice() * 100) + "%)");
            change *= -1;
            changeStr = "-" + formatter.format(change).substring(1);
        }
        else {
            changeStr = formatter.format(change).substring(1);
            priceChangeLabel.setText(df.format(change)  + " (" + df.format((change) / stock.getPrice() * 100) + "%)");
        }

        //Volume traded
        stockVolumeLabel.setText("Volume: " + nf.format(stock.getVolume()));

        //Set stock range for the day
        stockLowHighLabel.setText("Day Low/High: " + formatter.format(stock.getDaylow()) + " - " + formatter.format(stock.getDayhigh()));

        //Yesterday closing price
        stockPrevCloseLabel.setText("Yesterday's Close: " + formatter.format(stock.getPreviousClose()));
    }
}
