package com.example.myandroidexperiment2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.myandroidexperiment2.Model.Word;

public class WordProvider extends ContentProvider {
    private static final int MULTIPLE_WORDS = 1; //UriMathcher匹配结果码
    private static final int SINGLE_WORD = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(Word.AUTHORITY, Word.PATH_SINGLE, SINGLE_WORD);
        uriMatcher.addURI(Word.AUTHORITY, Word.PATH_MULTIPLE, MULTIPLE_WORDS);
    }
    public WordProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = MainActivity2.mDbHelper.getWritableDatabase();

        int count = 0;
        switch (uriMatcher.match(uri)){
            case MULTIPLE_WORDS:
                count = db.delete("words",selection, selectionArgs);
                break;
            case SINGLE_WORD:
                String whereClause = "id=" + uri.getPathSegments().get(1);
                count = db.delete("words",whereClause,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case MULTIPLE_WORDS:
                return Word.MINE_TYPE_MULTIPLE;
            case SINGLE_WORD:
                return Word.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        System.out.println(uri);
        SQLiteDatabase db = MainActivity2.mDbHelper.getWritableDatabase();

        long id = db.insert("words",null,values);

        if(id > 0){
            //在已有的Uri上加上id
            Uri newUri = ContentUris.withAppendedId(Word.CONTENT_URI,id);
            System.out.println(newUri);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLException("Failed to insert row into" + uri);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = MainActivity2.mDbHelper.getWritableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("words");

        switch (uriMatcher.match(uri)){
            case MULTIPLE_WORDS:
                return db.query("words",projection,selection, selectionArgs, null,null,sortOrder);
            case SINGLE_WORD:
                qb.appendWhere("id=" + uri.getPathSegments().get(1));
                return qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = MainActivity2.mDbHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MULTIPLE_WORDS:
                count = db.update("words",values,selection,selectionArgs);
                break;
            case SINGLE_WORD:
                String segment = "id=" + uri.getPathSegments().get(1);
                count = db.update("words",values,segment,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}