package com.example.sleep_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UI_5_AccountTab extends AppCompatActivity {
    Button  sign_out;
    TextView user_email,user_name,inputPerInfo;
    ImageView user_profile;
    AboutLogin aboutLogin = new AboutLogin();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_5_accounttab);
        user_email=findViewById(R.id.profile_email);//계정정보 확인탭 이메일
        user_name=findViewById(R.id.profile_text);//계정정보 확인탭 유저이름
        user_profile=findViewById(R.id.profile_image);//계정정보 확인탭 이미지
        sign_out=(Button)findViewById(R.id.sign_out); //로그아웃 버튼
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        aboutLogin.setGoogleSignInClient(GoogleSignIn.getClient(this, gso));

        if(aboutLogin.checklogin()) {
            String email = aboutLogin.getUser().getEmail();
            String name = aboutLogin.getUser().getDisplayName();
            //getFirebaseDatabase();
            String dbgender;
            String dbage;
            String dbjob;
            user_email.setText(email);
            user_name.setText(name);
            sign_out.setText("로그아웃");
            Picasso.get().load(aboutLogin.getUser().getPhotoUrl()).centerInside().fit().into(user_profile);
        }else{
            user_email.setText("로그인해주세요");
            user_name.setText("로그인해주세요");
            sign_out.setText("로그인");
        }
        //로그아웃 버튼 눌렀을때
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aboutLogin.checklogin()){
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
        /////////////////////////////// 탭 이동 버튼 관련////////////////////////////////////////
    }
    //로그아웃 메소드
    private void signOut() {
            FirebaseAuth.getInstance().signOut();
    }

    //해당 화면에서도 로그인 가능하도록 하는 코드들
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
            Intent intent = new Intent(this, UI_2_Maintimertab.class);
            startActivity(intent);
            finish();
        }
    }
    //파이어베이스 데이터 가져오는 메소드 - 미완성
    //참고자료 : https://m.blog.naver.com/PostView.naver?blogId=nife0719&logNo=221049879862&proxyReferer=https:%2F%2Fwww.google.com%2F
/*    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
*//*                arrayData.clear();
                arrayIndex.clear();*//*
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    firebasepost get = postSnapshot.getValue(firebasepost.class);
                    username=get.getUsername();
                    String[] info = {get.getGender(), get.getAge(), get.getJob()};
                    //String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);
*//*                    arrayData.add(Result);
                    arrayIndex.add(key);*//*
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
*//*                arrayAdapter.clear();
                arrayAdapter.addAll(arrayData);
                arrayAdapter.notifyDataSetChanged();*//*
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("글리온");
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }*/
}
