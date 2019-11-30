package com.takealookcat.project_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    menu_1 fragment1;
    menu_2 fragment2;
    menu_3 fragment3;
    menu_4 fragment4;
    menu_5 fragment5;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("sFile",MODE_PRIVATE);
        String issigned = pref.getString("issigned","false");

        //if (issigned.equals("false")) {
        //    Intent intent = new Intent(MainActivity.this, signing2.class);
        //    startActivity(intent);
        //    finish();
        //}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!hasPermissions(this, PERMISSIONS)){ //exif 권한
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        if ( Build.VERSION.SDK_INT >= 23 && // 위치 권한 rr
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]
                    { android.Manifest.permission.ACCESS_FINE_LOCATION },0 );
        }

        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("email","");
        //TextView textView1 = (TextView) findViewById(R.id.text1) ;
        //textView1.setText(text);

        //String getString = getIntent().getStringExtra("token");
        //TextView textView1 = (TextView) findViewById(R.id.text1) ;
        //textView1.setText(getString);

        fragment1 = new menu_1();
        fragment2 = new menu_2();
        fragment3 = new menu_3();
        fragment4 = new menu_4();
        fragment5 = new menu_5();

        getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment1).commit();

        ImageButton button1 = (ImageButton)findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment1).commit();
            }
        });

        ImageButton button2 = (ImageButton)findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment2).commit();
            }
        });

        ImageButton button3 = (ImageButton)findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment3).commit();
            }
        });

        ImageButton button4 = (ImageButton)findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment4).commit();
            }
        });

        ImageButton button5 = (ImageButton)findViewById(R.id.btn5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment5).commit();
            }
        });

    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
