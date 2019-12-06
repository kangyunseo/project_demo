package com.takealookcat.project_demo;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class signing extends AppCompatActivity {

    private LinearLayout dynamiclayout;
    private boolean layoutadded = false;

    private SessionCallback callback;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //kakao 리스너
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        //firebase 리스너
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(signing.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

        setContentView(R.layout.activity_signing);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //firbase 시작
        //firebase 로그인 인스턴스 호출
        mAuth = FirebaseAuth.getInstance();

        //로그인 버튼 이벤트
        Button login_signup = (Button) findViewById(R.id.login_signup);
        login_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebase_login ();
            }
        });
        //firbase 로그인 끝
        ////////////////////////////////////////////////////////////////////////////////////////////


        //kakao login 로그인 시작
        ////////////////////////////////////////////////////////////////////////////////////////////

        //kakao login 로그인 끝
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    //firbase 로그인 정의 시작
    ////////////////////////////////////////////////////////////////////////////////////////////
    public void firebase_login () {
        EditText email_input = (EditText) findViewById(R.id.email_input);
        EditText password_input = (EditText) findViewById(R.id.password_input);

        final String email = email_input.getText().toString();
        String password = password_input.getText().toString();

        if(!validateEmail(email) || !validatePassword(password)) {
            Toast.makeText(signing.this, "불가능한 이메일 비밀번호",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(layoutadded) {
                EditText password_confirm_input = (EditText) findViewById(R.id.password_confirm);
                String password_confirm = password_confirm_input.getText().toString();

                if(password.equals(password_confirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signing.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(signing.this, "이미 가입된 이메일",
                                            Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(signing.this, "알수없는 이유로 가입 못함",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(signing.this, "가입 성공",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.d("", "재확인ㄴㄴㄴㄴㄴㄴ");
                    Toast.makeText(signing.this, "비밀번호 재확인", Toast.LENGTH_SHORT).show();
                }
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(signing.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //다이얼로그 설정
                    AlertDialog.Builder builder = new AlertDialog.Builder(signing.this);
                    builder.setTitle("처음보네");
                    builder.setMessage("가입허쉴?");
                    builder.setPositiveButton("그랭", new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            add_view_dynamic ();
                        }
                    });
                    builder.setNegativeButton("아니", new DialogInterface.OnClickListener(){
                        // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                        }
                    });

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
                            Log.d("", "wrong pass wordddddddd");
                            Toast.makeText(signing.this, "비밀번호 틀림", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.d("", "loooooooooooooooooooooogin!no");
                            Toast.makeText(signing.this, "알수없는 이유로 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //if 분기로 인증 벨리데이션 추가
                        //로그인 성공시 이메일 저장
                        SharedPreferences pref = getSharedPreferences("sFile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", email);
                        editor.commit();


                        //로그인 성공 이벤트 추가
                        Log.d("", "loooooooooooooooooooooogin!");
                        Toast.makeText(signing.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(signing.this, MainActivity.class);
                        Intent intent = new Intent(signing.this, signing2.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

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
                        Toast.makeText(signing.this, "mail sent",
                                Toast.LENGTH_SHORT).show();
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
