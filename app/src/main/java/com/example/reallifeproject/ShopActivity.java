package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reallifeproject.adapter.DayRecViewAdapter;
import com.example.reallifeproject.adapter.ShopRecViewAdapter;
import com.example.reallifeproject.model.DayModel;
import com.example.reallifeproject.model.ItemModel;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ShopActivity extends AppCompatActivity {

    private TextView moneyTxt;
    private ImageView swordImg, shieldImg, magicImg;
    private ProgressBar loadProgress;
    private ImageView backBtn;
    private RecyclerView itemRecView;
    private ShopRecViewAdapter swordAdapter, armourAdapter, magicAdapter;
    private PlayerModel playerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        initView();

        playerModel = AndroidUtil.getPlayerModelFromIntent(getIntent());

        setupData();

        setUpSwordRecView();

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaitingActivity.class);
            AndroidUtil.passPlayerModelAsIntent(intent, playerModel);
            intent.putExtra("to_activity", "ShopActivityToInGameActivity");
            startActivity(intent);
        });

        swordImg.setOnClickListener(v -> {
            setUpSwordRecView();

        });

        shieldImg.setOnClickListener(v -> {
            setUpArmourRecView();
        });

        magicImg.setOnClickListener(v -> {
            setUpWandMagicRecView();
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupData(){
        moneyTxt.setText(String.valueOf(playerModel.getMoney()));
        loadProgress.setVisibility(View.GONE);
    }

    private void setUpSwordRecView(){
        Query query = FirebaseUtil.getItemModelReference()
                .whereEqualTo("kind", "Kiếm")
                .orderBy("money", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class).build();

        swordAdapter = new ShopRecViewAdapter(options, this, this);
        itemRecView.setLayoutManager(new GridLayoutManager(this, 2));
        itemRecView.setAdapter(swordAdapter);
        swordAdapter.startListening();
    }

    private void setUpArmourRecView(){
        Query query = FirebaseUtil.getItemModelReference()
                .whereEqualTo("kind", "Khiên")
                .orderBy("money", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class).build();

        armourAdapter = new ShopRecViewAdapter(options, this, this);
        itemRecView.setLayoutManager(new GridLayoutManager(this, 2));
        itemRecView.setAdapter(armourAdapter);
        armourAdapter.startListening();
    }
    private void setUpWandMagicRecView(){
        Query query = FirebaseUtil.getItemModelReference()
                .whereEqualTo("kind", "Gậy phép")
                .orderBy("money", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class).build();

        magicAdapter = new ShopRecViewAdapter(options, this, this);
        itemRecView.setLayoutManager(new GridLayoutManager(this, 2));
        itemRecView.setAdapter(magicAdapter);
        magicAdapter.startListening();
    }

    public void setMoney(int money){
        moneyTxt.setText(String.valueOf(money));
    }

    public PlayerModel getPlayerModel(){
        return playerModel;
    }

    private void initView(){
        moneyTxt = findViewById(R.id.moneyTxt);
        loadProgress = findViewById(R.id.loadProgress);
        backBtn = findViewById(R.id.backBtn);
        itemRecView = findViewById(R.id.itemRecView);
        swordImg = findViewById(R.id.swordImg);
        shieldImg = findViewById(R.id.shieldImg);
        magicImg = findViewById(R.id.magicImg);
    }


}