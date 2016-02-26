/*
 * Copyright (c) 2015. WeNoun™. TANSAN, Since 2014.
 * Code By Jey.
 */

package com.wenoun.library.notice.data;

import java.io.Serializable;

/**
 * Created by SnakeJey on 2015-01-27.
 */
public class Noti extends Object implements Serializable{
    private int no=-1;
    private String title="";
    private String msg="";
    private String date="89.01.02"; // date형식 : yy.mm.dd
    public Noti(int no, String title, String msg, String date){
        this.no=no;
        this.title=title;
        this.msg=msg;
        this.date=date;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
