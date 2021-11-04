package com.example.sleep_project;

import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class UI_1_3_login_activity extends AppCompatActivity {
    AboutLogin aboutLogin = new AboutLogin(); //로그인관련 변수와 로그인유무 파악하는 메소드 들어있는 함수 초기화
    private SignInButton signInButton;
    Button nologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_1_3_login_layout);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("741846607454-8jg548l83066ap18d8scimntofkuukvm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInButton = findViewById(R.id.sign_in_button);
        aboutLogin.setmAuth(FirebaseAuth.getInstance());
        aboutLogin.setGoogleSignInClient(GoogleSignIn.getClient(this, gso));
        nologin=(Button)findViewById(R.id.nologinbtn);

        //이미 로그인이 되어있다면 바로 UI_2_Maintimertab으로 넘어가게 하는코드
        if (aboutLogin.checklogin()) {
            Intent intent = new Intent(getApplication(), UI_2_Maintimertab.class);
            //테스트용으로 만든 intent
            startActivity(intent);
            finish();
        }
        //로그인버튼 누르면 로그인이 뜸
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("logintest","로그인 버튼 누른거 인식");
                signIn();
            }
        });

        //로그인 하지 않고 실행 - 정상동작
        nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(intent);
                Toast.makeText(UI_1_3_login_activity.this, "로그인하지 않고 실행합니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = aboutLogin.getGoogleSignInClient().getSignInIntent();
        Log.d("logintest","signin 호출되어 실행됨");
        startActivityForResult(signInIntent, aboutLogin.getRcSignIn());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("logintest","인텐트 호출 이후 결과값 받아지는 함수호출됨");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == aboutLogin.getRcSignIn()) {
            Log.d("logintest","if문 조건 맞아서 하단으로 떨어짐");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.d("logintest","로그인하는 try문 접근됨");
            } catch (ApiException e) {
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("logintest","로그인과정으로 넘어옴");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        aboutLogin.getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            Log.d("logintest","onComplete 함수 호출됨");
                            FirebaseUser user = aboutLogin.getmAuth().getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Log.d("logintest","여기가 떳다면 어디서 에러가 뜬것");
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            //Intent intent = new Intent(this, UI_1_4_UserInfo_Activity.class);
            Log.d("logintest", "updateUI 까지 넘어옴");
            Intent intent = new Intent(this, UI_2_Maintimertab.class);
            startActivity(intent);
            finish();
        }
    }
}