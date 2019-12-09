package com.takealookcat.project_demo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragDonaItem extends Fragment implements MainActivity.onKeyBackPressedListener{
    public String key;
    public String title ;
    public String content ;
    public String startDate ;
    public String dueDate ;
    public String targetAmount;
    public String curAmount;
    public String file;
    public Button btn_dona;

    //뒤로가기 구현
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //메인뷰 액티비티의 뒤로가기 callback 붙이기
        ((MainActivity)context).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBackKey() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FragCateDona())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_dona_item, container, false);

        btn_dona = (Button)v.findViewById(R.id.btn_dona);
        btn_dona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),kakaoPay.class);
                startActivity(intent);
            }
        });

        TextView ttitle = (TextView) v.findViewById(R.id.ttitle);
        TextView tcontent = (TextView) v.findViewById(R.id.tcontent);
        TextView tcurAmount = (TextView) v.findViewById(R.id.tcurAmount);
        TextView ttargetAmount = (TextView) v.findViewById(R.id.ttargetAmount);
        TextView tdueDate = (TextView) v.findViewById(R.id.tdueDate);
        TextView tstartDate = (TextView) v.findViewById(R.id.tstartDate);

        final ImageView iconImageView = (ImageView) v.findViewById(R.id.imageView) ;

        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            title = extra.getString("title");
            content = extra.getString("content");
            startDate = extra.getString("startDate");
            dueDate = extra.getString("dueDate");
            targetAmount = extra.getString("targetAmount");
            curAmount = extra.getString("curAmount");
            file = extra.getString("file");
            ttitle.setText(title);
            tcontent.setText(content);
            tcurAmount.setText(curAmount);
            ttargetAmount.setText(targetAmount);
            tdueDate.setText(dueDate);
            tstartDate.setText(startDate);
        }

        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = firebaseStorage.child("donation/"+file);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }


}
