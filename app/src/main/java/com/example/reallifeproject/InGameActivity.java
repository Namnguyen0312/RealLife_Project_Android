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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reallifeproject.adapter.DayRecViewAdapter;
import com.example.reallifeproject.dialog.EventDialog;
import com.example.reallifeproject.model.DayModel;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class InGameActivity extends AppCompatActivity implements EventDialog.EventDialogListener {
    private TextView moneyTxt, dayTxt, genderTxt, sceneTxt, atkTxt, magicTxt, defTxt, resisTxt,agiTxt;
    private TextView valueHeart, valueStress, valueStrength, valueSmart;
    private ImageView shopBtn, trainingBtn, skillBtn, inventoryBtn;
    private Button dayBtn;
    private ProgressBar heartProgress, stressProgress, strengthProgress, smartProgress;
    private ProgressBar loadProgress;
    private PlayerModel playerModel;
    private int day = 0;
    private String event, gender, scene;
    private int heart, stress, strength, smart, money, attack, magic, defense, resistance,agility;
    private boolean isDead = false;
    private RecyclerView dayRecView;
    private DayRecViewAdapter adapter;
    private String dialog;
    private static final String TAG = "InGameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

        initView();

        playerModel = AndroidUtil.getPlayerModelFromIntent(getIntent());

        loadProgress.setVisibility(View.GONE);

        loadGame();

        setupRecView();
        registerSnapshotListener();
        dayBtn.setOnClickListener(v -> {
            if (isDead){
                loadProgress.setVisibility(View.VISIBLE);
                FirebaseUtil.getPlayerModelReference().get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if (!task.getResult().isEmpty()){
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(playerModel.getPlayerId())) {
                                    document.getReference().delete();
                                }
                            }
                            loadProgress.setVisibility(View.GONE);
                            Intent intent = new Intent(InGameActivity.this, TitleScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        Log.d(TAG, "Error getting collection: ", task.getException());
                    }
                });
            }else {
                day++;
                dayTxt.setText(String.valueOf(day));
                dialog = "day";
                openDialog();

            }
        });


        trainingBtn.setOnClickListener(v -> {
            day++;
            dayTxt.setText(String.valueOf(day));
            dialog = "training";
            openDialog();
        });

        shopBtn.setOnClickListener(v -> {
            navigateToShopActivity();
        });

        skillBtn.setOnClickListener(v -> {
            // TODO: Skill Button
        });

        inventoryBtn.setOnClickListener(v -> {
            navigateToInventoryActivity();
        });

    }

    private void navigateToInventoryActivity(){
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra("to_activity", "InGameToInventoryActivity");
        startActivity(intent);
    }

    private void navigateToShopActivity(){
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra("to_activity", "ShopActivity");
        startActivity(intent);
    }

    private void registerSnapshotListener() {
        FirebaseUtil.getDayModelReference().addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                Log.e(TAG, "Listen failed", error);
                return;
            }

            if (snapshot != null && !snapshot.isEmpty()) {
                adapter.notifyDataSetChanged();
                dayRecView.smoothScrollToPosition(day+1);

            } else {
                Log.d(TAG, "No data");
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
        bundle.putInt("resistance", resistance);
        bundle.putInt("agility", agility);
        bundle.putInt("money", money);
        bundle.putInt("day", day);
        bundle.putString("event", event);
        bundle.putBoolean("isDead", isDead);
        bundle.putString("dialog", dialog);

        EventDialog eventDialog = new EventDialog();

        eventDialog.setArguments(bundle);
        eventDialog.show(getSupportFragmentManager(), "event dialog");

    }

    private  void setupRecView(){
        Query query = FirebaseUtil.getDayModelReference()
                .orderBy("day", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<DayModel> options = new FirestoreRecyclerOptions.Builder<DayModel>()
                .setQuery(query, DayModel.class).build();

        adapter = new DayRecViewAdapter(options, getApplicationContext());

        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setReverseLayout(true);
        dayRecView.setLayoutManager(manager);
        dayRecView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                dayRecView.smoothScrollToPosition(day+1);
            }
        });
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
        resistance = playerModel.getResistance();
        agility = playerModel.getAgility();
        isDead = playerModel.isDead();

        valueHeart.setText(String.valueOf(heart));
        valueStress.setText(String.valueOf(stress));
        valueStrength.setText(String.valueOf(strength));
        valueSmart.setText(String.valueOf(smart));
        heartProgress.setProgress(heart);
        stressProgress.setProgress(stress);
        strengthProgress.setProgress(strength);
        smartProgress.setProgress(smart);
        genderTxt.setText(gender);
        sceneTxt.setText(scene);
        moneyTxt.setText(String.valueOf(money));
        dayTxt.setText(String.valueOf(day));
        atkTxt.setText(String.valueOf(attack));
        magicTxt.setText(String.valueOf(magic));
        defTxt.setText(String.valueOf(defense));
        resisTxt.setText(String.valueOf(resistance));
        agiTxt.setText(String.valueOf(agility));
    }

    private void initView() {
        moneyTxt = findViewById(R.id.moneyTxt);
        dayTxt = findViewById(R.id.dayTxt);
        genderTxt = findViewById(R.id.genderTxt);
        sceneTxt = findViewById(R.id.sceneTxt);
        valueHeart = findViewById(R.id.valueHeart);
        valueStress = findViewById(R.id.valueStress);
        valueStrength = findViewById(R.id.valueStrength);
        valueSmart = findViewById(R.id.valueSmart);
        atkTxt = findViewById(R.id.atkTxt);
        magicTxt = findViewById(R.id.magicTxt);
        defTxt = findViewById(R.id.defTxt);
        resisTxt = findViewById(R.id.resisTxt);
        agiTxt = findViewById(R.id.agiTxt);
        dayRecView = findViewById(R.id.dayRecView);
        dayBtn = findViewById(R.id.dayBtn);
        shopBtn = findViewById(R.id.shopBtn);
        trainingBtn = findViewById(R.id.trainingBtn);
        skillBtn = findViewById(R.id.skillBtn);
        inventoryBtn = findViewById(R.id.inventoryBtn);
        loadProgress = findViewById(R.id.loadProgress);
        heartProgress = findViewById(R.id.heartProgress);
        stressProgress = findViewById(R.id.stressProgress);
        strengthProgress = findViewById(R.id.strengthProgress);
        smartProgress = findViewById(R.id.smartProgress);
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
            Log.d(TAG, "getAtt: smart " + smart);
        }

        if (att.containsKey("agility")) {
            agility = (int) att.get("agility");
             if (agility < 0) {
                smart = 0;
            }
            agiTxt.setText(String.valueOf(agility));
        }

        if (att.containsKey("money")) {
            money = (int) att.get("money");
            if (money < 0) {
                money = 0;
            }
            moneyTxt.setText(String.valueOf(money));
        }

        if (att.containsKey("isDead")){
            isDead = (boolean) att.get("isDead");
            if (isDead){
                shopBtn.setEnabled(false);
                skillBtn.setEnabled(false);
                trainingBtn.setEnabled(false);
                inventoryBtn.setEnabled(false);
                dayBtn.setText("Chơi mới");
            }
        }
    }


}