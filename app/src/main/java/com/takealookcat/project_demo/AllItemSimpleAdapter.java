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

public class AllItemSimpleAdapter extends BaseAdapter {
    private final static String TAG = "PINGPONG";
    List<AllItem> datas;
    Context context;
    LayoutInflater inflater;

    public AllItemSimpleAdapter(List<AllItem> datas, Context context){
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

            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item_commupage, parent, false);
        }
        TextView email = (TextView) convertView.findViewById(R.id.textView10);
        TextView text_cate = (TextView) convertView.findViewById(R.id.text_cate) ;
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        AllItem item = datas.get(position);
        if(item.type.equals("cat")){
            TextView basetextTitle = (TextView) convertView.findViewById(R.id.textView3) ;
            basetextTitle.setText(item.info);
            text_cate.setText("고양이");
        }

        else if(item.type.equals("feed")){
            TextView basetextTitle = (TextView) convertView.findViewById(R.id.textView3) ;
            basetextTitle.setText(item.info);
            text_cate.setText("급식소");
        }
        else if(item.type.equals("donation")){
            TextView basetextTitle = (TextView) convertView.findViewById(R.id.textView3) ;
            basetextTitle.setText(item.title);
            text_cate.setText("기부");
        }
        email.setText(item.email);
        return convertView;
    }

}