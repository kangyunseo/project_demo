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

public class FragCommupage extends Fragment {

    ListView listView;  // 리스트 뷰
    CommupageListAdapter adapter; // 커뮤니티페이지 홈, 게시판 리스트 어댑터
    List<CommupageItem> commupage_list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference catRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_commupage_home,container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        catRef = database.getReference("cat");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
  //      catRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
       // listView = (ListView)rootview.findViewById(R.id.listView);
//        adapter = new CommupageListAdapter(commupage_list, getContext());
  //      listView.setAdapter(adapter);


        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 위에 선언한 저장소인 datas를 초기화하고
            commupage_list.clear();

            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                CommupageItem item = snapshot.getValue(CommupageItem.class); // 컨버팅되서 Bbs로........
                item.key = key;
                commupage_list.add(0, item);

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
