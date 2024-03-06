package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reallifeproject.model.UserModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEdtTxt, passwordEdtTxt, confirmPasswordEdtTxt;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private UserModel userModel;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        setInProgress(false);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> registerUser());


    }

    private void registerUser(){

        setInProgress(true);

        String email = emailEdtTxt.getText().toString();
        String password = passwordEdtTxt.getText().toString();
        String confirmPassword = confirmPasswordEdtTxt.getText().toString();

        if(email.isEmpty()){
            AndroidUtil.showToast(this, "Email can't empty");
            setInProgress(false);
            return;
        }

        if(password.isEmpty()){
            AndroidUtil.showToast(this, "Password can't empty");
            setInProgress(false);
            return;
        }

        if(confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            AndroidUtil.showToast(this, "Confirm incorrect password");
            setInProgress(false);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, confirmPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "Email verification sent.");


                                        saveUserToFirestore(email, confirmPassword,user.getUid());
                                    } else {
                                        Log.e(TAG, "Failed to send verification email.", task1.getException());
                                        setInProgress(false);
                                    }
                                });

                        mAuth.signOut();
                    } else {
                        AndroidUtil.showToast(this, "Registration failed");
                        navigateMainActivity();
                        setInProgress(false);
                    }
                });


    }

    private void saveUserToFirestore(String email, String confirmPassword, String usrId) {

        userModel = new UserModel(email, confirmPassword, Timestamp.now(), usrId);

        FirebaseFirestore.getInstance().collection("users").document(usrId)
                .set(userModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        setInProgress(false);
                        navigateMainActivity();
                    }else {
                        Log.e(TAG, "Error writing document", task.getException());
                        setInProgress(false);
                    }
                });
    }

    private void navigateMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            registerBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initView(){
        emailEdtTxt = findViewById(R.id.emailEdtTxt);
        passwordEdtTxt = findViewById(R.id.passwordEdtTxt);
        confirmPasswordEdtTxt = findViewById(R.id.confirmPasswordEdtTxt);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.registerProgressBar);
    }
}