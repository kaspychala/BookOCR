package com.example.kasperspychala.bookocr;

/**
 * Created by Kasper on 23.05.2016.
 */
public class Books {
    //private variables
    int id;
    String author;
    String title;
    String genre;
    String path;

    // Empty constructor
    public Books(){

    }
    // constructor
    public Books(int id, String author, String title, String genre, String path){
        this.id = id;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.path = path;
    }

    // constructor
    public Books(String author, String title, String genre, String path){
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.path = path;
    }
    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting author
    public String getAuthor(){
        return this.author;
    }

    // setting author
    public void setAuthor(String author){
        this.author = author;
    }

    // getting title
    public String getTitle(){
        return this.title;
    }

    // setting title
    public void setTitle(String title){
        this.title = title;
    }

    // getting genre
    public String getGenre(){
        return this.genre;
    }

    // setting genre
    public void setGenre(String genre){
        this.genre = genre;
    }

    // getting path
    public String getPath(){
        return this.path;
    }

    // setting path
    public void setPath(String path){ this.path = path; }
}

