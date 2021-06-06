package com.example.sleep_project;

import android.content.Intent;
import android.os.Bundle;
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
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInButton = findViewById(R.id.sign_in_button);
        aboutLogin.setmAuth(FirebaseAuth.getInstance());
        aboutLogin.setGoogleSignInClient(GoogleSignIn.getClient(this, gso));
        nologin=(Button)findViewById(R.id.nologinbtn);

        //이미 로그인이 되어있다면 바로 UI_2_Maintimertab으로 넘어가게 하는코드
        if (aboutLogin.checklogin()) {
            //Intent intent = new Intent(getApplication(), UI_1_4_UserInfo_Activity.class);
            //테스트용으로 만든 intent
            Intent intent = new Intent(getApplication(), UI_2_Maintimertab.class);
            startActivity(intent);
            finish();
        }
        //로그인버튼 누르면 로그인이 뜸
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                signIn();
            }
        });
        nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                Intent intent=new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                Toast.makeText(UI_1_3_login_activity.this, "로그인하지 않고 실행합니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = aboutLogin.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, aboutLogin.getRcSignIn());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == aboutLogin.getRcSignIn()) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        aboutLogin.getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = aboutLogin.getmAuth().getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, UI_1_4_UserInfo_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}