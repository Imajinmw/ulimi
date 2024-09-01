package com.rodz.ulimi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.rodz.ulimi.views.SelectedItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    User user;
    RelativeLayout mainRl;
    String page = "home";
    TabLayout tab;
    LinearLayout mainLl, selectionContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper helper = new DbHelper(this);
        db = helper.getWritableDatabase();

        user = new User(db);

        if (!user.status){
            startActivity(new Intent(this, SetUp.class));
            finish();
        }
        mainRl = findViewById(R.id.mainRl);
        mainLl = findViewById(R.id.mainLl);

        View bottom;

        bottom = getLayoutInflater().inflate(R.layout.bottom_actions, null);
        //bottom.setBackgroundColor(theme.bg_primary);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = mainRl.getHeight();
        //params.leftMargin = 0;
        //params.topMargin = height - bottom.getHeight();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        mainRl.addView(bottom, params);

        tab = findViewById(R.id.tabs);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //System.out.println("Hello liwewe");
                switch (tab.getPosition()){
                    case 0:
                        myItems();
                        break;

                    case 1:
                        marketPlace();
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
                        myItems();
                        break;

                    case 1:
                        marketPlace();
                        break;

                    case 2:
                        //notifications();
                        break;
                }
            }
        });
        myItems();
        getData();
        printSelection();
    }

    public void printSelection(){
        selectionContainer.removeAllViews();

        Cursor cursor = db.rawQuery("SELECT * FROM selected JOIN items ON selected.item = items.webid ", null);
        while (cursor.moveToNext()){
            SelectedItem item = new SelectedItem(this, cursor);
            @SuppressLint("Range") String item_id = cursor.getString(cursor.getColumnIndex("item"));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    Intent intent = new Intent(MainActivity.this, Details.class);
                    intent.putExtra("item", item_id);
                    startActivity(intent);
                }
            });
            selectionContainer.addView(item.view);
        }
    }

    @Override
    protected void onResume() {
        printSelection();
        super.onResume();
    }

    public void getData(){
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("getData", "true");

        new Http(new HttpParams(this, Values.url, params){
            @Override
            public void onResponse(String text) {
                try {
                    JSONObject obj = new JSONObject(text);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //
                            try{
                                JSONArray plantsanimals = obj.optJSONArray("plantsanimals");
                                //
                                for (int i = 0; i < plantsanimals.length(); i++){
                                    JSONObject obj = plantsanimals.getJSONObject(i);
                                    Cursor c = db.rawQuery("SELECT * FROM items WHERE webid = ?", new String[]{obj.getString("id")});
                                    if (c.getCount() == 0){
                                        db.execSQL("INSERT INTO items (id,webid,name,description,picture,type) VALUES (NULL, ?, ?, ?, ?, ?)",
                                                new Object[]{obj.getString("id"),obj.getString("name"),obj.getString("description"),obj.getString("picture"),obj.getString("type")});
                                    }
                                }

                                JSONArray activities = obj.optJSONArray("activities");
                                //
                                for (int i = 0; i < activities.length(); i++){
                                    JSONObject obj = activities.getJSONObject(i);
                                    Cursor c = db.rawQuery("SELECT * FROM activities WHERE webid = ?", new String[]{obj.getString("id")});
                                    if (c.getCount() == 0){
                                        db.execSQL("INSERT INTO activities (id,webid,item,title, description,user,time, status, picture) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)",
                                                new Object[]{obj.getString("id"),obj.getString("item"),obj.getString("title"), obj.getString("description"),obj.getString("user"),obj.getString("time"), obj.getString("status"), obj.getString("picture")});
                                    }
                                }

                                JSONArray environment = obj.optJSONArray("environment");
                                //
                                for (int i = 0; i < environment.length(); i++){
                                    JSONObject obj = environment.getJSONObject(i);
                                    Cursor c = db.rawQuery("SELECT * FROM environment WHERE webid = ?", new String[]{obj.getString("id")});
                                    if (c.getCount() == 0){
                                        db.execSQL("INSERT INTO environment (id,webid,item,title, description,user,time, status, picture) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)",
                                                new Object[]{obj.getString("id"),obj.getString("item"),obj.getString("title"), obj.getString("description"),obj.getString("user"),obj.getString("time"), obj.getString("status"), obj.getString("picture")});
                                    }
                                }

                                JSONArray users = obj.optJSONArray("users");
                                //
                                for (int i = 0; i < users.length(); i++){
                                    JSONObject obj = users.getJSONObject(i);
                                    Cursor c = db.rawQuery("SELECT * FROM users WHERE webid = ?", new String[]{obj.getString("id")});
                                    if (c.getCount() == 0){
                                        db.execSQL("INSERT INTO users (id,webid,name,email,status,type) VALUES (NULL, ?, ?, ?, ?, ?)",
                                                new Object[]{obj.getString("id"),obj.getString("name"),obj.getString("email"),obj.getString("status"),obj.getString("type")});
                                    }
                                }
                            }
                            catch (Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                }
                catch(Exception ex){
                    System.out.println(text);
                    ex.printStackTrace();
                }
            }
        });
    }

    public void myItems(){
        mainLl.removeAllViews();
        View add_item = getLayoutInflater().inflate(R.layout.add_item, null);
        mainLl.addView(add_item);

        selectionContainer = new LinearLayout(this);
        selectionContainer.setOrientation(LinearLayout.VERTICAL);
        mainLl.addView(selectionContainer);
    }

    public void marketPlace(){

    }

    public void addCrop(View view) {
        Intent intent = new Intent(this, GeneralPurpose.class);
        intent.putExtra("action","addCrop");
        startActivity(intent);
    }

    public void addAnimal(View view) {
        Intent intent = new Intent(this, GeneralPurpose.class);
        intent.putExtra("action","addAnimal");
        startActivity(intent);
    }
}