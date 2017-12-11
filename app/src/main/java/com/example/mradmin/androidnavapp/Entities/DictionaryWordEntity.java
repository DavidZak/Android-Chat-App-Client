package com.example.mradmin.androidnavapp.Entities;

/**
 * Created by mrAdmin on 17.09.2017.
 */

public class DictionaryWordEntity {

    private String word;
    private String language;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public DictionaryWordEntity(String word, String language) {
        this.word = word;
        this.language = language;
    }

    public DictionaryWordEntity() {
    }
}
