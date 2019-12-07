package com.takealookcat.project_demo;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class menu_2 extends Fragment {

    FragCateDona fragcatedona; //donation 게시판 fragment
    FragCateBoard fragcateboard;
    static final String[] CategoryList = {"자유게시판", "기부", "게시판1"} ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_2,container,false);


        fragcatedona = new FragCateDona(); //donation 게시판 fragment
        fragcateboard = new FragCateBoard();    //자유게시판 fragment

        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, CategoryList) ;
        ListView listview = (ListView)rootview.findViewById(R.id.listview) ;
        listview.setAdapter(Adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                //String strText = (String) parent.getItemAtPosition(position) ;
                // TODO : use strText

                switch (position){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container, fragcateboard).commit();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container, fragcatedona).commit();
                        break;
                    case 2:
                        Toast.makeText(getActivity().getApplicationContext(),"아직이야",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

            }
        }) ;


        return rootview;
    }


}
