package com.example.reallifeproject.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reallifeproject.R;
import com.example.reallifeproject.ShopActivity;
import com.example.reallifeproject.model.ItemModel;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.type.Color;

import java.util.HashMap;
import java.util.Map;

public class ShopRecViewAdapter extends FirestoreRecyclerAdapter<ItemModel, ShopRecViewAdapter.ItemModelViewHolder> {

    private Context context;
    private ShopActivity shopActivity;
    private int changeAgility, changeAttack, changeMagic, changeDefense, changeResistance,changeMoney;
    private int day, heart, stress, strength, smart, attack, magic, defense, resistance,agility, money;
    private String event, gender;
    private PlayerModel playerModel;
    private Map<String, Object> haskMapItem = new HashMap<>();
    private Map<String, Object> hashMapInventory = new HashMap<>();
    private static final String TAG = "ShopRecViewAdapter";

    public ShopRecViewAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options, Context context, ShopActivity shopActivity) {
        super(options);
        this.context = context;
        this.shopActivity = shopActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemModelViewHolder holder, int position, @NonNull ItemModel model) {
        holder.moneyItemTxt.setText(String.valueOf(model.getMoney()));
        holder.nameItemTxt.setText(model.getName());
        holder.askedValueTxt.setText(model.getAsked());
        holder.magValueTxt.setText(String.valueOf(model.getMagic()));
        holder.defValueTxt.setText(String.valueOf(model.getDefense()));
        holder.resisValueTxt.setText(String.valueOf(model.getResistance()));
        holder.atkValueTxt.setText(String.valueOf(model.getAttack()));
        holder.agiValueTxt.setText(String.valueOf(model.getAgility()));
        holder.describeValueTxt.setText(model.getDescribe());
        holder.typeValueTxt.setText(model.getType());

        if (model.getCommon().equals("Thường")) {holder.nameItemTxt.setTextColor(ContextCompat.getColor(context, R.color.green));}
        else if (model.getCommon().equals("Hiếm")) {holder.nameItemTxt.setTextColor(ContextCompat.getColor(context, R.color.blue));}
        else if (model.getCommon().equals("Sử thi")) {holder.nameItemTxt.setTextColor(ContextCompat.getColor(context, R.color.purple));}
        else if (model.getCommon().equals("Huyền thoại")) {holder.nameItemTxt.setTextColor(ContextCompat.getColor(context, R.color.yellow));}


        holder.itemLayout.setVisibility(View.VISIBLE);
        holder.attLayout.setVisibility(View.GONE);
        holder.itemLayout.setOnClickListener(v -> {
            holder.itemLayout.setVisibility(View.GONE);
            holder.attLayout.setVisibility(View.VISIBLE);
        });
        holder.attLayout.setOnClickListener(v -> {
            holder.itemLayout.setVisibility(View.VISIBLE);
            holder.attLayout.setVisibility(View.GONE);
        });

        holder.itemPic.setImageResource(model.getImageId());

        playerModel = shopActivity.getPlayerModel();
        money = playerModel.getMoney();
        heart = playerModel.getHeart();
        stress = playerModel.getStress();
        strength = playerModel.getStrength();
        smart = playerModel.getSmart();
        attack = playerModel.getAttack();
        magic = playerModel.getMagic();
        defense = playerModel.getDefense();
        resistance = playerModel.getResistance();
        agility = playerModel.getAgility();
        day = playerModel.getDay();
        event = playerModel.getEvent();
        gender = playerModel.getGender();



        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
        boolean isBuy = snapshot.getBoolean("isBuy");
        Log.d(TAG, "onBindViewHolder: isbuy" + isBuy);
        if (isBuy) {
            holder.buyBtn.setEnabled(false);
        } else {
            holder.buyBtn.setEnabled(true);
        }

        holder.buyBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("bạn có chắc chắn muốn mua " + model.getName() + " không ?");
            changeMoney = money - model.getMoney();
            if (changeMoney < 0){
                builder.setPositiveButton("Mua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AndroidUtil.showToast(context, "Bạn không đủ tiền để mua " + model.getName() + " !");
                    }
                });
            }else if (!(model.getType().equals("Tất cả"))) {
                if (!(model.getType().equals(playerModel.getScene()))){
                    builder.setPositiveButton("Mua", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AndroidUtil.showToast(context, "Chức nghiệp không phù hợp");
                        }
                    });
                }else {
                    builder.setPositiveButton("Mua", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shopActivity.setMoney(changeMoney);
                            setPlayerModel(dialog, model, getSnapshots().getSnapshot(position).getId());
                        }
                    });
                }

            } else {
                builder.setPositiveButton("Mua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shopActivity.setMoney(changeMoney);
                        setPlayerModel(dialog, model, getSnapshots().getSnapshot(position).getId());
                    }
                });
            }

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        });
    }


    @NonNull
    @Override
    public ItemModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shop_rec_row, parent, false);
        return new ItemModelViewHolder(view);
    }

    private void setPlayerModel(DialogInterface dialog, ItemModel model, String itemId){
        event += "\nBạn vừa bỏ ra " + model.getMoney() + " đồng vàng để mua " + model.getName() + " (-(" + model.getMoney() +
                " đồng vàng)) (+(" + model.getAttack() + " tấn công)) (+(" + model.getDefense() + " giáp)) (+(" + model.getResistance() + " kháng phép)) (+(" + model.getMagic() + " phép thuật)) (+(" + model.getAgility() + " nhanh nhẹn))";
        changeAttack = model.getAttack() + attack;
        changeDefense = model.getDefense() + defense;
        changeMagic = model.getMagic() + magic;
        changeAgility = model.getAgility() + agility;
        changeResistance = model.getResistance() + resistance;
        playerModel.setMoney(changeMoney);
        playerModel.setAttack(changeAttack);
        playerModel.setDefense(changeDefense);
        playerModel.setResistance(changeResistance);
        playerModel.setMagic(changeMagic);
        playerModel.setAgility(changeAgility);
        playerModel.setEvent(event);
        Log.d(TAG, "setPlayerModel: id" + itemId);
        FirebaseUtil.getPlayerModelReferenceWithId().set(playerModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                haskMapItem.put("isBuy", true);
                FirebaseUtil.getItemModelReference().document(itemId).update(haskMapItem).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        AndroidUtil.showToast(context, "Bạn đã mua " + model.getName() + " thành công !");
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }else {
                        AndroidUtil.showToast(context, "Đã có lỗi xảy ra !");
                        Log.d(TAG, "setPlayerModel: " + task1.getException());
                    }
                });

            }
        });
    }

