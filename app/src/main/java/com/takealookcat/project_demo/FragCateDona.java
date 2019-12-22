package com.takealookcat.project_demo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragCateDona extends Fragment {
    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database;
    DatabaseReference donaRef;

    ListView listView;
    DonationListAdapter adapter;
    List<DonationItem> dona_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_frag_cate_dona,container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        donaRef = database.getReference("donation");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        donaRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
        listView = (ListView)rootview.findViewById(R.id.listview);
        adapter = new DonationListAdapter(dona_list, getActivity());
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO :
                String content = dona_list.get(position).getContent();
                String title = dona_list.get(position).getTitle();
                String camount = dona_list.get(position).getCurAmount();
                String tamount = dona_list.get(position).getTargetAmount();
                String sday = dona_list.get(position).getStartDate();
                String dday = dona_list.get(position).getDueDate();
                String file = dona_list.get(position).getFile();
                mListener.onFragmentInteraction_dona(content, title, camount, tamount, sday, dday, file);

            }
        }) ;
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }

    // 5. 파이어베이스가 호출해주는 이벤트 리스너 콜백
    // ValueEventListener : 경로의 전체 내용에 대한 변경을 읽고 수신 대기합니다.
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 위에 선언한 저장소인 list를 초기화하고
            dona_list.clear();
            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                DonationItem item = snapshot.getValue(DonationItem.class);
                item.key = key;
                dona_list.add(0,item);
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
            mListener = (OnFragmentInteractionListener) context;
        }catch(ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction_dona(String title, String content, String curAmount, String targetAmount, String startDate,
                                        String dueDate, String file);
        void onFragmentInteraction(String title, String content, String file, String email, String type);

        void onFragmentInteraction_home(String title, String content, String file, String email, String type);

        void onFragmentInteraction_homedona(String title, String content, String curAmount, String targetAmount, String startDate,
                                        String dueDate, String file);
    }
}
