package com.example.reallifeproject.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.reallifeproject.model.InventoryModel;
import com.example.reallifeproject.model.ItemModel;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.model.UserModel;

import java.util.ArrayList;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void passPlayerModelAsIntent(Intent intent, PlayerModel model){
        intent.putExtra("gender", model.getGender());
        intent.putExtra("age", model.getDay());
        intent.putExtra("event", model.getEvent());
        intent.putExtra("money", model.getMoney());
        intent.putExtra("playerId", model.getPlayerId());
        intent.putExtra("heart", model.getHeart());
        intent.putExtra("stress", model.getStress());
        intent.putExtra("strength", model.getStrength());
        intent.putExtra("smart", model.getSmart());
        intent.putExtra("scene", model.getScene());
        intent.putExtra("attack", model.getAttack());
        intent.putExtra("magic", model.getMagic());
        intent.putExtra("defense", model.getDefense());
        intent.putExtra("resistance", model.getResistance());
        intent.putExtra("agility", model.getAgility());
        intent.putExtra("isDead", model.isDead());

    }

    public static PlayerModel getPlayerModelFromIntent(Intent intent){
        PlayerModel playerModel = new PlayerModel();
        playerModel.setPlayerId(intent.getStringExtra("playerId"));
        playerModel.setScene(intent.getStringExtra("scene"));
        playerModel.setGender(intent.getStringExtra("gender"));
        playerModel.setEvent(intent.getStringExtra("event"));
        playerModel.setMoney(intent.getIntExtra("money", 0));
        playerModel.setHeart(intent.getIntExtra("heart",0));
        playerModel.setStress(intent.getIntExtra("stress",0));
        playerModel.setStrength(intent.getIntExtra("strength",0));
        playerModel.setSmart(intent.getIntExtra("smart",0));
        playerModel.setDay(intent.getIntExtra("age",0));
        playerModel.setMagic(intent.getIntExtra("magic",0));
        playerModel.setAttack(intent.getIntExtra("attack",0));
        playerModel.setDefense(intent.getIntExtra("defense",0));
        playerModel.setResistance(intent.getIntExtra("resistance",0));
        playerModel.setAgility(intent.getIntExtra("agility",0));
        playerModel.setDead(intent.getBooleanExtra("isDead", false));
        return playerModel;
    }


}
