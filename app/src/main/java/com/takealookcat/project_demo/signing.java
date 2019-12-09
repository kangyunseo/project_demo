package com.takealookcat.project_demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class signing extends AppCompatActivity {

    //비밀번호 확인 창 하나를 생성을 위함
    private LinearLayout dynamiclayout;
    private boolean layoutadded = false;

    //kakao
    private SessionCallback callback;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * 로그인 여부 확인
     * 자동 로그인
     * * check if state change
     *
     * button listener(수동 로그인)
     * * 파이어베이스 로그인 처리
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //kakao
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        //firebase
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(mAuth.getCurrentUser().isEmailVerified()){

                        String useremail ;
                        SharedPreferences pref = getSharedPreferences("sFile", MODE_PRIVATE);
                        useremail = pref.getString("email", "");

                        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
                        StorageReference storageReference2 = firebaseStorage.child("user/"+useremail);

                        storageReference2.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(signing.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(signing.this, signing2.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else{
                        sendEmail(mAuth);
                    }
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

        setContentView(R.layout.activity_signing);


        //firebase 로그인 회원가입 버튼 이벤트 처리
        mAuth = FirebaseAuth.getInstance();
        Button login_signup = (Button) findViewById(R.id.login_signup);
        login_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebase_login ();
            }
        });

        //kakao 로그인 버튼 이벤트는 api에서

    }

    /**
     * firebase 로그인 정의
     * 이메일, 비밀번호 유효성 검사
     * 일단 firebase 로그인 시도, 3가지 에러 캐치
     * 가입안된 이메일, 비밀번호 틀림, 그밖의 경우
     * 가입 안된 경우
     * 알림창 객체 생성, 비밀번호 재확인, 가입완료
     *
     * 로그인은 꼭 메일 인증한 메일주소만 통과시킴
     */
    public void firebase_login () {
        EditText email_input = (EditText) findViewById(R.id.email_input);
        EditText password_input = (EditText) findViewById(R.id.password_input);

        final String email = email_input.getText().toString();
        final String password = password_input.getText().toString();

        //todo 다이얼로그로 변경
        if(!validateEmail(email) || !validatePassword(password)) {
            Toast.makeText(signing.this, "아직 지원하지 않는 메일 도메인 입니다",
                    Toast.LENGTH_SHORT).show();
        } else {
            //해당 이메일로 처음 로그인 시도 하는 경우
            if(layoutadded) {
                EditText password_confirm_input = (EditText) findViewById(R.id.password_confirm);
                String password_confirm = password_confirm_input.getText().toString();

                //비밀번호 재확인
                if(password.equals(password_confirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signing.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(signing.this, "이미 가입된 이메일입니다 메일함을 확인해주세요",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(signing.this, "알수없는 이유로 가입 못함", Toast.LENGTH_SHORT).show();
                            } else {
                                sendEmail(mAuth);
                                Toast.makeText(signing.this, "가입 성공 메일함을 확인해주세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(signing.this, "비밀번호를 재확인 해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            //if(layoutadded)의 else
            //다이얼로그에서 yes하면 회원가입 절차진행
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(signing.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    ////////////////////////////////////////////////////////////////////////////////
                    //다이얼로그 설정
                    AlertDialog.Builder builder = new AlertDialog.Builder(signing.this);
                    builder.setTitle("처음보네");
                    builder.setMessage("가입허쉴?");
                    builder.setPositiveButton("그랭", new DialogInterface.OnClickListener(){
                        //오른쪽 버튼 = positive 버튼 처리
                        public void onClick(DialogInterface dialog, int whichButton){
                            add_view_dynamic ();
                        }
                    });
                    builder.setNegativeButton("아니", new DialogInterface.OnClickListener(){
                        //왼쪽 버튼 = negative 버튼 처리
                        public void onClick(DialogInterface dialog, int whichButton){
                        }
                    });
                    //다이얼로그 정의 끝
                    ////////////////////////////////////////////////////////////////////////////////

                    //로그인 실패시 분기
                    if (!task.isSuccessful()){
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException invaildEmail) {
                            AlertDialog dialog = builder.create();    // 알림창 객체 생성
                            if(!layoutadded) {
                                dialog.show();
                            }
                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                            Toast.makeText(signing.this, "비밀번호 틀림", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(signing.this, "알수없는 이유로 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        //아래는 성공적으로 로그인 한 경우다 verified 된 이메일인지 검사해야함
                    } else {
                        //if 분기로 인증 벨리데이션 추가
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            //로그인 성공시 이메일 저장
                            SharedPreferences pref = getSharedPreferences("sFile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.commit();


                            //로그인 성공 이벤트 추가
                            //Intent intent = new Intent(signing.this, MainActivity.class);
                            //signing2.class에서 프로필 사진 닉네임등 설정 이어가기
                            String useremail ;
                            SharedPreferences pref2 = getSharedPreferences("sFile", MODE_PRIVATE);
                            useremail = pref2.getString("email", "");

                            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference storageReference2 = firebaseStorage.child("user/"+useremail);

                            storageReference2.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(signing.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Intent intent = new Intent(signing.this, signing2.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            ////////////////////////////////////////////////////////////////////////
                            //이메일 인증 다이얼로그 설정
                            /*AlertDialog.Builder veri = new AlertDialog.Builder(signing.this);
                            builder.setTitle("이메일");
                            builder.setMessage("메일함을 확인해주세요?");
                            builder.setPositiveButton("그랭", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){
                                }
                            });*/
                            //이메일 인증 다이얼로그 정의 끝
                            ////////////////////////////////////////////////////////////////////////
                            sendEmail(mAuth);
                        }
                    }
                }
            });
        }
    }

    //비밀번호 확인을 위해 비밀번호 입력창 하나 더 생성
    public void add_view_dynamic () {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        EditText confirm_password = new EditText(signing.this);
        confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirm_password.setBackground(ContextCompat.getDrawable(signing.this, R.drawable.layer_list));
        confirm_password.setHeight((int) pixels);
        confirm_password.setId(R.id.password_confirm);

        pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        dynamiclayout = (LinearLayout) findViewById(R.id.dynamiclayout);
        dynamiclayout.addView(confirm_password);
        dynamiclayout.setPadding(0, (int) pixels,0,0);

        layoutadded = true;
    }

    //todo 학교 이메일만 가능하게 쭉 가져오기
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            //= Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
            = Pattern.compile("^[A-Z0-9._%+-]+@+(sogang.ac.kr)|(skku.edu)$"
            , Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM
            = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }

    //인증메일 보내기
    public void sendEmail(FirebaseAuth mAuth) {
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(signing.this, "mail sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    //firbase 로그인 정의 끝
    ////////////////////////////////////////////////////////////////////////////////////////////


    //kakao login 로그인 정의 시작
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
            //onDestroy();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }

        public void requestMe() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String email = result.getKakaoAccount().getEmail();
                    String profile_s = result.getKakaoAccount().getProfile().toString();

                    //로그인 성공시 이메일 저장
                    SharedPreferences pref = getSharedPreferences("sFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", email);
                    //이미지 url 저장
                    List<String> extractedUrls = extractUrls(profile_s);
                    for (String url : extractedUrls) {
                        //System.out.println(url);
                        editor.putString("profile_image_url", url.trim());
                    }
                    editor.commit();
                    Toast.makeText(signing.this, email, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(signing.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
                }

            });
        }
    }
    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    //kakao login 로그인 정의 끝
    ////////////////////////////////////////////////////////////////////////////////////////////
}
