package com.takealookcat.project_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class signing2 extends AppCompatActivity {

    private ImageView profile_image_view;
    private Bitmap profile_image_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing2);

        Button next = (Button)findViewById(R.id.login_signup);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signing2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*
        new Thread() {
            public void run(){
                SharedPreferences pref = getSharedPreferences("sFile",MODE_PRIVATE);
                String image_link = pref.getString("profile_image_url","http://kinimage.naver.net/20160619_255/1466325466712W1TRH_JPEG/1466325466562.jpeg");
                profile_image_file = GetImageFromURL(image_link);
            }
        }.start();
        profile_image_view = (ImageView)findViewById(R.id.profile_image_link);
        profile_image_view.setImageBitmap(profile_image_file);
        */
    }


    private Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap = null;

        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return imgBitmap;
    }
}
