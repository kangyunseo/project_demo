package com.takealookcat.project_demo;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllItemListAdapter extends BaseAdapter{
    private final static String TAG = "PINGPONG";
    List<AllItem> datas;
    Context context;
    LayoutInflater inflater;

    public AllItemListAdapter(List<AllItem> datas, Context context){
        Log.i(TAG, "=================================ListAdapter()");
        this.datas = datas;
        this.context = context;
        this.inflater = (LayoutInflater) context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.i(TAG, "=================================getCount()");
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG, "=================================getItem()");
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "=================================getItemId()");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        TextView email = (TextView) convertView.findViewById(R.id.textView3);
        //final ImageView profile = (ImageView) convertView.findViewById(R.id.imageView2);
        final CircleImageView profile = (CircleImageView) convertView.findViewById(R.id.imageView2);
        //라운딩입니다.
        GradientDrawable drawable=
                (GradientDrawable)  context.getResources().getDrawable(R.drawable.background_thema);
        iconImageView.setBackground(drawable);
        iconImageView.setClipToOutline(true);
/*
        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.img1); // 비트맵 이미지를 만든다.
        int width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
        int height=(int)((width*불러올 이미지의 높이)/불러올 이미지의 너비); // 세로 사이즈 지정
        Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, width, height, true); // 이미지 사이즈 조정
        imgview.setImageBitmap(resizedbitmap); // 이미지뷰에 조정한 이미지 넣기
*/
        profile.setBackground(drawable);
        profile.setClipToOutline(true);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        AllItem all = datas.get(position);

        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = null;

        descTextView.setText(all.info);
        if((all.type).equals("cat")) {
           storageReference = firebaseStorage.child("cat/" + all.file);
        }
        else if(all.type.equals("feed")){
            storageReference = firebaseStorage.child("feed/" + all.file);
        }
        else if(all.type.equals("donation")){
            storageReference = firebaseStorage.child("donation/" + all.file);
            descTextView.setText(all.title);
        }

        String useremail = all.email;
        StorageReference storageReference2 = firebaseStorage.child("user/"+useremail);

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(context)
                            .load(task.getResult())
                            .into(iconImageView);
                    //Glide.with(context).load(task.getResult()).apply(new RequestOptions().circleCrop()).into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        storageReference2.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(context)
                            .load(task.getResult())
                            .into(profile);
                    //Glide.with(context).load(task.getResult()).apply(new RequestOptions().circleCrop()).into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(all.content);

        email.setText(all.email);
        return convertView;
    }

}