package com.takealookcat.project_demo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class codebox {
    //확인취소 다이얼로그
    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyActivity.this);
    alert_confirm.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
        new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // 'YES'
        }
    }).setNegativeButton("취소",
        new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // 'No'
            return;
        }
    });
    AlertDialog alert = alert_confirm.create();
    alert.show();



    //파이어베이스 현재상태(로그인 중인지)를 채크
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d("", "onAuthStateChanged:signed_out");
        }
    }



    //파이어베이스 현재 가입여부
    mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
        @Override
        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                List<String> signInMethods = result.getSignInMethods();
                if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                    Toast.makeText(signing.this, "already signup",
                            Toast.LENGTH_SHORT).show();
                } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                    // User can sign in with email/link
                }
            } else {
                Log.e("", "Error getting sign in methods for user", task.getException());
            }
        }
    });




    //파이어베이스에 계정 추가 (회원가입
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(signing.this, "already signup",
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(signing.this, "signup failed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    });



    //파이어베이스에 로그인
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {
                Log.w("TAG", "signInWithEmail", task.getException());
                Toast.makeText(signing.this, "sign fail",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(signing.this, "signInWithEmail",
                        Toast.LENGTH_SHORT).show();
            }
        }
    });



    //인증메일 보내기
    mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            Toast.makeText(mContext, "mail sent",
                    Toast.LENGTH_SHORT).show();
        }
    });



    //버튼눌리면 칸추가
    Button YorN = (Button)findViewById(R.id.login_signup);
        YorN.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout thislay = (LinearLayout)findViewById(R.id.thislay);

            EditText addview = new EditText(signing.this);
            addview.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            addview.setBackground(ContextCompat.getDrawable(signing.this, R.drawable.layer_list));

            dynamicLayout = (LinearLayout) findViewById(R.id.add_view);
            dynamicLayout.addView(addview);
            btn_clicked = true;
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

            dynamicLayout.setPadding(0, 100,0,0);
        }
    });
}
