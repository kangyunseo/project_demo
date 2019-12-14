package com.takealookcat.project_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class menu_5 extends Fragment {

    static final String[] CategoryList = {"프로필 수정", "내가 쓴 글"} ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_5,container,false);

        // 어댑터
        ArrayAdapter Adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_commu, CategoryList) ;
        ListView listview = (ListView)rootview.findViewById(R.id.listview) ;
        listview.setAdapter(Adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                //String strText = (String) parent.getItemAtPosition(position) ;
                // TODO : use strText

                FragmentManager fragManager;
                FragmentTransaction fragTransaction;
                TextView toolbarTitle;
                ActionBar actionBar;

                switch (position){
                    case 0:
                        // 프로필 수정
                        // 프래그먼트
                        FragProfile fragProfile = new FragProfile(); // 프래그먼트 생성

                        fragManager = getFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container2, fragProfile);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();

                        // 툴바 타이틀 변경
                        toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                        toolbarTitle.setText("프로필 수정");

                        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                        actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // 버튼 모양 변경(뒤로)

                        break;
                    case 1:
                        // 내가 쓴 글
                        // 프래그먼트
                        FragMypost fragMypost = new FragMypost();

                        fragManager = getFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container2, fragMypost);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();

                        // 툴바 타이틀 변경
                        toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                        toolbarTitle.setText("내가 쓴 글");

                        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
                        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
                        actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // 버튼 모양 변경(뒤로)

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

            }
        }) ;


        return rootview;
    }
}
