package com.example.testonoff;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    public static final String DB_NAME = "ACLOCAL.db";
    public static String DB_PATH = "";
    private Context mContext;

    public Database(Context context){
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        this.DB_PATH = "databases/";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

//    public void openDatabase() {
//        String dbPath = mContext.getDatabasePath(DBName).getPath();
//        if(mDatabase != null && mDatabase.isOpen()) {
//            return;
//        }
//        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
//    }

//    public void closeDatabase() {
//        if(database!=null) {
//            database.close();
//        }
//    }
    public ArrayList<String> getAllData(){
        database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("SELECT DISTINCT brand FROM remote", null);
        ArrayList<String> aList = new ArrayList<String>();
        while (cursor.moveToNext()){
            aList.add(cursor.getString(1));
        }
        cursor.close();

        return aList;
    }


}
