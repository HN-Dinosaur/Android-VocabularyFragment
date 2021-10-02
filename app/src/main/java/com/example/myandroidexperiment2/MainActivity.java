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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidexperiment2.Model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    OnChangeListener onChangeListener;
    List<Word> words;
    List<String> wordNames = new ArrayList<>();
    ListView listView;

    Adapter adapter;
    WordDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = new ArrayList<>();
        init();

        //将数据库中的单词取出
//        words = getAll();

        for(int i = 0; i < words.size(); i++){
            wordNames.add(words.get(i).getWord());
        }
        listView = findViewById(R.id.listView);
        adapter = new Adapter(wordNames,this);
        listView.setAdapter(adapter);

        DetailFragment detailFragment = new DetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, detailFragment);
        fragmentTransaction.commit();



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


        //为ListView注册上下文菜单
        registerForContextMenu(listView);

        //创建SQL HELP对象，第一次运行时数据库并没有被创建
        mDbHelper = new WordDBHelper(this);

        //在列表显示所有单词

//        setWordListView(words);

    }


    //创建长按菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu_wordslistview, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        TextView textId = null;
        TextView textWord = null;
        TextView textMeaning = null;
        TextView textSample = null;

        AdapterView.AdapterContextMenuInfo info = null;
        View itemView = null;

        switch (item.getItemId()){
            case R.id.delete:

                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                deleteDialog(String.valueOf(info.position + 1));
                break;
            case R.id.modify:
                Toast.makeText(MainActivity.this, "modify", Toast.LENGTH_SHORT).show();
//                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//                itemView = info.targetView;
//                textId = itemView.findViewById(R.id.textViewWord);
//                textWord = itemView.findViewById(R.id.textViewWord);
//                textMeaning = itemView.findViewById(R.id.textViewMeaning);
//                textSample = itemView.findViewById(R.id.textViewSample);
//
//                if(textId != null && textWord != null && textMeaning != null && textSample != null){
//                    String strId = textId.getText().toString();
//                    String strWord = textWord.getText().toString();
//                    String strMeaning = textSample.getText().toString();
//                    String strSample = textSample.getText().toString();
//                    UpdataDialog(strId, strWord, strMeaning, strSample);
//                }

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
                Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
//                SerchDialog();
                break;
            case R.id.add:
//                Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
                insertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }


    void init(){

        Word word;
        word = new Word("1","apple","苹果","This is a apple");
        words.add(word);
        word = new Word("2","pear","梨","This is a pear");
        words.add(word);
        word = new Word("3","banana","香蕉","This is a banana");
        words.add(word);
        word = new Word("4","watermelon","西瓜","This is a watermelon");
        words.add(word);
        word = new Word("5","lemon","柠檬","This is a lemon");
        words.add(word);
    }
    //得到数据库内的所有数据
    List<Word> getAll(){
        return null;
    }
    void setOnChangeListener(OnChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }
    interface OnChangeListener{
        void changeText(Word word);
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
                        String word = ((EditText)linearLayout.findViewById(R.id.insertWord)).getText().toString();
                        String meaning = ((EditText)linearLayout.findViewById(R.id.insertMeaning)).getText().toString();
                        String sample = ((EditText)linearLayout.findViewById(R.id.insertSample)).getText().toString();


                        Toast.makeText(MainActivity.this, word + meaning + sample, Toast.LENGTH_SHORT).show();
//                        insertUserSql(String.valueOf(words.size() + 1) ,word, meaning, sample);

//                        words = getAll();
                        //显示出来
//                        setWordsListView(items);
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
    private void update(String word, String meaning, String sample){
        String sql = "update words set word=?,meaning=?,sample=?";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{word, meaning, sample});
    }
    private void updateDialog(String word, String meaning, String sample){
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
//        String word = ((EditText) linearLayout.findViewById(R.id.insertWord)).getText().toString();
//        String meaning = ((EditText) linearLayout.findViewById(R.id.insertMeaning)).getText().toString();
    }
}