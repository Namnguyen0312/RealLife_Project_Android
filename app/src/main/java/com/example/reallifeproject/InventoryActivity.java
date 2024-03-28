package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reallifeproject.model.ItemModel;

public class InventoryActivity extends AppCompatActivity {
    private ImageView backBtn, swordPic, shieldPic, magicWandPic;
    private TextView nameSwordTxt, nameShieldTxt, nameMagicWandTxt, moneyTxt;
    private ItemModel swordModel, shieldModel, magicWandModel;
    private ProgressBar loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        initView();

        getItemModel();

        setUpView();

        loadProgress.setVisibility(View.GONE);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaitingActivity.class);
            intent.putExtra("to_activity", "InGameActivity");
            startActivity(intent);
        });
    }

    private void setUpView(){
        if (swordModel != null){
            swordPic.setImageResource(swordModel.getImageId());
            nameSwordTxt.setText(swordModel.getName());
        } else {
            nameSwordTxt.setText("Trống");
        }
        if (shieldModel != null){
            shieldPic.setImageResource(shieldModel.getImageId());
            nameShieldTxt.setText(shieldModel.getName());

        }else {
            nameShieldTxt.setText("Trống");
        }

        if (magicWandModel != null){
            magicWandPic.setImageResource(magicWandModel.getImageId());
            nameMagicWandTxt.setText(magicWandModel.getName());
        }else {
            nameMagicWandTxt.setText("Trống");
        }


    }

    private void getItemModel() {
        swordModel = getIntent().getParcelableExtra("swordModel");
        shieldModel = getIntent().getParcelableExtra("shieldModel");
        magicWandModel = getIntent().getParcelableExtra("magicWandModel");
    }

    private void initView(){
        backBtn = findViewById(R.id.backBtn);
        swordPic = findViewById(R.id.swordPic);
        shieldPic = findViewById(R.id.shieldPic);
        magicWandPic = findViewById(R.id.magicWandPic);
        nameSwordTxt = findViewById(R.id.nameSwordTxt);
        nameShieldTxt = findViewById(R.id.nameShieldTxt);
        nameMagicWandTxt = findViewById(R.id.nameMagicWandTxt);
        moneyTxt = findViewById(R.id.moneyTxt);
        loadProgress = findViewById(R.id.loadProgress);
    }
}