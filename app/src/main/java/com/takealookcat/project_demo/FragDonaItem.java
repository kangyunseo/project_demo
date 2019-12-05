package com.takealookcat.project_demo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class FragDonaItem extends Fragment {
    public String key;
    public String title ;
    public String content ;
    public String startDate ;
    public String dueDate ;
    public String targetAmount;
    public String curAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_dona_item, container, false);

        TextView ttitle = (TextView) v.findViewById(R.id.ttitle);
        TextView tcontent = (TextView) v.findViewById(R.id.tcontent);
        TextView tcurAmount = (TextView) v.findViewById(R.id.tcurAmount);
        TextView ttargetAmount = (TextView) v.findViewById(R.id.ttargetAmount);
        TextView tdueDate = (TextView) v.findViewById(R.id.tdueDate);
        TextView tstartDate = (TextView) v.findViewById(R.id.tstartDate);


        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            title = extra.getString("title");
            content = extra.getString("content");
            startDate = extra.getString("startDate");
            dueDate = extra.getString("dueDate");
            targetAmount = extra.getString("targetAmount");
            curAmount = extra.getString("curAmount");
            ttitle.setText(title);
            tcontent.setText(content);
            tcurAmount.setText(curAmount);
            ttargetAmount.setText(targetAmount);
            tdueDate.setText(dueDate);
            tstartDate.setText(startDate);
        }
        return v;
    }


}
