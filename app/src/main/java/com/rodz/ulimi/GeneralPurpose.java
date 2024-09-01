package com.rodz.ulimi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rodz.ulimi.views.NewCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralPurpose extends AppCompatActivity {
    LinearLayout main;
    SQLiteDatabase db;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_purpose);

        DbHelper helper = new DbHelper(this);
        db = helper.getWritableDatabase();

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        getSupportActionBar().setTitle(action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main = findViewById(R.id.mainLl);

        switch (action){
            case "addCrop":
                addCrop();
                break;

            case "addAnimal":
                addAnimal();
                break;

            case "SignIn":
                signIn();
                break;
        }
    }

    public void signIn(){
        View view = getLayoutInflater().inflate(R.layout.signin, null);
        main.addView(view);
        final String[] stage = {"phone"};

        MaterialButton btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //try to login
                EditText phone = findViewById(R.id.phone);
                EditText code = findViewById(R.id.code);
                TextInputLayout layout = findViewById(R.id.layout);

                Map<String, Object> params = new LinkedHashMap<>();
                params.put("phone_login", phone.getText().toString());
                params.put("code", code.getText().toString());
                params.put("stage", stage[0]);

                new Http(new HttpParams(GeneralPurpose.this, Values.url, params){
                    @Override
                    public void onResponse(String text) {
                        try {
                            JSONObject obj = new JSONObject(text);
                            if (obj.getBoolean("status")){
                                if (stage[0].equals("phone")){
                                    btn.setText("Confirm Code");
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.topMargin = dpToPx(12);
                                    layout.setLayoutParams(layoutParams);
                                    stage[0] = "code";
                                }
                                else{
                                    //save the user
                                    db.execSQL("INSERT INTO user (id, webid, name, dob, district,gender,phone) VALUES (NULL, ?, ?, ?, ?, ?, ?)",
                                            new Object[]{obj.getString("id"), obj.getString("name"), obj.getString("dob"),obj.getString("district"), obj.getString("gender"), obj.getString("phone")});
                                    startActivity(new Intent(GeneralPurpose.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                        catch (Exception exception){
                            System.out.println(text);
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void addCrop(){
        //
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("getCrops", "true");

        new Http(new HttpParams(this, Values.url, params){
            @Override
            public void onResponse(String text) {
                try{
                    JSONArray rows = new JSONArray(text);

                    main.removeAllViews();

                    for (int i = 0; i < rows.length(); i++){
                        JSONObject obj = rows.getJSONObject(i);
                        String id = obj.getString("id");

                        NewCrop newCrop = new NewCrop(GeneralPurpose.this, obj);
                        newCrop.setAddClickLister(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.execSQL("CREATE TABLE IF NOT EXISTS selected (id INTEGER PRIMARY KEY AUTOINCREMENT, item VARCHAR)");
                                db.execSQL("INSERT INTO selected (id, item) VALUES (NULL, ?)",new Object[]{id});
                                finish();
                            }
                        });
                        main.addView(newCrop.view);
                    }
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }

    public void addAnimal(){
        //
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("getAnimals", "true");

        new Http(new HttpParams(this, Values.url, params){
            @Override
            public void onResponse(String text) {
                try{
                    JSONArray rows = new JSONArray(text);

                    main.removeAllViews();

                    for (int i = 0; i < rows.length(); i++){
                        JSONObject obj = rows.getJSONObject(i);
                        String id = obj.getString("id");

                        NewCrop newCrop = new NewCrop(GeneralPurpose.this, obj);
                        newCrop.setAddClickLister(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.execSQL("CREATE TABLE IF NOT EXISTS selected (id INTEGER PRIMARY KEY AUTOINCREMENT, item VARCHAR)");
                                db.execSQL("INSERT INTO selected (id, item) VALUES (NULL, ?)",new Object[]{id});
                                finish();
                            }
                        });
                        main.addView(newCrop.view);
                    }
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }
}