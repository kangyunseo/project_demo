package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class menu_1 extends Fragment {

    menu_1_1 menu_1_1;
    menu_1_2 menu_1_2;
    menu_1_3 menu_1_3;

    ImageButton btn_cat;
    ImageButton btn_feed;
    ImageButton btn_write;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_1,container,false);

        menu_1_1 = new menu_1_1();
        menu_1_2 = new menu_1_2();
        menu_1_3 = new menu_1_3();

        btn_cat = (ImageButton)rootview.findViewById(R.id.main_cat);
        btn_feed = (ImageButton) rootview.findViewById(R.id.main_feed);
        btn_write = (ImageButton)rootview.findViewById(R.id.main_board);

        getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_1).commit();


        btn_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "고양이 게시판", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_1).commit();
            }
        });

        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "급식소 게시판", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_2).commit();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "커뮤니티 게시판", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_3).commit();
            }
        });

        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }

}

