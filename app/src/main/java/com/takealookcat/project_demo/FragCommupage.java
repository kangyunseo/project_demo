package com.takealookcat.project_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragCommupage extends Fragment {
    ListView listView;  // 리스트 뷰
    AllItemSimpleAdapter adapter; // 커뮤니티페이지 홈, 게시판 리스트 어댑터
    List<AllItem> all_list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference allRef;

    ImageView selectboardIcon; // 게시판 메뉴 더보기 아이콘

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_commupage_home, container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        allRef = database.getReference("all");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        allRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
        listView = (ListView)rootview.findViewById(R.id.listview);
        adapter = new AllItemSimpleAdapter(all_list, getContext());
        listView.setAdapter(adapter);


        selectboardIcon = (ImageView)rootview.findViewById(R.id.selectboardIcon);

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
                            /*
                            case R.id.notice:
                                //Toast.makeText(getApplication(),"메뉴1",Toast.LENGTH_SHORT).show();
                                FragCommupageBoard fragCommupageBoard = new FragCommupageBoard();

                                fragManager = getFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container2, fragCommupageBoard);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();
                                break;
                                */
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

                            case R.id.dona:
                                // 프래그먼트
                                menu_1_3 menu_1_3 = new menu_1_3();

                                fragManager = getFragmentManager();
                                fragTransaction = fragManager.beginTransaction();
                                fragTransaction.replace(R.id.container2, menu_1_3);
                                fragTransaction.addToBackStack(null);
                                fragTransaction.commit();

                                // 툴바 타이틀 변경
                                toolbarTitle = ((MainActivity)getActivity()).findViewById(R.id.toolbarTitle);
                                toolbarTitle.setText("기부갤러리");

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

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 위에 선언한 저장소인 datas를 초기화하고
            all_list.clear();

            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                AllItem item = snapshot.getValue(AllItem.class); // 컨버팅되서 Bbs로........
                item.key = key;
                all_list.add(0, item);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
}
