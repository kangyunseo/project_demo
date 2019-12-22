package com.takealookcat.project_demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class menu_4 extends Fragment implements  View.OnClickListener {
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapData tmapdata = null;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private static String mApiKey = "0b5da8f6-7489-4ee5-9627-381211198f26";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    private ArrayList<MapPoint> m_mapPoint2 = new ArrayList<MapPoint>();

    private String address;
    private Double lat = null;
    private Double lon = null;

    private Button bt_find;
    private Button bt_fac;
    private EditText bt_fac_option;

    private TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
    private TMapPoint tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)
    List<catfeedItem> catfeedItems = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference catRef;
    DatabaseReference feedRef;

    final LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            tMapPointStart.setLatitude(location.getLatitude());
            tMapPointStart.setLongitude(location.getLongitude());
            tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.menu_4,container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        catRef = database.getReference("cat");
        feedRef = database.getReference("feed");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        catRef.addValueEventListener(postListener);
        feedRef.addValueEventListener(postListener2);

        mContext = this.getContext();

        bt_find = (Button)v.findViewById(R.id.bt_findadd);
        bt_fac = (Button)v.findViewById(R.id.bt_findfac);
        bt_fac_option = (EditText)v.findViewById(R.id.editText);

        tmapdata = new TMapData();
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.map_view);
        tmapview = new TMapView(mContext);

        linearLayout.addView(tmapview);
        tmapview.setSKTMapApiKey(mApiKey);

        //addPoint();
        //showMarkerPoint();

        /* 현재 보는 방향*/
        tmapview.setCompassMode(true);

        /* 현위치 아이콘표시 */
        tmapview.setIconVisibility(true);

        /*줌 레벨*/
        tmapview.setZoomLevel(15);

        /* 지도 타입 */
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);

        /*언어 설정 */
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(mContext); // 단말의 위치탐색을 위한 클래스
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간 설정
        tmapgps.setMinDistance(5); // 위치변경 인식 최소거리 설정

        //tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); // 네트워크 기반의 위치탐색
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); // 위성기반의 위치 탐색
        final LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( getActivity(), new String[]
                    { android.Manifest.permission.ACCESS_FINE_LOCATION },0 );
        }
        else{
            Toast.makeText(getActivity(), "LocationManager is ready!", Toast.LENGTH_SHORT).show();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    0,
                    networkLocationListener);
        }
        tmapgps.OpenGps();

        /* 화면 중심을 단말의 현재위치로 이동*/
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        //풍선 클릭시
        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {

                lat = markerItem.latitude;
                lon = markerItem.longitude;
                tMapPointEnd.setLatitude(lat);
                tMapPointEnd.setLongitude(lon);
                find_path();
                //1. 위도, 경도로 주소 검색하기
                tmapdata.convertGpsToAddress(lat,lon,new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String strAddress) {
                        address = strAddress;
                    }
                });

                Toast.makeText(mContext,"도착지가 설정되었습니다. : " ,Toast.LENGTH_SHORT).show();
            }
        });

        bt_find.setOnClickListener(this);
        bt_fac.setOnClickListener(this);
        return v;
    }
    //핀 찍을 data
    /*
    public void addPoint() {
        // 강남 //
        //m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
        //칠보고등학교 //
        m_mapPoint.add(new MapPoint("칠보고등학교",  37.274594, 126.945092));
    }
     */
    //마커 (핀) 찍는함수
    public void showMarkerPoint() {
        for(int i=0; i<m_mapPoint.size();i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            /* 핀 이미지 */
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            item1.setCalloutTitle(m_mapPoint.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            //item1.setAutoCalloutVisible(true);

            /*풍선 안 우측 버튼*/
            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.i_go);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strId = String.format("pmarker%d",mMarkerID++);

            tmapview.addMarkerItem(strId, item1);
            mArrayMarkerID.add(strId);
        }
    }
    //feed 함수
    public void showMarkerPoint2() {
        for(int i=0; i<m_mapPoint2.size();i++) {
            TMapPoint point = new TMapPoint(m_mapPoint2.get(i).getLatitude(), m_mapPoint2.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            /* 핀 이미지 */
            //이미지 다른걸로 (feed)
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.poi_dot);

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint2.get(i).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            item1.setCalloutTitle(m_mapPoint2.get(i).getName());
            item1.setCalloutSubTitle("서울");
            item1.setCanShowCallout(true);
            //item1.setAutoCalloutVisible(true);

            /*풍선 안 우측 버튼*/
            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.i_go);

            item1.setCalloutRightButtonImage(bitmap_i);

            String strId = String.format("pmarker%d",mMarkerID++);

            tmapview.addMarkerItem(strId, item1);
            mArrayMarkerID.add(strId);
        }
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId()) {
            case R.id.bt_findadd:
                //convertToAddress();
                Intent intent = new Intent(mContext,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_findfac:
                getAroundBizPoi(bt_fac_option.getText().toString());
                break;
        }
    }

    //3. 주소검색으로 위도, 경도 검색하기
    /* 명칭 검색을 통한 주소 변환 */
    public void convertToAddress() {
        //다이얼로그 띄워서, 검색창에 입력받음
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("POI 통합 검색");

        final EditText input = new EditText(mContext);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();
                TMapData tmapdata = new TMapData();

                tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback(){
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        for(int i=0; i< poiItem.size();i++)
                        {
                            TMapPOIItem item = poiItem.get(i);

                            Log.d("주소로 찾기", "POI Name: " + item.getPOIName().toString() +"," +
                                    "Address: " + item.getPOIAddress().replace("null","") +","+
                                    "Point: " + item.getPOIPoint().toString()
                            );
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //2. 주변 편의시설 검색하기
    /* 화면 중심의 위도 경도를 통한, 주변 편의시설 검색 */
    public void getAroundBizPoi(String option) {
        TMapData tmapdata = new TMapData();

        TMapPoint point = tmapview.getCenterPoint();

        tmapdata.findAroundNamePOI(point, option, 1, 99, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                for (int i=0; i< poiItem.size();i++) {
                    TMapPOIItem item = poiItem.get(i);
                    Log.d("편의시설","POI Name: " + item.getPOIName() +"," + "Address: " + item.getPOIAddress().replace("null","") + ",Point : "+ item.getPOIPoint().getLatitude() +","+ item.getPOIPoint().getLongitude());
                    m_mapPoint.add(new MapPoint(item.getPOIName(), item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude()));
                    showMarkerPoint();
                }
            }

        });
    }

    public void find_path() {
        new Thread(new Runnable() {
            public void run() {
                //마커생성
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                TMapMarkerItem markerItem2 = new TMapMarkerItem();

                // 마커 아이콘
                Context context = mContext;
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.poi_dot);

                // 3번째 파라미터 true == 지도 이동 Animation 사용
                tmapview.setCenterPoint(tMapPointStart.getLongitude(), tMapPointStart.getLatitude());

                //마커1(출발지)
                markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint(tMapPointStart); // 마커의 좌표 지정
                markerItem1.setName("출발지"); // 마커의 타이틀 지정

                //마커2(도착지)
                markerItem2.setIcon(bitmap); // 마커 아이콘 지정
                markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem2.setTMapPoint(tMapPointEnd); // 마커의 좌표 지정
                markerItem2.setName("N서울타워"); // 마커의 타이틀 지정

                //경로안내
                try {
                    TMapData tmapdata = new TMapData();
                    TMapPolyLine tMapPolyLine = tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(5);
                    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
                    for (int i = 0; i < alTMapPoint.size(); i++) {
                        tMapPolyLine.addLinePoint(alTMapPoint.get(i));
                    }
                    tmapview.addTMapPolyLine("Line1", tMapPolyLine);
                    tmapview.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                    tmapview.addMarkerItem("markerItem2", markerItem2); // 지도에 마커 추가

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Toast.makeText(mContext,"길찾기 완료" ,Toast.LENGTH_SHORT).show();
    }
    ValueEventListener postListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            m_mapPoint.clear();
            // 위에 선언한 저장소인 datas를 초기화하고
            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                catfeedItem item = snapshot.getValue(catfeedItem.class); // 컨버팅되서 Bbs로.......
                //System.out.println("테스트"+ item.longitude);
                if(item.longitude != null)
                    m_mapPoint.add(new MapPoint(item.info,  Double.parseDouble(item.longitude), Double.parseDouble(item.latitude)));
            }
            showMarkerPoint();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    ValueEventListener postListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            m_mapPoint2.clear();
            // 위에 선언한 저장소인 datas를 초기화하고
            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                catfeedItem item = snapshot.getValue(catfeedItem.class); // 컨버팅되서 Bbs로.......
                if(item.longitude != null)
                    m_mapPoint2.add(new MapPoint(item.info,  Double.parseDouble(item.longitude), Double.parseDouble(item.latitude)));
            }
            showMarkerPoint2();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


}
