package com.lucaschilders.myapplication;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Stock";
    private static final String TABLE_STOCKS = "stocks";
    private static final String KEY_STOCK = "stock";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "SQLiteAppLog";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE stocks ( " + "stock TEXT )";

        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");

        this.onCreate(db);
    }

    public void addStock(String stock){
        Log.d(TAG, "addBook() - " + stock);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK, stock);

        db.insert(TABLE_STOCKS, null, values);

        db.close();
    }

    public ArrayList<String> getAllStocks() {
        ArrayList<String> stocks = new ArrayList<String>();

        String query = "SELECT  * FROM " + TABLE_STOCKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                stocks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "getAllBooks() - " + stocks);

        return stocks;
    }

    public void deleteStock(String stock) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_STOCKS, KEY_STOCK+" = ?", new String[] { stock });

        db.close();

        Log.d(TAG, "deleteBook() - " + stock);
    }
}