;
    public class ItemModelViewHolder extends RecyclerView.ViewHolder{

        private TextView moneyItemTxt, nameItemTxt, atkValueTxt, magValueTxt, defValueTxt, resisValueTxt, agiValueTxt, askedValueTxt, describeValueTxt, typeValueTxt;
        private ImageView itemPic;
        private RelativeLayout itemLayout;
        private LinearLayout attLayout;
        private Button buyBtn;

        public ItemModelViewHolder(@NonNull View itemView) {
            super(itemView);
            moneyItemTxt = itemView.findViewById(R.id.moneyItemTxt);
            nameItemTxt = itemView.findViewById(R.id.nameItemTxt);
            itemPic = itemView.findViewById(R.id.itemPic);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            buyBtn = itemView.findViewById(R.id.buyBtn);
            attLayout = itemView.findViewById(R.id.attLayout);
            atkValueTxt = itemView.findViewById(R.id.atkValueTxt);
            magValueTxt = itemView.findViewById(R.id.magValueTxt);
            defValueTxt = itemView.findViewById(R.id.defValueTxt);
            resisValueTxt = itemView.findViewById(R.id.resisValueTxt);
            agiValueTxt = itemView.findViewById(R.id.agiValueTxt);
            askedValueTxt = itemView.findViewById(R.id.askedValueTxt);
            describeValueTxt = itemView.findViewById(R.id.describeValueTxt);
            typeValueTxt = itemView.findViewById(R.id.typeValueTxt);
        }
    }
}
