package com.crud.myEnglishApp.utils;

import org.springframework.beans.factory.annotation.Autowired;

public class AddWordState {




    private String word;
    private String translation;
    private String example;
    private int step; // 0 = слово, 1 = перевод, 2 = пример

    // Геттеры и сеттеры
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public int getStep() { return step; }
    public void setStep(int step) { this.step = step; }
}
