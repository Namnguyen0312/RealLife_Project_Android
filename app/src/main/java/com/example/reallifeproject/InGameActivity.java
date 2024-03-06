package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reallifeproject.dialog.EventDialog;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;


public class InGameActivity extends AppCompatActivity {
    private TextView moneyTxt, ageTxt, eventTxt, genderTxt, sceneTxt;
    private TextView valueHeart, valueStress, valueStrength, valueSmart;
//    private Button eventBtn1, eventBtn2, eventBtn3;
    private LinearLayout eventLayout;
    private ImageView eventImg;
    private ProgressBar heartProgress, stressProgress, strengthProgress, smartProgress;
    private PlayerModel playerModel;
    private int age = 0;
    private String event, gender, scene;
    private int heart, stress, strength, smart, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        initView();

        playerModel = AndroidUtil.getPlayerModelFromIntent(getIntent());

        loadGame();

        eventLayout.setOnClickListener(v -> {
            openDialog();
        });

    }

    private void openDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt("age", age);

        EventDialog eventDialog = new EventDialog();

        eventDialog.setArguments(bundle);
        eventDialog.show(getSupportFragmentManager(), "event dialog");

    }

    private void loadGame(){
        heart = playerModel.getHeart();
        stress = playerModel.getStress();
        strength = playerModel.getStrength();
        smart = playerModel.getSmart();
        money = playerModel.getMoney();
        age = playerModel.getAge();
        event = playerModel.getEvent();
        gender = playerModel.getGender();
        scene = playerModel.getScene();



//        eventImg.setImageBitmap(bitmap);

        valueHeart.setText(String.valueOf(heart));
        valueStress.setText(String.valueOf(stress));
        valueStrength.setText(String.valueOf(strength));
        valueSmart.setText(String.valueOf(smart));
        heartProgress.setProgress(heart);
        stressProgress.setProgress(stress);
        strengthProgress.setProgress(strength);
        smartProgress.setProgress(smart);
        eventTxt.setText(String.valueOf(event));
        genderTxt.setText(gender);
        sceneTxt.setText(scene);
        moneyTxt.setText(String.valueOf(money));
        ageTxt.setText(String.valueOf(age));
    }

    private void initView(){
        eventLayout = findViewById(R.id.eventLayout);
        moneyTxt = findViewById(R.id.moneyTxt);
        ageTxt = findViewById(R.id.ageTxt);
        eventTxt = findViewById(R.id.eventTxt);
        genderTxt = findViewById(R.id.genderTxt);
        sceneTxt = findViewById(R.id.sceneTxt);
        valueHeart = findViewById(R.id.valueHeart);
        valueStress = findViewById(R.id.valueStress);
        valueStrength = findViewById(R.id.valueStrength);
        valueSmart = findViewById(R.id.valueSmart);
//        eventBtn1 = findViewById(R.id.eventBtn1);
//        eventBtn2 = findViewById(R.id.eventBtn2);
//        eventBtn3 = findViewById(R.id.eventBtn3);
        heartProgress = findViewById(R.id.heartProgress);
        stressProgress = findViewById(R.id.stressProgress);
        strengthProgress = findViewById(R.id.strengthProgress);
        smartProgress = findViewById(R.id.smartProgress);
        eventImg = findViewById(R.id.eventImg);
    }
}