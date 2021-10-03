package com.example.myandroidexperiment2.Model;


import java.io.Serializable;

public class Word implements Serializable {
    public String id;
    public String word;
    public String meaning;
    public String sample;

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
}
