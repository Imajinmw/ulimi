package com.rodz.ulimi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SetUp extends AppCompatActivity {
    EditText name,dob,phone;
    SQLiteDatabase db;
    User user;
    AutoCompleteTextView district;
    RadioGroup genderGroup;
    MaterialButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        DbHelper helper = new DbHelper(this);
        db = helper.getWritableDatabase();
        user= new User(db);

        name = (EditText) findViewById(R.id.name);
        dob = (EditText) findViewById(R.id.year);
        phone = (EditText) findViewById(R.id.phone);
        district = findViewById(R.id.district);

        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        btn = findViewById(R.id.btn);

        //the distring
        try {
            JSONArray rows = new JSONArray(myFunction(this).toString());
            ArrayList<String> districts = new ArrayList<>();
            for (int i = 0; i < rows.length(); i++){
                JSONObject dis = rows.getJSONObject(i);
                districts.add(dis.getString("name"));
            }
            ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, districts);
            aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            district.setAdapter(aa1);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("name-register", name.getText().toString());
                params.put("dob", name.getText().toString());
                params.put("district", district.getText().toString());
                params.put("phone", phone.getText().toString());
                String genderName = "Male";
                int selected = genderGroup.getCheckedRadioButtonId();
                if (selected == R.id.female){
                    genderName = "Female";
                }
                params.put("gender", genderName);

                new Http(new HttpParams(SetUp.this, Values.url, params){
                    @Override
                    public void onResponse(String text) {
                        try{
                            JSONObject obj = new JSONObject(text);
                            if (obj.getBoolean("status")){
                                //save the user
                                db.execSQL("INSERT INTO user (id, webid, name, dob, district,gender, phone) VALUES (NULL, ?, ?, ?, ?, ?, ?)",
                                        new Object[]{obj.getString("id"), obj.getString("name"), obj.getString("dob"),obj.getString("district"), obj.getString("gender"), obj.getString("phone")});
                                startActivity(new Intent(SetUp.this, MainActivity.class));
                                finish();
                            }
                        }
                        catch (Exception ex){
                            System.out.println(text);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Already registered?");

        LinearLayout cont = new LinearLayout(this);
        cont.setOrientation(LinearLayout.HORIZONTAL);
        cont.setPadding(dpToPx(15),dpToPx(15),dpToPx(15),dpToPx(15));

        LinearLayout left = new LinearLayout(this), right = new LinearLayout(this);
        left.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
        right.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
        cont.addView(left);
        cont.addView(right);

        MaterialButton signin = new MaterialButton(this), close = new MaterialButton(this);
        signin.setText("Sign In");
        close.setText("Close");

        left.addView(signin);
        right.addView(close);
        alert.setView(cont);
        alert.show();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetUp.this, GeneralPurpose.class);
                intent.putExtra("action", "SignIn");
                startActivity(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DialogInterface)alert).cancel();
            }
        });
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private StringBuilder myFunction(Context context){

        final Resources resources =  context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.districts);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder strBuild = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                strBuild.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strBuild;

    }
}