package com.example.kasperspychala.bookocr;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHandler db;
    List<Books> books;
    ListAdapter adapter;
    RowBook RowBook_data[];
    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    Uri picUri;
    Bitmap thePic;
    File path;
    String recText;
    String currentDateandTime;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redraw();
        db = new DatabaseHandler(this);
        adapter = new ListAdapter(this, R.layout.row_book, RowBook_data);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                db.deleteBooks(books.get(pos));
                redraw();
                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // use standard intent to capture an image
                    Intent captureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    // we will handle the returned data in onActivityResult
                    path = new File(Environment.getExternalStorageDirectory(), "ocr_data");
                    if (!path.exists()) {
                        try {
                            path.mkdir();
                        } catch (Exception ex) {
                            Log.e("io", ex.getMessage());
                        }

                    }
                    File file = new File(Environment.getExternalStorageDirectory() + "/ocr_data/temp.jpg");
                    Uri outputFileUri = Uri.fromFile(file);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException anfe) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't crop, sorry m8", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                //picUri = data.getData();
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {
                // get the returned data
                filePath = path
                        + "/temp.jpg";

                thePic = BitmapFactory.decodeFile(filePath);
                recText = scanImage();
                recBook(recText);
                File file = new File(Environment.getExternalStorageDirectory()+"/ocr_data/temp.jpg");
                file.delete();
                redraw();

            }
        }
    }

    /**
     * this function does the crop operation.
     */
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            File file = new File(Environment.getExternalStorageDirectory()+"/ocr_data/temp.jpg");
            picUri = Uri.fromFile(file);

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // save file on sdcard
            path = new File(Environment.getExternalStorageDirectory(), "ocr_data");
            if(!path.exists()){
                try {
                    path.mkdir();
                } catch (Exception ex) {
                    Log.e("io", ex.getMessage());
                }

            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            currentDateandTime = sdf.format(new Date());
            File f = new File(path,
                    "/temp.jpg");

            picUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);

            // start the activity - handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String scanImage(){
        TessBaseAPI baseApi = new TessBaseAPI();
        String dataPath = Environment.getExternalStorageDirectory() + "/tesseract/";
        baseApi.init(dataPath, "eng");
        baseApi.setImage(thePic);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();

        return recognizedText;
    }

    public void redraw() {
        db = new DatabaseHandler(this);
        books = db.getAllBooks();

        //Refresh books
        RowBook_data = new RowBook[books.size()];
        for (int i = 0; i < books.size(); i++) {
            RowBook_data[i] = new RowBook(R.drawable.add, books.get(i).getTitle(), books.get(i).getAuthor());
        }

        adapter = new ListAdapter(this, R.layout.row_book, RowBook_data);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void recBook(String recText){
        int start=0;
        int end =0;
        recText = recText+"\n";
        while(end+1<recText.length()) {
            end = recText.substring(start).indexOf("\n");
            end=start+end;
            if(recText.substring(start,end)!="\n"){
                db.addBook(new Books(recText.substring(start,end), recText.substring(start,end), null, null));
            }
            start=end+1;
        }
    }
    public static Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
}
