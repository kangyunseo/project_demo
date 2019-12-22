package com.takealookcat.project_demo;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.appcompat.widget.Toolbar; // 툴바
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements FragCateDona.OnFragmentInteractionListener {

    Menu toolbarRightBtn;     // 툴바 우측 버튼

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

    public interface onKeyBackPressedListener{
        void onBackKey();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }


    @Override
    public void onBackPressed() {

        //Fragment 로 뒤로가기 callback 보내기위한 로직
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction_dona(String title, String content, String curAmount, String targetAmount, String startDate, String dueDate, String file) {
        FragDonaItem FragItem = new FragDonaItem();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("curAmount", curAmount);
        bundle.putString("targetAmount", targetAmount);
        bundle.putString("startDate", startDate);
        bundle.putString("dueDate", dueDate);
        bundle.putString("file", file);
        FragItem.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.container2, FragItem).addToBackStack(null).commit();
    }
    @Override
    public void onFragmentInteraction(String title, String content, String file, String email, String type){
        FragBoardItem FragItem = new FragBoardItem();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("file", file);
        bundle.putString("email", email);
        bundle.putString("type", type);
        FragItem.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container2, FragItem).addToBackStack(null).commit();
    }


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


        // * 액티비티 첫 실행시 화면
        TextView toolbarTitle;
        final ActionBar actionBar;

        // 프래그먼트 초기화
        fragment1 = new menu_1();
        getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment1).commit();

        // 버튼
        final ImageButton button1 = (ImageButton)findViewById(R.id.btn1);
        final ImageButton button2 = (ImageButton)findViewById(R.id.btn2);
        final ImageButton button3 = (ImageButton)findViewById(R.id.btn3);
        final ImageButton button4 = (ImageButton)findViewById(R.id.btn4);
        final ImageButton button5 = (ImageButton)findViewById(R.id.btn5);
        button1.setImageResource(R.drawable.ic_botnavi_icon_home_on);

        // 툴바
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);                      // 액션바를 툴바로 대체

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기본 타이틀 가리기
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요

        // 툴바 타이틀 변경
        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Take A Look 고양이");

        // * 1. 홈
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프래그먼트 초기화
                FragmentManager fragManager = getSupportFragmentManager();
                for(int i = 0; i < fragManager.getBackStackEntryCount(); i++) {
                    fragManager.popBackStack();
                }

                fragment1 = new menu_1();
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment1).commit();

                // 상단 툴바
                TextView toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("TAKE A LOOK 고양이"); // 타이틀 변경
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false); // 뒤로가기 버튼 지우기
                actionBar.show();

                // 하단 탭 버튼 변경
                button1.setImageResource(R.drawable.ic_botnavi_icon_home_on);
                button2.setImageResource(R.drawable.ic_botnavi_icon_commu_off);
                button3.setImageResource(R.drawable.ic_botnavi_icon_write_off);
                button4.setImageResource(R.drawable.ic_botnavi_icon_map_off);
                button5.setImageResource(R.drawable.ic_botnavi_icon_user_off);
            }
        });

        // * 2. 커뮤니티
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 프래그먼트 초기화
                FragmentManager fragManager = getSupportFragmentManager();
                for(int i = 0; i < fragManager.getBackStackEntryCount(); i++) {
                    fragManager.popBackStack();
                }

                fragment2 = new menu_2();
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment2).commit();

                // 상단 툴바
                TextView toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("커뮤니티"); // 타이틀 변경
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false); // 뒤로가기 버튼 지우기
                actionBar.show();

                // 하단 탭 버튼 변경
                button1.setImageResource(R.drawable.ic_botnavi_icon_home_off);
                button2.setImageResource(R.drawable.ic_botnavi_icon_commu_on);
                button3.setImageResource(R.drawable.ic_botnavi_icon_write_off);
                button4.setImageResource(R.drawable.ic_botnavi_icon_map_off);
                button5.setImageResource(R.drawable.ic_botnavi_icon_user_off);
            }
        });

        // * 3. 글 쓰기
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 프래그먼트 초기화
                FragmentManager fragManager = getSupportFragmentManager();
                for(int i = 0; i < fragManager.getBackStackEntryCount(); i++) {
                    fragManager.popBackStack();
                }

                fragment3 = new menu_3();
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment3).commit();

                // 상단 툴바
                TextView toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("글 쓰기"); // 타이틀 변경
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel); // 버튼 모양 변경(취소)
                actionBar.show();

                // 하단 탭 버튼 변경
                button1.setImageResource(R.drawable.ic_botnavi_icon_home_off);
                button2.setImageResource(R.drawable.ic_botnavi_icon_commu_off);
                button3.setImageResource(R.drawable.ic_botnavi_icon_write_on);
                button4.setImageResource(R.drawable.ic_botnavi_icon_map_off);
                button5.setImageResource(R.drawable.ic_botnavi_icon_user_off);
            }
        });

        // * 4. 지도
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 프래그먼트 초기화
                FragmentManager fragManager = getSupportFragmentManager();
                for(int i = 0; i < fragManager.getBackStackEntryCount(); i++) {
                    fragManager.popBackStack();
                }

                fragment4 = new menu_4();
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment4).commit();

                // 상단 툴바
                TextView toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("지도"); // 타이틀 변경

                ActionBar actionBar = getSupportActionBar();
                //actionBar.hide();
                //actionBar.setDisplayHomeAsUpEnabled(false); // 뒤로가기 버튼 지우기
                /*
                if (toolbarRightBtn != null) {
                    MenuItem item = (MenuItem)toolbarRightBtn.findItem(R.id.toolbarRightBtn);
                    item.setIcon(R.drawable.ic_icon_search);
                    item.setVisible(true);
                }
                */

                // 하단 탭 버튼 변경
                button1.setImageResource(R.drawable.ic_botnavi_icon_home_off);
                button2.setImageResource(R.drawable.ic_botnavi_icon_commu_off);
                button3.setImageResource(R.drawable.ic_botnavi_icon_write_off);
                button4.setImageResource(R.drawable.ic_botnavi_icon_map_on);
                button5.setImageResource(R.drawable.ic_botnavi_icon_user_off);
            }
        });

        // * 5. 사용자 관리
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프래그먼트 초기화
                FragmentManager fragManager = getSupportFragmentManager();
                for(int i = 0; i < fragManager.getBackStackEntryCount(); i++) {
                    fragManager.popBackStack();
                }
                //fragManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragment5 = new menu_5();
                getSupportFragmentManager().beginTransaction().replace(R.id.view, fragment5).commit();
                actionBar.show();


                // 하단 탭 버튼 변경
                button1.setImageResource(R.drawable.ic_botnavi_icon_home_off);
                button2.setImageResource(R.drawable.ic_botnavi_icon_commu_off);
                button3.setImageResource(R.drawable.ic_botnavi_icon_write_off);
                button4.setImageResource(R.drawable.ic_botnavi_icon_map_off);
                button5.setImageResource(R.drawable.ic_botnavi_icon_user_on);
            }
        });

    }

    // 툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.appbar_menu, menu);
        toolbarRightBtn = menu; // 툴바 오른쪽 버튼

        return true;
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
