package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menu_1 extends Fragment {

    menu_1_1 menu_1_1;
    menu_1_2 menu_1_2;
    menu_1_3 menu_1_3;

    Button btn_cat;
    Button btn_feed;
    Button btn_write;


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

        btn_cat = (Button)rootview.findViewById(R.id.main_cat);
        btn_feed = (Button) rootview.findViewById(R.id.main_feed);
        btn_write = (Button)rootview.findViewById(R.id.main_board);

        getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_1).commit();


        btn_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "menu1", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_1).commit();
            }
        });

        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "menu2", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_2).commit();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "menu3", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.container1, menu_1_3).commit();
            }
        });

        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }

}
