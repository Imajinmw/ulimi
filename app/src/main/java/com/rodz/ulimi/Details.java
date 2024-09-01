package com.rodz.ulimi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class Details extends AppCompatActivity {
    SQLiteDatabase db;
    User user;
    FileManager fileManager;
    LinearLayout mainLl;
    ImageView display;
    TabLayout tab;
    String item = "0";
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DbHelper helper = new DbHelper(this);
        db = helper.getWritableDatabase();
        user = new User(db);
        fileManager = new FileManager(this);
        display = findViewById(R.id.display);

        mainLl = (LinearLayout)findViewById(R.id.mainLl);

        Intent intent = getIntent();
        item = intent.getStringExtra("item");

        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE webid = ?", new String[]{item});
        cursor.moveToFirst();
        getSupportActionBar().setTitle(cursor.getString(cursor.getColumnIndex("name")));


        ///////////////////for display picture
        String picture = cursor.getString(cursor.getColumnIndex("picture"));
        Bitmap bitmap = fileManager.getBitmap(picture);
        if (bitmap == null) {
            fileManager.downloadImage(Values.uploadsDir+picture, picture, display);
        }
        else{
            try{
                if (bitmap.getWidth() > 300){
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                }
                display.setImageBitmap(bitmap);
            }
            catch(Exception ff){
                Toast.makeText(this, ff.toString(), Toast.LENGTH_LONG).show();
            }
        }///////////////////////////////////////


        tab = findViewById(R.id.tabs);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //System.out.println("Hello liwewe");
                switch (tab.getPosition()){
                    case 0:
                        showEnvironment();
                        break;

                    case 1:
                        showActivities();
                        break;

                    case 2:
                        //notifications();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        showEnvironment();
                        break;

                    case 1:
                        showActivities();
                        break;

                    case 2:
                        //notifications();
                        break;
                }
            }
        });
        showEnvironment();
    }

    public void showEnvironment(){
        mainLl.removeAllViews();

        Cursor cursor = db.rawQuery("SELECT * FROM environment WHERE item = ?", new String[]{item});
        while (cursor.moveToNext()){

        }
    }

    public void showActivities(){

    }
}