package com.example.myandroidexperiment2.Model;


import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Word implements Serializable {
    public String id;
    public String word;
    public String meaning;
    public String sample;

    public static final String AUTHORITY = "xyz.longdy.blog.wordprovider";//URI授权者

    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    public static final String MINE_ITEM = "vnd.bistu.cs.se.word";

    public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
    public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;

    public static final String PATH_SINGLE = "word/*";//单条数据的路径
    public static final String PATH_MULTIPLE = "word";//多条数据的路径


    //Content Uri
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;

    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    public Word(){}
    public Word(String id, String word, String meaning, String sample){
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.sample = sample;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }
    public String toString(){
        return "id:" + id + "\nword:" + word + "\nmeaning: " + meaning + "\nsample: " + sample;
    }
//    public static abstract class WordTest implements BaseColumns {
//        public static final String TABLE_NAME="words";
//    }
}
