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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragDonaItem extends Fragment{
    public String key;
    public String title ;
    public String content ;
    public String startDate ;
    public String dueDate ;
    public String targetAmount;
    public String curAmount;
    public String file;
    public ImageButton btn_dona;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_dona_item, container, false);

        btn_dona = (ImageButton)v.findViewById(R.id.bt_dona);
        btn_dona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),kakaoPay.class);
                startActivity(intent);
            }
        });

        TextView ttitle = (TextView) v.findViewById(R.id.donaTitle);
        TextView ttargetAmount = (TextView) v.findViewById(R.id.targetAmount);
        TextView tstartDate = (TextView) v.findViewById(R.id.startDate);
        TextView tdueDate = (TextView) v.findViewById(R.id.dueDate);

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
            //tcontent.setText(content);
            //tcurAmount.setText(curAmount);
            ttargetAmount.setText(targetAmount);
            tdueDate.setText(dueDate);
            tstartDate.setText(startDate);
        }
        return v;
    }

}
