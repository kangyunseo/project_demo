package com.takealookcat.project_demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class menu_1 extends Fragment {
    private FragCateDona.OnFragmentInteractionListener mListener;
    ListView listView;
    AllItemListAdapter adapter;
    List<AllItem> all_list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference allRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.menu_1, container, false);
        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        allRef = database.getReference("all");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        allRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
        listView = (ListView)rootview.findViewById(R.id.listview1);
        adapter = new AllItemListAdapter(all_list, getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO :
                if(all_list.get(position).type.equals("donation")) {
                    String content = all_list.get(position).getContent();
                    String title = all_list.get(position).getTitle();
                    String camount = all_list.get(position).getCurAmount();
                    String tamount = all_list.get(position).getTargetAmount();
                    String sday = all_list.get(position).getStartDate();
                    String dday = all_list.get(position).getDueDate();
                    String file = all_list.get(position).getFile();
                    mListener.onFragmentInteraction_homedona(content, title, camount, tamount, sday, dday, file);
                }
                else{
                    String content = all_list.get(position).getContent();
                    String title = all_list.get(position).getInfo();
                    String email = all_list.get(position).getemail();
                    String file = all_list.get(position).getFile();
                    String type = all_list.get(position).getType();
                    mListener.onFragmentInteraction_home(title, content,  file, email, type);
                }

            }
        }) ;

        return rootview;
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
                all_list.add(0,item);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (FragCateDona.OnFragmentInteractionListener) context;
        }catch(ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }
}

