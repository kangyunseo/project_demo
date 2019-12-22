package com.takealookcat.project_demo;

import android.content.SharedPreferences;
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

public class FragMypost extends Fragment {
    ListView listView;  // 리스트 뷰
    AllItemSimpleAdapter adapter; // 커뮤니티페이지 홈, 게시판 리스트 어댑터
    List<AllItem> all_list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference allRef;

    SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_mypost, container,false);

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

        return rootview;
    }
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //자신의 email가져오기
            String email;
            pref = getActivity().getSharedPreferences("sFile", getActivity().MODE_PRIVATE);
            email = pref.getString("email", "");
            pref.edit().putString("profile_image", "user/"+email+".jpeg");

            // 위에 선언한 저장소인 datas를 초기화하고
            all_list.clear();

            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                AllItem item = snapshot.getValue(AllItem.class); // 컨버팅되서 Bbs로........
                item.key = key;
                if(item.email.equals(email))
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
