package com.example.myandroidexperiment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "WordsDatabase";
    private final static int DATABASE_VERSION = 1;

    private final static String SQL_CREATE_DATABASE =
            "create table words ( id primary key, word TEXT, meaning TEXT, sample TEXT)";

    private final static String SQL_DELETE_TABLE =
    "DROP TABLE IF EXISTS words";


    public WordDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //数据库升级  删除久表  创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
