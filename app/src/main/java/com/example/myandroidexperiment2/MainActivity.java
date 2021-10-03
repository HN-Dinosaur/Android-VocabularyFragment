package com.example.myandroidexperiment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidexperiment2.Model.Word;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    OnChangeListener onChangeListener;
    List<Word> words;
    List<String> wordNames = new ArrayList<>();
    ListView listView;

    Adapter adapter;
    static WordDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建SQL HELP对象，第一次运行时数据库并没有被创建
        mDbHelper = new WordDBHelper(this);

        //将数据库中的单词取出
        words = getAll();
//        //在列表显示所有单词
//
        setWordListView(words);
//
        //设置Detail布局
        setWordDetailView();
//
        //设置列表点击响应
        setListClick();
//
        //为ListView注册上下文菜单
        registerForContextMenu(listView);

    }
    void setWordListView(List<Word> words){
        wordNames = new ArrayList<>();
        for(int i = 0; i < words.size(); i++){
            wordNames.add(words.get(i).getWord());
        }
        listView = findViewById(R.id.listView);
        adapter = new Adapter(wordNames,this);
        listView.setAdapter(adapter);
    }
    void setWordDetailView(){
        DetailFragment detailFragment = new DetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, detailFragment);
        fragmentTransaction.commit();
    }
    void setListClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedPosition(i);
                adapter.notifyDataSetInvalidated();

                if(onChangeListener != null){
                    onChangeListener.changeText(words.get(i));
                }
            }
        });
    }


    //创建长按菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu_wordslistview, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = null;
        switch (item.getItemId()){
            case R.id.delete:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                deleteDialog(words.get(info.position).getId());
                break;
            case R.id.modify:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Word word = words.get(info.position);
                updateDialog(word.getId(),word.getWord(), word.getMeaning(), word.getSample(), info.position);
                break;
        }
        return true;
    }

    //创建右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.operate_vocabulary, menu);
        return true;
    }
    //点击右上角菜单
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.search:
                searchDialog();
                break;
            case R.id.add:
                insertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //得到数据库内的所有数据
    List<Word> getAll(){
        String sql = "select * from words";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{});
        List<Word> insertWordList = new ArrayList<>();
        Word insertWordClass;
        while(cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex("word"));
            @SuppressLint("Range") String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            @SuppressLint("Range") String sample = cursor.getString(cursor.getColumnIndex("sample"));
            insertWordClass = new Word(id, word, meaning, sample);
            insertWordList.add(insertWordClass);
        }
        return insertWordList;
    }

    //插入
    private void insertUserSql(String id, String word, String meaning, String sample){
        String sql = "insert into words(id, word, meaning, sample) values(?,?,?,?)";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{id, word, meaning, sample});
    }
    //插入的Dialog
    private void insertDialog(){
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);;
        new AlertDialog.Builder(this)
                .setTitle("新增单词")
                .setView(linearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String insertWord = ((EditText)linearLayout.findViewById(R.id.insertWord)).getText().toString();
                        String insertMeaning = ((EditText)linearLayout.findViewById(R.id.insertMeaning)).getText().toString();
                        String insertSample = ((EditText)linearLayout.findViewById(R.id.insertSample)).getText().toString();
                        String insertId = getTime();
                        insertUserSql(insertId ,insertWord, insertMeaning, insertSample);

                        words = getAll();
                        //显示出来
                        setWordListView(words);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    private void delete(String id){
        String sql = "delete from words where id = '" + id + "'";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql);
    }
    private void deleteDialog(final String id){
        new AlertDialog.Builder(this)
                .setTitle("删除单词")
                .setMessage("是否确定删除单词")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(id);

                        words = getAll();
                        setWordListView(words);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    private void update(String id, String word, String meaning, String sample){
        String sql = "update words set word=?,meaning=?,sample=? where id = ?";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{word, meaning, sample, id});
    }
    private void updateDialog(String id, String word, String meaning, String sample, int position){
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) linearLayout.findViewById(R.id.insertWord)).setText(word);
        ((EditText)linearLayout.findViewById(R.id.insertMeaning)).setText(meaning);
        ((EditText)linearLayout.findViewById(R.id.insertSample)).setText(sample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")
                .setView(linearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String modifyWord = ((EditText) linearLayout.findViewById(R.id.insertWord)).getText().toString();
                        String modifyMeaning = ((EditText) linearLayout.findViewById(R.id.insertMeaning)).getText().toString();
                        String modifySample = ((EditText) linearLayout.findViewById(R.id.insertSample)).getText().toString();
                        update(id, modifyWord, modifyMeaning, modifySample);

                        //更新视图
                        words = getAll();
                        //改变左侧列表
                        setWordListView(words);
                        //改变右侧内容
                        onChangeListener.changeText(words.get(i));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    private ArrayList<Word> search(String word){
        String sql = "select * from words where word like ? order by word desc";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery(sql, new String[]{"%" + word + "%"});
        return convertCursorToList(cursor);
    }


    private ArrayList<Word> convertCursorToList(Cursor cursor){
        ArrayList<Word> list = new ArrayList<>();
        Word searchWordClass;
        while(cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex("word"));
            @SuppressLint("Range") String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            @SuppressLint("Range") String sample = cursor.getString(cursor.getColumnIndex("sample"));
            searchWordClass = new Word(id, word, meaning, sample);
            list.add(searchWordClass);
        }
        return list;
    }

    private void searchDialog(){
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.search, null);
        new AlertDialog.Builder(this)
                .setTitle("搜索")
                .setView(linearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String searchWord = ((EditText) linearLayout.findViewById(R.id.searchWord)).getText().toString();
                        ArrayList<Word> results = search(searchWord);


                        if(results.size() > 0){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("results", results);
                            Intent intent = new Intent(MainActivity.this,SearchResultActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this, "没有找到", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
    String getTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddhhmmss");
        return dateFormat.format(date);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        words = getAll();
        setWordListView(words);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }
    void setOnChangeListener(OnChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }
    interface OnChangeListener {
        void changeText(Word word);
    }

}