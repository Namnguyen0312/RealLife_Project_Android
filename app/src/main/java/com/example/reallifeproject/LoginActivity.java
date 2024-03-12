package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reallifeproject.utils.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdtTxt, passwordEdtTxt;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        setInProgress(false);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> loginUser());

    }

    private void loginUser(){
        setInProgress(true);

        String email = emailEdtTxt.getText().toString();
        String password = passwordEdtTxt.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            AndroidUtil.showToast(this, "Email and password can't empty");
            setInProgress(false);
            return;
        }
        else {
            confirmLogin(email, password);
        }

    }

    private void confirmLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                setInProgress(false);
                                Intent intent = new Intent(LoginActivity.this, TitleScreenActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                mAuth.signOut();
                                setInProgress(false);
                                AndroidUtil.showToast(this, "Please verify your email before logging in");
                            }
                        }
                    }else {
                        setInProgress(false);
                        AndroidUtil.showToast(this, "wrong email or password");
                    }
                });
    }

    private void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initView(){
        emailEdtTxt = findViewById(R.id.emailEdtTxt);
        passwordEdtTxt = findViewById(R.id.passwordEdtTxt);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.loginProgressBar);
    }
}