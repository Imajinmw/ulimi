package com.rodz.ulimi.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.rodz.ulimi.FileManager;
import com.rodz.ulimi.R;
import com.rodz.ulimi.Values;

public class SelectedItem {
    Context _this;
    public LinearLayout view, view1;
    FileManager fileManager;

    @SuppressLint("ResourceType")
    public SelectedItem(Context ctx, Cursor c){
        _this = ctx;
        fileManager = new FileManager(_this);

        view = new LinearLayout(_this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        view1 = new LinearLayout(ctx);
        view1.setBackgroundResource(R.drawable.thin_light);
        view1.setOrientation(LinearLayout.HORIZONTAL);
        view1.setPadding(dpToPx(9), dpToPx(9), dpToPx(9), dpToPx(9));
        view1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        view.addView(view1);

        LinearLayout linearLayout2 = new LinearLayout(ctx);
        linearLayout2.setLayoutParams(new LayoutParams(dpToPx(0), LayoutParams.WRAP_CONTENT, .25f));
        view1.addView(linearLayout2);

        ImageView imageView1 = new ImageView(ctx);
        imageView1.setImageResource(R.drawable.hop);
        imageView1.setLayoutParams(new LayoutParams(dpToPx(80), dpToPx(80)));
        Bitmap bitmap = fileManager.getBitmap(c.getString(c.getColumnIndex("picture")));
        String picture = c.getString(c.getColumnIndex("picture"));
        if (bitmap == null) {
            fileManager.downloadImage(Values.uploadsDir+picture, c.getString(c.getColumnIndex("picture")), imageView1);
        }
        else{
            try{
                if (bitmap.getWidth() > 300){
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                }
                imageView1.setImageBitmap(bitmap);
            }
            catch(Exception ff){
                Toast.makeText(_this, ff.toString(), Toast.LENGTH_LONG).show();
            }
        }
        linearLayout2.addView(imageView1);

        LinearLayout linearLayout3 = new LinearLayout(ctx);
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        linearLayout3.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        linearLayout3.setLayoutParams(new LayoutParams(dpToPx(0), LayoutParams.WRAP_CONTENT, .75f));
        view1.addView(linearLayout3);

        TextView textView1 = new TextView(ctx);
        textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
        textView1.setTextColor(Color.parseColor(_this.getString(R.color.black)));
        textView1.setText(c.getString(c.getColumnIndex("name")));
        textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout3.addView(textView1);

        TextView textView2 = new TextView(ctx);
        textView2.setText(c.getString(c.getColumnIndex("description")));
        textView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout3.addView(textView2);
    }

    public void setOnClickListener(View.OnClickListener listener){
        view1.setOnClickListener(listener);
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = _this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
