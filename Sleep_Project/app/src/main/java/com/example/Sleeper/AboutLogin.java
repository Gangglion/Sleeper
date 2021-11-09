//로그인 관련 정보와 로그인유무 판단해서 boolean값 리턴해주는 메소드가 있는 클래스입니다.
package com.example.Sleeper;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AboutLogin {
    private FirebaseUser user;
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    //GoogleSignInClient의 getter/setter
    public GoogleSignInClient getGoogleSignInClient(){
        return mGoogleSignInClient;
    }
    public void setGoogleSignInClient(GoogleSignInClient mGoogleSignInClient){
        this.mGoogleSignInClient=mGoogleSignInClient;
    }
    //RC_SIGN_IN 의 getter
    public int getRcSignIn(){
        return RC_SIGN_IN;
    }
    //User반환하는 getter
    public FirebaseUser getUser(){
        return user;
    }
    //mAuth반환하는 getter/setter
    public FirebaseAuth getmAuth(){
        return mAuth;
    }
    public void setmAuth(FirebaseAuth mAuth){
        this.mAuth=mAuth;
    }
    //클래스의 생성자 - 로그인된 정보를 받아옴
    public AboutLogin(){
        user = FirebaseAuth.getInstance().getCurrentUser(); //로그인된 정보 받아옴
        mAuth = FirebaseAuth.getInstance();
    }
    //로그인된 정보가 있는지(user가 null이 아닌지) 판단하여 boolean 리턴 - null이면 false, 아니면 true
    public boolean checklogin(){
        if(user!=null)
        {
            return true;
        }else
        {
            return false;
        }
    }
}
