package com.rodz.ulimi.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.rodz.ulimi.R;
import com.rodz.ulimi.Values;

import org.json.JSONObject;

import java.net.URL;

public class NewCrop {
    Context _this;
    public LinearLayout view;
    MaterialButton materialButton1,materialButton2;
    public NewCrop(Context ctx, JSONObject crop){
        try {
            _this = ctx;
            view = new LinearLayout(ctx);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            view.setOrientation(LinearLayout.VERTICAL);
            view.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

            LinearLayout linearLayout2 = new LinearLayout(ctx);
            linearLayout2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout2.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.setBackgroundResource(R.drawable.thin_light);
            view.addView(linearLayout2);

            LinearLayout imgContainer = new LinearLayout(_this);
            imgContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(160)));
            imgContainer.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.addView(imgContainer);

            ImageView imageView1 = new ImageView(ctx);
            imageView1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            imgContainer.addView(imageView1);

            String filename = crop.getString("picture");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL newurl = new URL(Values.uploadsDir + filename);
                        Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        ((Activity) _this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels - dpToPx(24);
                                double height = bitmap.getHeight();
                                double width = bitmap.getWidth();
                                double newHeight = screenWidth/width * height;
                                imageView1.setImageBitmap(bitmap);
                                imageView1.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int)newHeight));

                                //imageView1.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            LinearLayout linearLayout3 = new LinearLayout(ctx);
            linearLayout3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout3.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            //linearLayout3.setBackgroundColor(Color.WHITE);
            linearLayout3.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.addView(linearLayout3);

            TextView textView1 = new TextView(ctx);
            textView1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
            textView1.setTextSize(17.0f);
            textView1.setText(crop.getString("name"));
            linearLayout3.addView(textView1);

            TextView textView2 = new TextView(ctx);
            textView2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView2.setText(crop.getString("description"));
            linearLayout3.addView(textView2);

            LinearLayout linearLayout4 = new LinearLayout(ctx);
            linearLayout4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout3.addView(linearLayout4);

            materialButton1 = new MaterialButton(ctx);
            materialButton1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            materialButton1.setAllCaps(false);
            materialButton1.setText("Add");
            linearLayout4.addView(materialButton1);

            materialButton2 = new MaterialButton(ctx);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = dpToPx(12);
            materialButton2.setLayoutParams(layoutParams);
            materialButton2.setAllCaps(false);
            materialButton2.setText("View");
            linearLayout4.addView(materialButton2);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setAddClickLister(View.OnClickListener listener){
        materialButton1.setOnClickListener(listener);
    }

    public void setViewClickLister(View.OnClickListener listener){
        materialButton2.setOnClickListener(listener);
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = _this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
