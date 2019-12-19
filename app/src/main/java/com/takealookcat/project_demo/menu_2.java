package com.takealookcat.project_demo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class menu_2 extends Fragment {


    FragCateDona fragcatedona; //donation 게시판 fragment
    FragCateBoard fragcateboard;
    static final String[] CategoryList = {"서강 고양이 모임"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_2,container,false);

        // * 갤러리 버튼
        LinearLayout galleryCat; // 고양이갤러리 레이아웃
        LinearLayout galleryFeedplace; // 급식소갤러리 레이아웃

        galleryCat = rootview.findViewById(R.id.galleryCat);
        galleryFeedplace = rootview.findViewById(R.id.galleryFeedplace);

        // -> 고양이갤러리 이동
        galleryCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "고양이 게시판", Toast.LENGTH_LONG).show();
                FragmentManager fragManager;
                FragmentTransaction fragTransaction;
                TextView toolbarTitle;
                ActionBar actionBar;

                // 프래그먼트
                menu_1_1 menu_1_1 = new menu_1_1(); // 프래그먼트 생성

                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.replace(R.id.container2, menu_1_1);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();

                // 툴바 타이틀 변경
                toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("고양이갤러리");

                actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // 버튼 모양 변경(뒤로)

            }
        });

        // -> 급식소갤러리 이동
        galleryFeedplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "급식소 게시판", Toast.LENGTH_LONG).show();
                FragmentManager fragManager;
                FragmentTransaction fragTransaction;
                TextView toolbarTitle;
                ActionBar actionBar;

                // 프래그먼트
                menu_1_2 menu_1_2 = new menu_1_2();

                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.replace(R.id.container2, menu_1_2);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();

                // 툴바 타이틀 변경
                toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                toolbarTitle.setText("급식소갤러리");

                actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // 버튼 모양 변경(뒤로)
            }
        });


        // * 내 커뮤니티 그룹


        /*
        ArrayAdapter Adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item_basetext, CategoryList) ;
        ListView listview = (ListView)rootview.findViewById(R.id.listview) ;
        listview.setAdapter(Adapter) ;
        */

        // ListView
        int dataSize = 0;
        ArrayList<BasetextItem> basetexts = new ArrayList<>();

        ListView listView = null;
        BasetextListAdapter adapter;

        for (int i = 0; i < 1; i++) {
            BasetextItem item = new BasetextItem();
            item.title = CategoryList[i];
            dataSize++;
            basetexts.add(item);
        }

        // Adapter 생성
        adapter = new BasetextListAdapter(basetexts);
        listView = (ListView)rootview.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                //String strText = (String) parent.getItemAtPosition(position) ;
                // TODO : use strText

               // fragcatedona = new FragCateDona(); //donation 게시판 fragmen
                //fragcateboard = new FragCateBoard();    //자유게시판 fragment


                FragmentManager fragManager;
                FragmentTransaction fragTransaction;
                TextView toolbarTitle;
                ActionBar actionBar;

                switch (position){
                    case 0:
                        // 프래그먼트
                        FragCommupage fragCommupage = new FragCommupage(); // 커뮤니티페이지 홈 fragment

                        fragManager = getFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container2, fragCommupage);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();

                        // 툴바 타이틀 변경
                        toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                        toolbarTitle.setText("서강 고양이 모임");

                        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                        actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // 버튼 모양 변경(뒤로)

                        break;
                    /*
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container, fragcatedona).commit();
                        break;
                    case 2:
                        Toast.makeText(getActivity().getApplicationContext(),"아직이야",Toast.LENGTH_SHORT).show();
                        break;
                    */
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

            }
        }) ;


        return rootview;
    }


}
