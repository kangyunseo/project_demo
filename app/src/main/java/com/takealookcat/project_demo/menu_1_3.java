package com.takealookcat.project_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class menu_1_3 extends Fragment {

    ListView listView;
    ListViewAdapter adapter;
    List<ListViewItem> datas = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference donaRef;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_1_3, container, false);
// 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        donaRef = database.getReference("cat");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        donaRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
        listView = (ListView)rootview.findViewById(R.id.listview3);
        adapter = new ListViewAdapter(datas, getContext());
        listView.setAdapter(adapter);

        /*
        ListView listview ;
        final ListViewAdapter adapter;
        final ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

        adapter = new ListViewAdapter(listViewItemList,getContext()) ;

        listview = (ListView)rootview.findViewById(R.id.listview1);
        listview.setAdapter(adapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getIcon() ;

                // TODO : use item data.
            }
        }) ;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot catData : dataSnapshot.getChildren()) {

                    // child 내에 있는 데이터만큼 반복합니다.
                    //String msg2 = catData.getValue().toString();
                    ListViewItem cat = catData.getValue(ListViewItem.class);
                    listViewItemList.add(cat);
                   // adapter.addItem(cat.getDesc(),cat.getTitle()) ;
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        return rootview;
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 위에 선언한 저장소인 datas를 초기화하고
            datas.clear();
            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                ListViewItem bbs = snapshot.getValue(ListViewItem.class); // 컨버팅되서 Bbs로........
                bbs.key = key;
                datas.add(0,bbs);

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

