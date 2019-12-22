package com.takealookcat.project_demo;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class tab_4 extends Fragment {
    ImageView ivPreview4;
    EditText editTitle, editCcontent;
    TextView startDate, dueDate;
    EditText targetAmount;
    ImageButton btChoose;
    ImageButton btUpload;
    FirebaseDatabase database;
    DatabaseReference donaRef;
    DatabaseReference allRef;
    Uri filePath;
    LinearLayout startDateLayout, dueDateLayout;


    boolean startDateFlag, dueDateFlag; // 1일때 날짜 작성

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.tab_4,container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        donaRef = database.getReference("donation");
        allRef = database.getReference("all");

        btChoose= ( ImageButton)rootview.findViewById(R.id.btn_dona);
        btUpload = ( ImageButton) rootview.findViewById(R.id.bt_upload4);

        editTitle = (EditText)rootview.findViewById(R.id.editTitle);
        editCcontent = (EditText)rootview.findViewById(R.id.editContent);
        startDate = (TextView)rootview.findViewById(R.id.startDate);
        dueDate = (TextView)rootview.findViewById(R.id.dueDate);
        targetAmount = (EditText)rootview.findViewById(R.id.targetAmount);

        // 기간 레이아웃
        startDateLayout = (LinearLayout)rootview.findViewById(R.id.startDateLayout);
        dueDateLayout = (LinearLayout)rootview.findViewById(R.id.dueDateLayout);
        // 미리보기
        ivPreview4 = (ImageView) rootview.findViewById(R.id.ivPreview4);

        // 3자리마다 컴마
//        DecimalFormat myFormatter = new DecimalFormat("###,###");
  //      String formattedStringPrice = myFormatter.format(intPrice);

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

        // 시작일 선택
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜 가져오기
                Date currentTime = Calendar.getInstance().getTime();
                String year_text = new SimpleDateFormat("yyyy", Locale.getDefault()).format(currentTime);
                int todayYear = Integer.parseInt(year_text);
                String month_text = new SimpleDateFormat("MM", Locale.getDefault()).format(currentTime);
                int todayMonth = Integer.parseInt(month_text);
                String day_text = new SimpleDateFormat("dd", Locale.getDefault()).format(currentTime);
                int todayDay = Integer.parseInt(day_text);

                //Toast.makeText(view.getContext(), year_text + month_text + day_text, Toast.LENGTH_SHORT).show();

                // DatePickerDialog 날짜 설정 후, 열기
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), listener, todayYear, todayMonth - 1, todayDay);
                dialog.show();

                startDateFlag = true;
            }
        });

        // 마감일 선택
        dueDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜 가져오기
                Date currentTime = Calendar.getInstance().getTime();
                String year_text = new SimpleDateFormat("yyyy", Locale.getDefault()).format(currentTime);
                int todayYear = Integer.parseInt(year_text);
                String month_text = new SimpleDateFormat("MM", Locale.getDefault()).format(currentTime);
                int todayMonth = Integer.parseInt(month_text);
                String day_text = new SimpleDateFormat("dd", Locale.getDefault()).format(currentTime);
                int todayDay = Integer.parseInt(day_text);

                //Toast.makeText(view.getContext(), year_text + month_text + day_text, Toast.LENGTH_SHORT).show();

                // DatePickerDialog 날짜 설정 후, 열기
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), listener, todayYear, todayMonth - 1, todayDay);
                dialog.show();

                dueDateFlag = true;
            }
        });

        return rootview;
    }

    // 날짜선택 리스너
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            String selectedYear, selectedMonth, selectedDay;
            selectedYear = String.valueOf(year);
            selectedMonth = String.valueOf(monthOfYear);
            selectedDay = String.valueOf(dayOfMonth);

            if (startDateFlag == true) {
                startDate.setText(selectedYear + "-" + selectedMonth + "-" + selectedDay);
                startDateFlag = false;
            }
            else if (dueDateFlag == true) {
                dueDate.setText(selectedYear + "-" + selectedMonth + "-" + selectedDay);
                dueDateFlag = false;
            }
        }
    };

    //

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData(); // uri값
            String filename = getPath(getContext(),filePath); // uri값으로 절대경로 값
            //Log.d(TAG, "uri:" + String.valueOf(filePath));

            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //btChoose.setImageBitmap(rotateImage(bitmap,getOrientationOfImage(filename)));
                // 미리보기 이미지 설정
                ivPreview4.setAdjustViewBounds(true);
                ivPreview4.requestLayout();
                ivPreview4.setImageBitmap(rotateImage(bitmap,getOrientationOfImage(filename)));
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
            StorageReference storageRef = storage.getReferenceFromUrl("gs://projectdemo-5609c.appspot.com").child("donation/" + filename);
            //올라가거라...

            //FirebaseDatabase database = FirebaseDatabase.getInstance();
            //final DatabaseReference myRef = database.getReference("message");
            SharedPreferences sf = getActivity().getSharedPreferences("sFile",MODE_PRIVATE);
            //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
            final String email = sf.getString("email","");

            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기

                            String title = editTitle.getText().toString();
                            String content = editCcontent.getText().toString();
                            String start = startDate.getText().toString();
                            String due = dueDate.getText().toString();
                            String target = targetAmount.getText().toString();

                            // 6
                            // 6.1 donation 레퍼런스 (테이블)에 키를 생성한다.
                            String key = donaRef.push().getKey();

                            // 6.2 입력될 키, 값 세트 (레코드)를 생성.
                            Map<String, String > postValues = new HashMap<>();
                            postValues.put("title", title);
                            postValues.put("content", content);
                            postValues.put("startDate", start);
                            postValues.put("dueDate", due);
                            postValues.put("targetAmount", target);
                            postValues.put("curAmount", "0");
                            postValues.put("file", filename);
                            postValues.put("email", email);


                            DatabaseReference keyRef = donaRef.child(key);
                            keyRef.setValue(postValues);

                            //all
                            String key3 = allRef.push().getKey();
                            Map<String, String > postValues3 = new HashMap<>();
                            postValues3.put("type", "donation");
                            postValues.put("title", title);
                            postValues.put("content", content);
                            postValues.put("startDate", start);
                            postValues.put("dueDate", due);
                            postValues.put("targetAmount", target);
                            postValues.put("curAmount", "0");
                            postValues.put("file", filename);
                            postValues.put("email", email);
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
}
