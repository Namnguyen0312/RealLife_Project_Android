package com.example.reallifeproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reallifeproject.model.DayModel;
import com.example.reallifeproject.model.InventoryModel;
import com.example.reallifeproject.model.ItemModel;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


public class WaitingActivity extends AppCompatActivity {
    private Map<String, Object> hashMap = new HashMap<>();
    private PlayerModel playerModel;
    private InventoryModel inventoryModel;
    private ItemModel sword = new ItemModel();
    private ItemModel shield = new ItemModel();
    private ItemModel magicWand = new ItemModel();

    private static final String TAG = "WaitingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        String activity = getIntent().getStringExtra("to_activity");
        if (activity.equals("InGameActivity")){
            FirebaseUtil.getPlayerModelReferenceWithId().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    playerModel = task.getResult().toObject(PlayerModel.class);
                    Intent intent = new Intent(this, InGameActivity.class);
                    AndroidUtil.passPlayerModelAsIntent(intent, playerModel);
                    startActivity(intent);
                }
            });
        } else if (activity.equals("ShopActivity")) {
            FirebaseUtil.getPlayerModelReferenceWithId().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    playerModel = task.getResult().toObject(PlayerModel.class);
                    Intent intent = new Intent(this, ShopActivity.class);
                    AndroidUtil.passPlayerModelAsIntent(intent, playerModel);
                    startActivity(intent);
                }
            });
        } else if (activity.equals("ShopActivityToInGameActivity")) {
            playerModel = AndroidUtil.getPlayerModelFromIntent(getIntent());

            FirebaseUtil.getDayModelReference().get().addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                       DayModel dayModel = queryDocumentSnapshot.toObject(DayModel.class);
                       if (dayModel.getDay() == playerModel.getDay()){
                           updateMap();
                           FirebaseUtil.getDayModelReference().document(queryDocumentSnapshot.getId()).update(hashMap).addOnCompleteListener(task1 -> {
                               if (task1.isSuccessful()){
                                   Intent intent = new Intent(this, InGameActivity.class);
                                   AndroidUtil.passPlayerModelAsIntent(intent, playerModel);
                                   startActivity(intent);
                               }
                           });
                       }
                   }
               }
            });
        } else if (activity.equals("InGameToInventoryActivity")) {
            FirebaseUtil.getItemModelReference()
                    .whereEqualTo("isBuy", true)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            if (!(task.getResult().isEmpty())){
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if (queryDocumentSnapshot.getString("kind").equals("Kiếm")){
                                        sword = queryDocumentSnapshot.toObject(ItemModel.class);
                                    } else if (queryDocumentSnapshot.getString("kind").equals("Khiên")) {
                                        shield = queryDocumentSnapshot.toObject(ItemModel.class);
                                    } else if (queryDocumentSnapshot.getString("kind").equals("Gậy phép")) {
                                        magicWand = queryDocumentSnapshot.toObject(ItemModel.class);
                                    }
                                }
                                navigateToInventoryActivity();
                            }
                            else {
                                Log.d(TAG, "onCreate: khong co item nao");
                                navigateToInventoryActivity();
                            }
                        }
                    });
        }
    }

    private void updateMap() {
        hashMap.put("money", playerModel.getMoney());
        hashMap.put("attack", playerModel.getAttack());
        hashMap.put("magic", playerModel.getMagic());
        hashMap.put("defense", playerModel.getDefense());
        hashMap.put("resistance", playerModel.getResistance());
        hashMap.put("agility", playerModel.getAgility());
        hashMap.put("event", playerModel.getEvent());
    }

    private void navigateToInventoryActivity() {
        Intent intent = new Intent(this, InventoryActivity.class);
        intent.putExtra("swordModel", sword);
        intent.putExtra("shieldModel", shield);
        intent.putExtra("magicWandModel", magicWand);
        startActivity(intent);
    }

}