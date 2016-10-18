package com.example.kasperspychala.bookocr;

/**
 * Created by Kasper on 21.05.2016.
 */
public class RowBook {
    public int icon;
    public String title;
    public String author;

    public RowBook(){

    }

    public RowBook(int icon, String title, String author) {

        this.icon = icon;
        this.title = title;
        this.author = author;
    }

    public String getTitle(){
        return title;
    }
}

