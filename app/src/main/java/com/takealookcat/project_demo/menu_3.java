package com.takealookcat.project_demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import android.content.SharedPreferences;
public class menu_3 extends Fragment {

    //프레그먼트
    tab_3 tab_3;
    tab_4 tab_4;

    ImageButton btn_cat;
    ImageButton btn_feed;
    ImageButton btn_write;
    ImageButton btn_dona;

    ImageView selectboardIcon; // 게시판 메뉴 선택 아이콘
    TextView selectedBoardTitle; // 선택된 게시판 종류

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_3,container,false);

        selectboardIcon = (ImageView)rootview.findViewById(R.id.selectboardIcon);
        selectedBoardTitle = (TextView)rootview.findViewById(R.id.selectedBoardTitle);

        selectboardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getActivity().getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getActivity().getMenuInflater().inflate(R.menu.board_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        FragmentManager fragManager;
                        FragmentTransaction fragTransaction;
                        TextView toolbarTitle;
                        ActionBar actionBar;
                        switch (item.getItemId()){

                            case R.id.cat:
                                // 프래그먼트
                                tab_1 tab_1 = new tab_1();

                                fragManager = getFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container, tab_1);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();

                                // 선택된 게시판 타이틀 변경
                                selectedBoardTitle.setText("고양이");
                                selectedBoardTitle.setTextColor(getResources().getColor(R.color.mainTextBlack));

                                break;
                            case R.id.feedplace:
                                tab_2 tab_2 = new tab_2();

                                fragManager = getFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container, tab_2);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();

                                // 선택된 게시판 타이틀 변경
                                selectedBoardTitle.setText("급식소");
                                selectedBoardTitle.setTextColor(getResources().getColor(R.color.mainTextBlack));
                                break;
                            case R.id.dona:

                                // 선택된 게시판 타이틀 변경
                                selectedBoardTitle.setText("기부");
                                selectedBoardTitle.setTextColor(getResources().getColor(R.color.mainTextBlack));
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

                popup.show();//Popup Menu 보이기
            }
        });
       /*
        //프래그먼트
        tab_1 = new tab_1();
        tab_2 = new tab_2();
        tab_3 = new tab_3();
        tab_4 = new tab_4();

        btn_cat = (ImageButton)rootview.findViewById(R.id.btn_cat);
        btn_feed = (ImageButton) rootview.findViewById(R.id.btn_feed);
        btn_write = (ImageButton)rootview.findViewById(R.id.btn_write);
        btn_dona = (ImageButton) rootview.findViewById(R.id.btn_dona);

        getFragmentManager().beginTransaction().replace(R.id.container, tab_1).commit();

        btn_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "고양이 등록", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container, tab_1).commit();
            }
        });
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "급식소 등록", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container, tab_2).commit();
            }
        });
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "게시판 등록", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container, tab_3).commit();
            }
        });
        btn_dona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "기부글 등록", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container, tab_4).commit();
            }
        });
*/
        return rootview;
    }

}
