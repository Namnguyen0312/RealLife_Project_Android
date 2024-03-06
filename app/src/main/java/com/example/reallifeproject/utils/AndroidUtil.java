package com.example.reallifeproject.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.model.UserModel;

public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void passPlayerModelAsIntent(Intent intent, PlayerModel model){
        intent.putExtra("gender", model.getGender());
        intent.putExtra("age", model.getAge());
        intent.putExtra("event", model.getEvent());
        intent.putExtra("money", model.getMoney());
        intent.putExtra("playerId", model.getPlayerId());
        intent.putExtra("heart", model.getHeart());
        intent.putExtra("stress", model.getStress());
        intent.putExtra("strength", model.getStrength());
        intent.putExtra("smart", model.getSmart());
        intent.putExtra("scene", model.getScene());
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
        playerModel.setAge(intent.getIntExtra("age",0));
        return playerModel;
    }

}
