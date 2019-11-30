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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class menu_2 extends Fragment {
    FragCateDona fragcatedona; //donation 게시판 fragment

    static final String[] CategoryList = {"게시판1", "게시판2", "기부"} ;
    Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_2,container,false);
        button = (Button)rootview.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),kakaoPay.class);
                startActivity(intent);
            }
        });

        fragcatedona = new FragCateDona(); //donation 게시판 fragment

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
                        break;
                    case 1:
                        break;
                    case 2:
                        //Toast.makeText(getActivity().getApplicationContext(),"!",Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.container, fragcatedona).commit();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

            }
        }) ;



        return rootview;
    }


}
