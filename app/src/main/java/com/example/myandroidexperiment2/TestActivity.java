package com.example.myandroidexperiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myandroidexperiment2.Model.Word;


public class TestActivity extends AppCompatActivity {

    private static final String TAG="MyWordsTag";
    private ContentResolver resolver;
    Button all,delete,deleteAll,update,add,search,back;
    static WordDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        mDbHelper = new WordDBHelper(this);
        resolver = this.getContentResolver();


//        System.out.println(Word.WordTest._ID);
        all = findViewById(R.id.all);
        delete = findViewById(R.id.delete);
        deleteAll = findViewById(R.id.deleteAll);
        update = findViewById(R.id.update);
        add = findViewById(R.id.add);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                Cursor cursor = resolver.query(Word.CONTENT_URI,new String[]{
                        "id","word","meaning","sample"},null,null,null);
                if(cursor == null){
                    Toast.makeText(TestActivity.this, "没有找到记录", Toast.LENGTH_SHORT).show();
                    return;
                }

                String msg = "";
                while(cursor.moveToNext()){
                    msg += "id:" + cursor.getString(cursor.getColumnIndex("id")) + "\n";
                    msg += "word: " + cursor.getString(cursor.getColumnIndex("word")) + "\n";
                    msg += "meaning: " + cursor.getString(cursor.getColumnIndex("meaning")) + "\n";
                    msg += "sample: " + cursor.getString(cursor.getColumnIndex("sample")) + "\n";
                }
                Log.v(TAG,msg);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = "3";
//                Uri uri = Uri.parse(Word.CONTENT_URI_STRING + "/" + id);
                Uri uri = Uri.parse(Word.CONTENT_URI_STRING + "/" + id);
                int result = resolver.delete(uri,null,null);
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolver.delete(Word.CONTENT_URI, null, null);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = "3";
                String word = "banana";
                String meaning = "Banana";
                String sample = "This is a Banana";

                ContentValues values = new ContentValues();
                values.put("word",word);
                values.put("meaning",meaning);
                values.put("sample",sample);
                Uri uri = Uri.parse(Word.CONTENT_URI_STRING + "/" + id);
//                Uri uri = Uri.parse(Word.CONTENT_URI_STRING + "/" + id);
                int result = resolver.update(uri,values,null,null);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String id = MainActivity.getTime();
                String word = "watermelon";
                String meaning = "Watermelon";
                String sample = "This is a watermelon";

                ContentValues values = new ContentValues();
                values.put("id","3");
                values.put("word",word);
                values.put("meaning",meaning);
                values.put("sample",sample);

                Uri newUri = resolver.insert(Word.CONTENT_URI,values);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String id = "3";
                Uri uri = Uri.parse(Word.CONTENT_URI_STRING + "/" + id);
                Cursor cursor = resolver.query(uri,new String[]{
                        "id","word","meaning","sample"},null,null,null);
                if(cursor == null){
                    Toast.makeText(TestActivity.this, "没有找到记录", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = "";
                while(cursor.moveToNext()){
                    msg += "id:" + cursor.getString(cursor.getColumnIndex("id")) + "\n";
                    msg += "word: " + cursor.getString(cursor.getColumnIndex("word")) + "\n";
                    msg += "meaning: " + cursor.getString(cursor.getColumnIndex("meaning")) + "\n";
                    msg += "sample: " + cursor.getString(cursor.getColumnIndex("sample")) + "\n";
                }
                Log.v(TAG,msg);
            }
        });


    }
}