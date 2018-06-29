package com.androidpureland.autotest.Entity;

/**
 * Created by Administrator on 2018/6/22.
 */

public class DialogAskEntity {
    private String txt;
    private boolean isChecked;
    private int Action;

    public DialogAskEntity(String txt, boolean isChecked, int action) {
        this.txt = txt;
        this.isChecked = isChecked;
        Action = action;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getAction() {
        return Action;
    }

    public void setAction(int action) {
        Action = action;
    }


}
