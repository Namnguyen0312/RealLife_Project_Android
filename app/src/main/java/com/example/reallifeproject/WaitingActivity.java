package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;


public class WaitingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);


        FirebaseUtil.getPlayerModelReferenceWithId().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                PlayerModel playerModel = task.getResult().toObject(PlayerModel.class);
                Intent intent = new Intent(this, InGameActivity.class);
                AndroidUtil.passPlayerModelAsIntent(intent, playerModel);
                startActivity(intent);
            }
        });
    }
}