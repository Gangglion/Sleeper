package com.example.sleep_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class UI_5_AccountTab extends AppCompatActivity {
    Button main, music, statistics, account, sign_out;
    TextView user_email,user_name;
    ImageView user_profile;
    private FirebaseUser user;
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_5_accounttab);
        user = FirebaseAuth.getInstance().getCurrentUser(); //로그인된 정보 받아옴
        user_email=findViewById(R.id.profile_email);//계정정보 확인탭 이메일
        user_name=findViewById(R.id.profile_text);//계정정보 확인탭 유저이름
        user_profile=findViewById(R.id.profile_image);//계정정보 확인탭 이미지
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        account = (Button) findViewById(R.id.set); //설정기능 버튼*/
        account.setBackgroundColor(Color.GRAY);
        sign_out=(Button)findViewById(R.id.sign_out); //로그아웃 버튼
        mAuth = FirebaseAuth.getInstance();
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(user!=null) {
            String email = user.getEmail();
            String name = user.getDisplayName();
            user_email.setText(email);
            user_name.setText(name);
            sign_out.setText("로그아웃");
            Picasso.get().load(user.getPhotoUrl()).centerInside().fit().into(user_profile);
        }else{
            user_email.setText("로그인해주세요");
            user_name.setText("로그인해주세요");
            sign_out.setText("로그인");
        }
        //로그아웃 버튼 눌렀을때
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    signOut();
                    Toast.makeText(UI_5_AccountTab.this, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                    startActivity(intent);
                    finish();
                }else{
                    signIn();
                    Toast.makeText(UI_5_AccountTab.this, "로그인 되었습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //메인기능 탭 눌렀을때 이동하는 기능
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(intent);
                finish();
            }
        });
        //음악기능 탭 눌렀을때 이동하는 기능
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                finish();
            }
        });
        //통계기능 탭 눌렀을때 이동하는 기능
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //로그아웃 메소드
    private void signOut() {
            FirebaseAuth.getInstance().signOut();
    }

    //해당 화면에서도 로그인 가능하도록 하는 코드들
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
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
            Intent intent = new Intent(this, UI_2_Maintimertab.class);
            startActivity(intent);
            finish();
        }
    }

}
