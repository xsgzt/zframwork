package com.ztyb.framework.widget.sidebarsearchview;

public class SortModel {
    private boolean isFirst;
    private String name;
    private String letters;//显示拼音的首字母




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean getFirst() {
        return isFirst;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
