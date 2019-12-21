package com.takealookcat.project_demo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class tab_1 extends Fragment {

    ImageView ivPreview;

    ImageButton btChoose;
    ImageButton btUpload;

    Uri filePath;
    EditText cat_info, cat_Content, date_now;
    TextView latitude;
    TextView longitude;
    TextView exiftext;

    FirebaseDatabase database;
    DatabaseReference catRef;
    DatabaseReference allRef;
    DatabaseReference locationRef;

    Geocoder geocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.tab_1,container,false);

        //파베
        database = FirebaseDatabase.getInstance();
        catRef = database.getReference("cat");
        allRef = database.getReference("all");
        locationRef = database.getReference("location");

        //cat_Title= (EditText)rootview.findViewById(R.id.cat_title);
        cat_Content = (EditText)rootview.findViewById(R.id.cat_context);
        cat_info = (EditText)rootview.findViewById(R.id.cat_info);
        date_now = (EditText)rootview.findViewById(R.id.date_now);
        Date currentTime = Calendar.getInstance().getTime();
        String date_text = new SimpleDateFormat("yyyy-MM-dd, EE", Locale.getDefault()).format(currentTime);
        date_now.setText(date_text);

        exiftext = (TextView) rootview.findViewById(R.id.exif);
        //업로드
        btChoose = (ImageButton) rootview.findViewById(R.id.bt_choose);
        btUpload = (ImageButton) rootview.findViewById(R.id.bt_upload);

        // 미리보기
        ivPreview = (ImageView) rootview.findViewById(R.id.ivPreview);

        //exif
        latitude = (TextView) rootview.findViewById(R.id.latitude);
        longitude = (TextView) rootview.findViewById(R.id.longitude);

        geocoder = new Geocoder(getContext());

        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드
                uploadFile();
            }
        });

        return rootview;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData(); // uri값
            String filename = getPath(getContext(),filePath); // uri값으로 절대경로 값
            //Log.d(TAG, "uri:" + String.valueOf(filePath));

            try {
                ExifInterface exif = new ExifInterface(filename);
                String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                exiftext.setText(datetime);
                float lon = getTagString(exif,0);
                float lat = getTagString(exif,1);
                //float longitude = convertToDegree(lon);
                longitude.setText(String.valueOf(lon));
                latitude.setText(String.valueOf(lat));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
            }

            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                btChoose.setImageBitmap(rotateImage(bitmap,getOrientationOfImage(filename)));
                //ivPreview.setImageBitmap(rotateImage(bitmap,90));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }

    /* exif*/
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            final String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://projectdemo-5609c.appspot.com").child("cat/" + filename);
            //올라가거라...

            //FirebaseDatabase database = FirebaseDatabase.getInstance();
            //final DatabaseReference myRef = database.getReference("message");

            //final String cattitle = cat_Title.getText().toString();
            final String catcontext = cat_Content.getText().toString();
            final String catinform = cat_info.getText().toString();
            //
            final String datenow = date_now.getText().toString();

            final String lati= latitude.getText().toString();
            final String longi = longitude.getText().toString();

            SharedPreferences sf = getActivity().getSharedPreferences("sFile",MODE_PRIVATE);
            //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
            final String email = sf.getString("email","");

            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기

                            //cat
                            String key = catRef.push().getKey();
                            Map<String, String > postValues = new HashMap<>();
                            //postValues.put("title", cattitle);
                            postValues.put("content", catcontext);
                            postValues.put("file", filename);
                            postValues.put("info", catinform);
                            postValues.put("date", datenow);
                            postValues.put("email", email);

                            DatabaseReference keyRef = catRef.child(key);
                            keyRef.setValue(postValues);

                            //location
                            String key2 = locationRef.push().getKey();
                            Map<String, String > postValues2 = new HashMap<>();
                            //postValues.put("title", cattitle);
                            postValues2.put("latitude", lati);
                            postValues2.put("longitude", longi);
                            postValues2.put("category", "고양이");
                            postValues2.put("file", filename);

                            DatabaseReference keyRef2 = locationRef.child(key2);
                            keyRef2.setValue(postValues2);

                            //all
                            String key3 = allRef.push().getKey();
                            Map<String, String > postValues3 = new HashMap<>();
                            postValues3.put("type", "cat");
                            postValues3.put("content", catcontext);
                            postValues3.put("file", filename);
                            postValues3.put("info", catinform);
                            postValues3.put("date", datenow);
                            postValues3.put("email", email);

                            DatabaseReference keyRef3 = allRef.child(key3);
                            keyRef3.setValue(postValues3);

                            //myRef.push().setValue(filename);
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            //addPoint();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    /* exif*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;

    };

    private Float getTagString ( ExifInterface exif,int a) {
        Float latitude =0.0F , longitude =0.0F;
        boolean valid = false;

        String attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        if ((attrLATITUDE != null) && (attrLATITUDE_REF != null) && (attrLONGITUDE != null)
                && (attrLONGITUDE_REF != null)) {

            valid = true;

            if (attrLATITUDE_REF.equals("N")) {
                latitude = convertToDegree(attrLATITUDE);
            } else {
                latitude = 0 - convertToDegree(attrLATITUDE);
            }

            if (attrLONGITUDE_REF.equals("E")) {
                longitude = convertToDegree(attrLONGITUDE);
            } else {
                longitude = 0 - convertToDegree(attrLONGITUDE);
            }
        }

        if(a == 0) {
            return latitude;
        }
        else if (a==1){
            return longitude;
        }

        return 0.0F;
    }

}
