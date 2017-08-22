package com.example.fossien.Garanty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fossien.Garanty.data.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fossien on 26/07/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "products";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "marque";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addProduct(Product mProd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, mProd.getName()); // Contact Name
        values.put(KEY_PH_NO, mProd.getMarque()); // Contact marque

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product mProd = new Product(cursor.getString(2), cursor.getString(1), cursor.getInt(0), cursor.getString(0));
        // return contact
        return mProd;
    }

    public List<Product> getAllproducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product mProd = new Product();
                mProd.setPrice(cursor.getInt(0));
                mProd.setName(cursor.getString(1));
                mProd.setMarque(cursor.getString(2));
                // Adding contact to list
                productList.add(mProd);
            } while (cursor.moveToNext());
        }
        // return contact list
        return productList;
    }

    public void deleteProduct(String pName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?", new String[] { pName });
        db.close();
    }
}
