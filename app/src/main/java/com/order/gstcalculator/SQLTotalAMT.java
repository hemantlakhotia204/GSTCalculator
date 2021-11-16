package com.order.gstcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class SQLTotalAMT extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AMOUNT_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "AMT_TABLE";

    private static final String KEY_ID = "_id";
    private static final String KEY_QUANTITY = "_quantity";
    private static final String KEY_LIST_PRICE = "_list_price";
    private static final String KEY_DISC = "_disc";
    private static final String KEY_PRICE = "_price";
    private static final String KEY_TAXABLE_VALUE = "taxable_value";
    private static final String KEY_TAX_RATE = "tax_rate";
    private static final String KEY_GST = "_gst";
    private static final String KEY_TOTAL = "_total";


    public SQLTotalAMT(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUANTITY + " FLOAT NOT NULL, " +
                KEY_LIST_PRICE + " FLOAT NOT NULL, " + KEY_DISC + " FLOAT, " + KEY_PRICE + " FLOAT NOT NULL,  " + KEY_TAXABLE_VALUE + " FLOAT NOT NULL, " +
                KEY_TAX_RATE + " FLOAT NOT NULL, " + KEY_GST + " FLOAT NOT NULL, " + KEY_TOTAL + " FLOAT NOT NULL);";
        db.execSQL(createDb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    void deleteDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


    int deleteRow(int index) {

        SQLiteDatabase db = this.getWritableDatabase();


        return db.delete(DATABASE_TABLE, KEY_ID + "=" + index, null);
    }



    void setBill(float quantity, float listPrice, float discount, float price, float taxableValue, float taxRate, float GST, float totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try {
            cv.put(KEY_QUANTITY, quantity);
            cv.put(KEY_LIST_PRICE, listPrice);
            cv.put(KEY_DISC, discount);
            cv.put(KEY_PRICE, price);
            cv.put(KEY_TAXABLE_VALUE, taxableValue);
            cv.put(KEY_TAX_RATE, taxRate);
            cv.put(KEY_GST, GST);
            cv.put(KEY_TOTAL, totalAmount);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.insert(DATABASE_TABLE, null, cv);
        db.close();
    }

    Cursor getCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID, KEY_QUANTITY, KEY_LIST_PRICE, KEY_DISC, KEY_PRICE, KEY_TAXABLE_VALUE, KEY_TAX_RATE, KEY_GST, KEY_TOTAL};
        return db.query(DATABASE_TABLE, columns, null, null, null, null, null);

    }




}
