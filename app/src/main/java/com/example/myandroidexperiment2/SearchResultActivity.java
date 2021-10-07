package com.example.myandroidexperiment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
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

import com.example.myandroidexperiment2.Model.Word;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    List<String> wordNames;
    List<Word> results;
    ListView searchList;
    Adapter adapter;

    MainActivity.OnChangeListener onChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        results = (ArrayList<Word>) getIntent().getExtras().getSerializable("results");
        setWordListView(results);

        //设置FragmentDetail布局
        setWordDetailView();

        //设置列表点击响应
        setListClick();

        //为ListView注册上下文菜单
        registerForContextMenu(searchList);
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
                deleteDialog(results.get(info.position).getId());
                break;
            case R.id.modify:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Word word = results.get(info.position);
                updateDialog(word.getId(), word.getWord(), word.getMeaning(), word.getSample(),info.position);
                break;
        }
        return true;
    }
    //创建右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_to_main, menu);
        return true;
    }
    //点击右上角菜单
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //返回
        finish();
        return super.onOptionsItemSelected(item);
    }
    private void delete(String id){
        String sql = "delete from words where id = '" + id + "'";
        SQLiteDatabase db = MainActivity.mDbHelper.getWritableDatabase();
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

                        updateDeleteResults(id);
                        //改变左侧列表
                        setWordListView(results);
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
        SQLiteDatabase db = MainActivity.mDbHelper.getWritableDatabase();
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

                        updateModifyResults(id, modifyWord, modifyMeaning, modifySample);
                        //改变左侧列表
                        setWordListView(results);
                        //改变右侧内容
                        onChangeListener.changeText(results.get(position));
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
    void setWordListView(List<Word> words){
        wordNames = new ArrayList<>();
        for(int i = 0; i < words.size(); i++){
            wordNames.add(words.get(i).getWord());
        }
        searchList = findViewById(R.id.searchListView);
        adapter = new Adapter(wordNames,this);
        searchList.setAdapter(adapter);
    }
    void setWordDetailView(){
        SearchFragment searchFragment = new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.searchFrameLayout, searchFragment);
        fragmentTransaction.commit();
    }
    void updateModifyResults(String id, String word, String meaning, String sample){
        Word updateWord;
        for(int i = 0; i < results.size(); i++){
            if(results.get(i).getId().equals(id)){
                updateWord = new Word(id, word, meaning, sample);
                results.set(i, updateWord);
            }
        }
    }
    void updateDeleteResults(String id){
        for(int i = 0; i < results.size(); i++){
            if(results.get(i).getId().equals(id)){
                results.remove(i);
            }
        }
    }
    void setListClick(){
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedPosition(i);
                adapter.notifyDataSetInvalidated();

                if(onChangeListener != null){
                    onChangeListener.changeText(results.get(i));
                }
            }
        });
    }
    void setOnChangeListener(MainActivity.OnChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }
}