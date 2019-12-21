package com.takealookcat.project_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;

public class FragCommupageBoard extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_commupage_board, container,false);

        TextView boardTitle = (TextView)rootview.findViewById(R.id.boardTitle);
        ImageView selectboardIcon = (ImageView)rootview.findViewById(R.id.selectboardIcon);

        boardTitle.setText("공지");
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
                                break;
                            case R.id.feedplace:
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


        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }
}
