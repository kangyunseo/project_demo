package com.takealookcat.project_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommupageListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CommupageItem> datas;
    LayoutInflater inflater = null;

    // ListViewAdapter의 생성자
    public CommupageListAdapter(ArrayList<CommupageItem> data) {
        this.datas = data;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return datas.size();
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_commupage, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView commupageTitle = (TextView) convertView.findViewById(R.id.textView3); // 제목

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CommupageItem item = datas.get(position);

        // 아이템 내, 각 위젯에 데이터 반영
        commupageTitle.setText(item.title);

        return convertView;
    }
}
