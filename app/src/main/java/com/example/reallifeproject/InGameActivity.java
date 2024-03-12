package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reallifeproject.dialog.EventDialog;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class InGameActivity extends AppCompatActivity implements EventDialog.EventDialogListener {
    private TextView moneyTxt, dayTxt, eventTxt, genderTxt, sceneTxt, atkTxt, magicTxt, defTxt, agiTxt;
    private TextView valueHeart, valueStress, valueStrength, valueSmart;
    //    private Button eventBtn1, eventBtn2, eventBtn3;
    private RelativeLayout eventLayout;
    private ImageView eventImg;
    private ProgressBar heartProgress, stressProgress, strengthProgress, smartProgress;
    private ProgressBar loadProgress;
    private PlayerModel playerModel;
    private int day = 0;
    private String event, gender, scene;
    private int heart, stress, strength, smart, money, attack, magic, defense, agility;
    private int countClick = 0;
    private static final String TAG = "InGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        initView();

        playerModel = AndroidUtil.getPlayerModelFromIntent(getIntent());

        loadProgress.setVisibility(View.GONE);

        loadGame();
//        eventLayout.setOnClickListener(v -> {
//            countClick++;
//
//            if (day == 0){
//                day++;
//                dayTxt.setText(String.valueOf(day));
//
//            }
//
//            if (countClick % 2 == 1){
//                if(day >= 1) {
//                    eventImg.setImageResource(R.drawable.strength_icon);
//                    event = "You are training to fight evil";
//                    eventTxt.setText(event);
//                    openDialog();
//                }
//
//            }
//            else {
//                saveModel();
//                loadProgress.setVisibility(View.VISIBLE);
//                FirebaseUtil.getPlayerModelReferenceWithId().set(playerModel).addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        loadProgress.setVisibility(View.GONE);
//                        day++;
//                        dayTxt.setText(String.valueOf(day));
//                    }
//                });
//            }
//
//
//        });

        eventLayout.setOnClickListener(v -> {
            if (day == 0) {
                day++;
                dayTxt.setText(String.valueOf(day));
                eventImg.setImageResource(R.drawable.strength_icon);
                event = "You are training to fight evil";
                eventTxt.setText(event);
            } else if (day >= 1) {
                day++;
                dayTxt.setText(String.valueOf(day));
                openDialog();
            }

        });

    }


    private void openDialog() {
        Bundle bundle = new Bundle();

        bundle.putString("gender", gender);
        bundle.putString("scene", scene);
        bundle.putInt("heart", heart);
        bundle.putInt("stress", stress);
        bundle.putInt("strength", strength);
        bundle.putInt("smart", smart);
        bundle.putInt("attack", attack);
        bundle.putInt("magic", magic);
        bundle.putInt("defense", defense);
        bundle.putInt("agility", agility);
        bundle.putInt("money", money);
        bundle.putInt("day", day);

        EventDialog eventDialog = new EventDialog();

        eventDialog.setArguments(bundle);
        eventDialog.show(getSupportFragmentManager(), "event dialog");

    }


    private void loadGame() {
        heart = playerModel.getHeart();
        stress = playerModel.getStress();
        strength = playerModel.getStrength();
        smart = playerModel.getSmart();
        money = playerModel.getMoney();
        day = playerModel.getDay();
        event = playerModel.getEvent();
        gender = playerModel.getGender();
        scene = playerModel.getScene();
        attack = playerModel.getAttack();
        magic = playerModel.getMagic();
        defense = playerModel.getDefense();
        agility = playerModel.getAgility();

        if (day >= 1) {
            eventImg.setImageResource(R.drawable.strength_icon);
        }


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
        dayTxt.setText(String.valueOf(day));
        atkTxt.setText(String.valueOf(attack));
        magicTxt.setText(String.valueOf(magic));
        defTxt.setText(String.valueOf(defense));
        agiTxt.setText(String.valueOf(agility));
    }

    private void initView() {
        eventLayout = findViewById(R.id.eventLayout);
        moneyTxt = findViewById(R.id.moneyTxt);
        dayTxt = findViewById(R.id.dayTxt);
        eventTxt = findViewById(R.id.eventTxt);
        genderTxt = findViewById(R.id.genderTxt);
        sceneTxt = findViewById(R.id.sceneTxt);
        valueHeart = findViewById(R.id.valueHeart);
        valueStress = findViewById(R.id.valueStress);
        valueStrength = findViewById(R.id.valueStrength);
        valueSmart = findViewById(R.id.valueSmart);
        atkTxt = findViewById(R.id.atkTxt);
        magicTxt = findViewById(R.id.magicTxt);
        defTxt = findViewById(R.id.defTxt);
        agiTxt = findViewById(R.id.agiTxt);

//        eventBtn1 = findViewById(R.id.eventBtn1);
//        eventBtn2 = findViewById(R.id.eventBtn2);
//        eventBtn3 = findViewById(R.id.eventBtn3);
        loadProgress = findViewById(R.id.loadProgress);
        heartProgress = findViewById(R.id.heartProgress);
        stressProgress = findViewById(R.id.stressProgress);
        strengthProgress = findViewById(R.id.strengthProgress);
        smartProgress = findViewById(R.id.smartProgress);
        eventImg = findViewById(R.id.eventImg);
    }

    @Override
    public void getAtt(Map<String, Object> att) {
        if (att.containsKey("heart")) {
            heart = (int) att.get("heart");
            if (heart > 100) {
                heart = 100;
            } else if (heart < 0) {
                heart = 0;
            }
            heartProgress.setProgress(heart);
            valueHeart.setText(String.valueOf(heart));
        }
        if (att.containsKey("stress")) {
            stress = (int) att.get("stress");
            if (stress > 100) {
                stress = 100;
            } else if (stress < 0) {
                stress = 0;
            }
            stressProgress.setProgress(stress);
            valueStress.setText(String.valueOf(stress));
        }
        if (att.containsKey("strength")) {
            strength = (int) att.get("strength");
            if (strength > 100) {
                strength = 100;
            } else if (strength < 0) {
                strength = 0;
            }
            strengthProgress.setProgress(strength);
            valueStrength.setText(String.valueOf(strength));
        }
        if (att.containsKey("smart")) {
            smart = (int) att.get("smart");
            if (smart > 100) {
                smart = 100;
            } else if (smart < 0) {
                smart = 0;
            }
            smartProgress.setProgress(smart);
            valueSmart.setText(String.valueOf(smart));
        }
        if (att.containsKey("money")) {
            money = (int) att.get("money");
            if (money < 0) {
                money = 0;
            }
            moneyTxt.setText(String.valueOf(money));
        }
    }


}