package com.example.kasperspychala.bookocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BooksManager";

    // Books table name
    private static final String TABLE_BOOKS = "books";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_TITLE = "title";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_PATH = "path";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Books_TABLE = "CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AUTHOR + " TEXT,"
                + KEY_TITLE + " TEXT," + KEY_GENRE + " TEXT," + KEY_PATH +
                " TEXT" + ")";
        db.execSQL(CREATE_Books_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);

        // Create tables again
        onCreate(db);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new books
    void addBook(Books books) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, books.getAuthor()); // Books Author
        values.put(KEY_TITLE, books.getTitle()); // Books Title
        values.put(KEY_GENRE, books.getGenre());// Books Genre
        values.put(KEY_PATH, books.getPath());// Books Photo Path

        // Inserting Row
        db.insert(TABLE_BOOKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single book
    Books getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKS, new String[] { KEY_ID,
                        KEY_AUTHOR, KEY_TITLE, KEY_GENRE, KEY_PATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Books books = new Books(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return book
        return books;
    }

    // Getting All Books
    public List<Books> getAllBooks() {
        List<Books> booksList = new ArrayList<Books>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Books books = new Books();
                books.setID(Integer.parseInt(cursor.getString(0)));
                books.setAuthor(cursor.getString(1));
                books.setTitle(cursor.getString(2));
                books.setGenre(cursor.getString(3));
                books.setPath(cursor.getString(4));
                // Adding books to list
                booksList.add(books);
            } while (cursor.moveToNext());
        }

        // return books list
        return booksList;
    }

    // Updating single books
    public int updateBooks(Books books) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, books.getAuthor());
        values.put(KEY_TITLE, books.getTitle());
        values.put(KEY_GENRE, books.getGenre());
        values.put(KEY_PATH, books.getPath());
        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(books.getID()) });
    }

    // Deleting single book
    public void deleteBooks(Books books) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, KEY_ID + " = ?",
                new String[] { String.valueOf(books.getID()) });
        db.close();
    }

}