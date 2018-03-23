package com.infinity.weather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.infinity.weather.model.db.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.mazurkevich on 17.08.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    private static final String TABLE_NAME = "weather";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String WOEID = "woeid";


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_DB = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL, "
                + WOEID + " INTEGER NOT NULL);";
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Element> getAllElements(){
        String query = "SELECT  * FROM " + TABLE_NAME;
        List<Element> list = new ArrayList<Element>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Element element = new Element();
                element.setId(Integer.parseInt(cursor.getString(0)));
                element.setName(cursor.getString(1));
                element.setWoeid(cursor.getString(2));
                // Adding element to list
                list.add(element);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void deleteAllElements(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void getElement(){

    }

    public void insertElement(Element element){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, element.getName());
        values.put(WOEID, element.getWoeid());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteElement(Element element){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, WOEID + "= ?", new String[] {element.getWoeid()});
        db.close();
    }
}
