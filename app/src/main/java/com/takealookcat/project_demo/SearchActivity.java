package com.takealookcat.project_demo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<String> list;
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        list = new ArrayList<String>();
        adapter = new SearchAdapter(list, this);
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.

                String text = editSearch.getText().toString();
                search(text);

            }
        });

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {
        list.clear();

        if(charText.length() != 0 )
        {
            final String strData = charText;
            TMapData tmapdata = new TMapData();
            tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback(){
                @Override
                public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {

                    for(int i=0; i< poiItem.size();i++)
                    {
                        TMapPOIItem item = poiItem.get(i);

                        //Log.d("주소로 찾기", "POI Name: " + item.getPOIName().toString() +"," +
                        //      "Address: " + item.getPOIAddress().replace("null","") +","+
                        //    "Point: " + item.getPOIPoint().toString()
                        //);
                        String data = "POI Name: " + item.getPOIName().toString() + '\n';
                        data += "Address: " + item.getPOIAddress().replace("null","");
                        data += "Point: " + item.getPOIPoint().toString();
                        list.add(item.getPOIName().toString());


                        if ( i == 4)
                            break;

                    }
                }
            });

        }
        adapter.notifyDataSetChanged();
        Toast.makeText(SearchActivity.this,"도착지가 설정되었습니다. : " ,Toast.LENGTH_SHORT).show();
    }

}

