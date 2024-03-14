package com.example.reallifeproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reallifeproject.R;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class EventDialog extends DialogFragment {
    private Random random = new Random();
    private Button btn1, btn2, btn3;
    private TextView randomEventTxt;
    private ImageView randomEventPic;
    private int day, heart, stress, strength, smart, attack, magic, defense, agility, money;
    private String event;
    private String gender, scene;
    private int changeHeart, changeStress, changeStrength, changeSmart, changeAgility, changeAttack, changeMagic, changeDefense, changeMoney;
    private List<String> activities;
    private List<Double> probabilities;
    private List<Double> cumulativeProbabilities;
    private String selectedActivity;
    private EventDialogListener listener;
    private boolean isDead = false;
    private Map<String, Object> hashMap = new HashMap<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();

        gender = bundle.getString("gender");
        scene = bundle.getString("scene");
        heart = bundle.getInt("heart");
        stress = bundle.getInt("stress");
        strength = bundle.getInt("strength");
        smart = bundle.getInt("smart");
        attack = bundle.getInt("attack");
        magic = bundle.getInt("magic");
        defense = bundle.getInt("defense");
        agility = bundle.getInt("agility");
        money = bundle.getInt("money");
        day = bundle.getInt("day");
        event = bundle.getString("event");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_event, null);

        builder.setView(view);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        randomEventTxt = view.findViewById(R.id.randomEventTxt);
        randomEventPic = view.findViewById(R.id.randomEventPic);

        randomList(R.array.activities_outside, R.array.probabilities_outside);
//            selectedActivity = "You wander in the forest and are chased by a bear";
        event = selectedActivity;
        randomEventTxt.setText(selectedActivity);
        switch (selectedActivity) {
            case "Bạn đang lang thang và tìm thấy kho báu":
                randomEventPic.setImageResource(R.drawable.treasure_icon);
                chooseTreasure();
                break;
            case "Bạn đang đứng trước nơi cực kỳ đáng sợ, đầy rẫy quái vật ghê rợn, đó chính là dungeon":
                randomEventPic.setImageResource(R.drawable.dungeon_icon);
                chooseDungeon();
                break;
            case "Bạn gặp được một người dân tốt bụng ở ngôi làng gần đó nói sẽ tặng bạn thứ gì đó":
                randomEventPic.setImageResource(R.drawable.pocket_coins_icon);
                chooseSomethingFromVil();
                break;
            case "Bạn đang lang thang thì gặp phải gấu":
                randomEventPic.setImageResource(R.drawable.bear_icon);
                chooseChasedByBear();
                break;
            case "Bạn lạc vào rừng và trời thì sắp tối":
                randomEventPic.setImageResource(R.drawable.forest_icon);
                chooseLost();
                break;
            case "Bạn cảm thấy chân mình mất cảm giác và nhìn xuống thì nguyên một cái bẫy cắm vào chân bạn":
                randomEventPic.setImageResource(R.drawable.trap_icon);
                chooseTrap();
                break;
            case "Bạn dành cả ngày chỉ để hít đất":
                randomEventPic.setImageResource(R.drawable.push_up_icon);
                choosePushUp();
                break;
            case "Bạn dành cả ngày chỉ để chạy":
                randomEventPic.setImageResource(R.drawable.run_icon);
                chooseRunning();
                break;
            case "Bạn đang đi thám thính thì tìm thấy ngôi làng bỏ hoang gần đó":
                randomEventPic.setImageResource(R.drawable.village_icon);
                chooseAbandonedVillage();
                break;
            default:
                dismiss();
                break;
        }

        setCancelable(false);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EventDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context +
                    "must implement EventDialogListener");
        }
    }

    public interface EventDialogListener {
        void getAtt(Map<String, Object> att);
    }

    private void randomList(int acId, int probId) {
        activities = new ArrayList<>();
        probabilities = new ArrayList<>();

        String[] activityArray = getResources().getStringArray(acId);
        String[] probabilityArray = getResources().getStringArray(probId);
        if (activityArray.length == probabilityArray.length) {
            for (String activity : activityArray) {
                activities.add(activity);
            }
            for (String probability : probabilityArray) {
                double parsedProbability = Double.parseDouble(probability);
                probabilities.add(parsedProbability);
            }
        }

        calculateCumulativeProbabilities();

        generateRandomActivity();
    }

    private void calculateCumulativeProbabilities() {
        cumulativeProbabilities = new ArrayList<>();
        double totalProbability = 0;
        for (double probability : probabilities) {
            totalProbability += probability;
        }

        for (double probability : probabilities) {
            cumulativeProbabilities.add(probability / totalProbability);
        }

        for (int i = 1; i < cumulativeProbabilities.size(); i++) {
            cumulativeProbabilities.set(i, cumulativeProbabilities.get(i - 1) + cumulativeProbabilities.get(i));
        }
    }

    private void generateRandomActivity() {
        double randomValue = random.nextDouble();

        selectedActivity = null;
        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (randomValue <= cumulativeProbabilities.get(i)) {
                selectedActivity = activities.get(i);
                break;
            }
        }
        if (selectedActivity != null) {
            randomEventTxt.setText(selectedActivity);
        }
    }

    private void normalizeData() {
        if (heart > 100) {
            heart = 100;
        } else if (heart < 0) {
            heart = 0;
        }

        if (stress > 100) {
            stress = 100;
        } else if (stress < 0) {
            stress = 0;
        }

        if (strength > 100) {
            strength = 100;
        } else if (strength < 0) {
            strength = 0;
        }

        if (smart > 100) {
            smart = 100;
        } else if (smart < 0) {
            smart = 0;
        }


        if (money < 0) {
            money = 0;
        }
    }

    private void updateMap() {
        hashMap.put("day", day);
        hashMap.put("money", money);
        hashMap.put("stress", stress);
        hashMap.put("heart", heart);
        hashMap.put("strength", strength);
        hashMap.put("smart", smart);
        hashMap.put("attack", attack);
        hashMap.put("magic", magic);
        hashMap.put("defense", defense);
        hashMap.put("agility", agility);
        hashMap.put("event", event);
        hashMap.put("isDead", isDead);
    }

    private void chooseTreasure() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Mở ra");
        btn2.setText("Bỏ đi");
        btn1.setOnClickListener(v -> {
            int randomValue = random.nextInt(3);
            if (randomValue == 0) {
                changeMoney = 5000;
                changeStress = 5;
                money += changeMoney;
                stress += changeStress;
                event += "\nTừng núi vàng chất đống bên trong rương kho báu (+" + changeMoney + " đồng vàng) (+"+ changeStress + " căng thẳng)";
                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });

            } else if (randomValue == 1) {
                changeStress = 5;
                changeStrength = 3;
                changeSmart = 3;
                strength += changeStress;
                smart += changeSmart;
                stress += changeStress;
                event += "\nBạn đã nhận được ban phước từ các tinh linh trong hòm kho báu (+" + changeStrength  + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);
                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else {
                changeHeart = -10;
                changeStress = 10;
                heart += changeHeart;
                stress += changeStress;
                event += "\nĐột nhiên một con mimic xông ra và tấn công bạn ("  + changeHeart + " máu) (+" + changeStress + " căng thẳng)";
                normalizeData();

                if (heart == 0){
                    isDead = true;
                    event += "\nBạn đã chết";
                }

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            event += "\nBạn chọn bỏ qua kho báu và tiếp tục cuộc hành trình, có thể đó là quyết định đúng đắn";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseDungeon() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Chinh phạt");
        btn2.setText("Rời đi");
        btn1.setOnClickListener(v -> {
            // TODO: Entry Dungeon
            event += "\nBạn đã tiến vào dungeon";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            event += "\nBạn chưa chắc chắn vào thực lực lúc này và quyết định rời đi";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseSomethingFromVil() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Nhận");
        btn2.setText("Từ chối");
        btn1.setOnClickListener(v -> {
            int randomValue = random.nextInt(3);
            if (randomValue == 0) {
                int[] coins = {50, 60, 70, 80, 90, 100, 500};
                int randomCoin = coins[random.nextInt(coins.length)];
                money += randomCoin;
                event += "\nBạn ngạc nhiên với sấp vàng trên tay mình (+" + randomCoin + " đồng vàng)";
                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else if (randomValue == 1) {
                changeStrength = 5;
                changeSmart = 5;
                strength += changeStrength;
                smart += changeSmart;
                event += "\nDân làng ngỏ ý bạn tham gia vào một trại huấn luyện đặc biệt vì nhận thấy tiềm năng của bạn (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn)";
                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else {
                changeHeart = -10;
                changeStress = 10;
                changeMoney = -100;
                heart += changeHeart;
                money += changeMoney;
                stress += changeStress;
                event += "\nBạn đã bị lừa nhưng kịp thời nhận ra và trốn thoát (" + changeHeart + " máu) (" + changeMoney + " đồng vàng) + (+" + changeStress + " căng thẳng)";
                normalizeData();

                if (heart == 0){
                    isDead = true;
                    event += "\nyou're dead";
                }

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            event += "\nBạn đã từ chối lòng tốt của người dân, tuy nhiên ai biết được lòng người thế nào";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseChasedByBear() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Tấn công (100  máu) (10 tấn công) (10 phòng thủ)");
        btn2.setText("Chạy thoát (nhanh nhẹn >20)");
        if (agility <= 20) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            int atkBear = 10;
            int defBear = 10;
            int heartBear = 100;
            int damageReceived;
            int damageInflicted;
            int totalDamageReceived = 0;
            int randomValue = random.nextInt(2);
            boolean isEnd = false;
            while (!isEnd) {
                damageInflicted = attack - defBear;
                damageReceived = atkBear - defense;
                totalDamageReceived += damageReceived;
                if (randomValue == 0) {
                    if (damageInflicted > 0) {
                        heartBear -= damageInflicted;
                        if (heartBear <= 0) {
                            isEnd = true;
                            changeStrength = 5;
                            changeStress = 5;
                            changeSmart = 5;
                            changeMoney = 1000;
                            heart -= totalDamageReceived;
                            strength += 5;
                            smart += 5;
                            money += 1000;
                            stress += 5;
                            event += "\nBạn đã đánh bại con gấu (-" + totalDamageReceived + " máu) (+" + changeMoney + " đồng vàng) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";
                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);
                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                            continue;
                        }
                    }

                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            isEnd = true;
                            event += "\nYou're dead";
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                } else {
                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            isEnd = true;
                            event += "Bạn đã chết";
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                            continue;
                        }
                    }
                    if (damageInflicted > 0) {
                        heartBear -= damageInflicted;
                        if (heartBear <= 0) {
                            isEnd = true;
                            changeStrength = 5;
                            changeStress = 5;
                            changeSmart = 5;
                            changeMoney = 1000;
                            heart -= totalDamageReceived;
                            strength += 5;
                            smart += 5;
                            money += 1000;
                            stress += 5;
                            event += "\nBạn đã đánh bại con gấu (-" + totalDamageReceived + " máu) (+" + changeMoney + " đồng vàng) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }
        });
        btn2.setOnClickListener(v -> {
            event += "\nBạn đã chạy thoát thành công";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseLost() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        String[] enemies = {"goblin", "rabbit", "troll", "boar", "bat demon", "slime", "zombie", "villagers"};
        String enemy = enemies[random.nextInt(7)];
        switch (enemy) {
            case "goblin":
                    btn1.setText("Bạn đụng độ với goblin (25 máu) (7 tấn công) (2 phòng thủ)");
                break;
            case "rabbit":
                btn1.setText("Bạn đụng độ với rabbit (10 máu) (2 tấn công) (1 phòng thủ)");
                break;
            case "troll":
                btn1.setText("Bạn đụng độ với troll (50 máu) (10 tấn công) (10 phòng thủ)");
                break;
            case "boar":
                btn1.setText("You encounter a boar (25 máu) (7 tấn công) (5 phòng thủ)");
                break;
            case "bat demon":
                btn1.setText("Bạn đụng độ với bat demon (5 máu) (20 tấn công) (0 phòng thủ)");
                break;
            case "slime":
                btn1.setText("Bạn đụng độ với slime (100 máu) (2 tấn công) (2 phòng thủ)");
                break;
            case "zombie":
                btn1.setText("Bạn đụng độ với goblin (25 máu) (5 tấn công) (3 phòng thủ)");
                break;
            case "villagers":
                btn1.setText("Số bạn được độ khi được dân làng phát hiện và cứu giúp");
                btn2.setVisibility(View.GONE);
                break;
        }

        btn2.setText("Chạy khỏi (nhanh nhẹn >20");
        if (!(agility > 20)) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            int atkEnemy = 0;
            int defEnemy = 0;
            int heartEnemy = 0;
            switch (enemy) {
                case "goblin":
                    atkEnemy = 7;
                    defEnemy = 2;
                    heartEnemy = 25;
                    break;
                case "rabbit":
                    atkEnemy = 2;
                    defEnemy = 1;
                    heartEnemy = 10;
                    break;
                case "troll":
                    atkEnemy = 10;
                    defEnemy = 10;
                    heartEnemy = 50;
                    break;
                case "boar":
                    atkEnemy = 7;
                    defEnemy = 5;
                    heartEnemy = 25;
                    break;
                case "bat demon":
                    atkEnemy = 20;
                    defEnemy = 0;
                    heartEnemy = 5;
                    break;
                case "slime":
                    atkEnemy = 2;
                    defEnemy = 2;
                    heartEnemy = 100;
                    break;
                case "zombie":
                    atkEnemy = 5;
                    defEnemy = 3;
                    heartEnemy = 25;
                    break;
                case "villagers":
                    int randomValue = random.nextInt(2);
                    if (randomValue == 0) {
                        changeMoney = -100;
                        stress = 10;
                        money += changeMoney;
                        stress += stress;
                        event += "\nBạn đã thành công rời khỏi khu rừng u ám và gửi cho dân làng một ít tiền (" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                    } else {
                        changeMoney = -500;
                        changeHeart = -20;
                        changeStress = 20;
                        money += changeMoney;
                        heart += changeHeart;
                        stress += changeStress;
                        event += "\nBạn đã bị lừa và dân làng đã bỏ rơi bạn (" + changeHeart + " máu) (" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                    }

                    normalizeData();

                    if (heart == 0){
                        isDead = true;
                        event += "\nyou're dead";
                    }

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    dismiss();
                                }
                            });
                        }
                    });
                    break;
            }
            int damageReceived;
            int damageInflicted;
            int totalDamageReceived = 0;
            boolean isEnd = false;
            while (!isEnd) {
                int randomValue = random.nextInt(2);
                damageInflicted = attack - defEnemy;
                damageReceived = atkEnemy - defense;
                totalDamageReceived += damageReceived;
                if (randomValue == 0) {
                    if (damageInflicted > 0) {
                        heartEnemy -= damageInflicted;
                        if (heartEnemy <= 0) {
                            isEnd = true;
                            heart -= totalDamageReceived;
                            switch (enemy) {
                                case "goblin":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được goblin (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "rabbit":
                                    changeStress = 5;
                                    changeMoney = 100;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được rabbit (-" + totalDamageReceived + " máu) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "troll":
                                    changeStrength = 5;
                                    changeSmart = 5;
                                    changeStress = 5;
                                    changeMoney = 200;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được troll (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "boar":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được boar (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "bat demon":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được bat demon (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "slime":
                                    changeStress = 5;
                                    changeMoney = 1000;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được slime (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "zombie":
                                    changeStrength = 1;
                                    changeSmart = 1;
                                    changeStress = 10;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được zombie (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                            }

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                            continue;
                        }
                    }

                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            isEnd = true;
                            event += "\nBạn đã chết";
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                } else {
                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            isEnd = true;
                            event += "\nBạn đã chết";
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                            continue;
                        }
                    }
                    if (damageInflicted > 0) {
                        heartEnemy -= damageInflicted;
                        if (heartEnemy <= 0) {
                            isEnd = true;
                            heart -= totalDamageReceived;
                            switch (enemy) {
                                case "goblin":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được goblin (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "rabbit":
                                    changeStress = 5;
                                    changeMoney = 100;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được rabbit (-" + totalDamageReceived + " máu) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "troll":
                                    changeStrength = 5;
                                    changeSmart = 5;
                                    changeStress = 5;
                                    changeMoney = 200;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được troll (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "boar":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được boar (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "bat demon":
                                    changeStrength = 3;
                                    changeSmart = 3;
                                    changeStress = 5;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được bat demon (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "slime":
                                    changeStress = 5;
                                    changeMoney = 1000;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được slime (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                                case "zombie":
                                    changeStrength = 1;
                                    changeSmart = 1;
                                    changeStress = 10;
                                    changeMoney = 100;
                                    strength += changeStrength;
                                    smart += changeSmart;
                                    money += changeMoney;
                                    stress += changeStress;
                                    event += "\nBạn đã đánh bại được zombie (-" + totalDamageReceived + " máu) (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
                                    break;
                            }

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                            continue;
                        }
                    }

                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            isEnd = true;
                            event += "\nBạn đã chết";
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }
        });

        btn2.setOnClickListener(v -> {
            event += "\nBạn đã chạy thoát thành công";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseTrap() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);

        btn1.setText("Dùng lực để phá (sức mạnh > 50)");
        btn2.setText("Sử dụng minh mẫn để mở (minh mẫn  > 20) (nhanh nhẹn > 20)");
        btn3.setText("Hét to để cầu cứu");
        if (!(strength > 50)) btn1.setEnabled(false);
        if (!(smart > 20 && agility > 20)) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            changeHeart = -5;
            changeStrength = 5;
            changeStress = 5;
            heart += changeHeart;
            strength += changeStrength;
            stress += changeStress;
            event += "\nThành công thoát khỏi bẫy (" + changeHeart + " máu) (+" + changeStrength + " sức mạnh) (+" + changeStress + " căng thẳng)";
            normalizeData();

            if (heart == 0){
                isDead = true;
                event += "\nBạn đã chết";
            }

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });


        });
        btn2.setOnClickListener(v -> {
            changeHeart = -5;
            changeSmart = 2;
            changeAgility = 5;
            changeStress = 5;
            heart += changeHeart;
            smart += changeSmart;
            agility += changeAgility;
            stress += changeStress;
            event += "\nThành công thoát khỏi bẫy (" + changeHeart + " máu) (+" + changeSmart + " minh mẫn) (+" + changeAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";


            normalizeData();

            if (heart == 0){
                isDead = true;
                event += "\nyou're dead";
            }

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });

        btn3.setOnClickListener(v -> {
            int randomValue = random.nextInt(2);
            if (randomValue == 0) {
                changeHeart = -5;
                changeStress = 5;
                heart += changeHeart;
                stress += changeStress;
                event += "\nBạn đã may mắn nhân được sự trợ giúp từ người dân gần đó (" + changeHeart + " máu) (+"  + changeStress + " căng thẳng)";
            } else {
                changeHeart = -30;
                changeStress = 20;
                heart += changeHeart;
                stress += changeStress;
                event += "\nKhông một ai nghe thấy tiếng gọi vô vọng của bạn, chỉ còn lai bạn và bóng đêm cùng với chiếc bẫy ngày càng cắm sâu vào chân bạn (" + changeHeart + " máu) (+"  + changeStress + " căng thẳng)";
            }

            normalizeData();

            if (heart == 0){
                isDead = true;
                event += "\nyou're dead";
            }

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void choosePushUp() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);

        btn1.setText("Nhẹ nhàng");
        btn2.setText("Bình thường (sức mạnh >30");
        btn3.setText("Nhanh (sức mạnh >50");
        if(!(strength > 30)) btn2.setEnabled(false);
        if (!(strength > 50)) btn3.setEnabled(false);
        btn1.setOnClickListener(v -> {
            int randomValue = random.nextInt(3) + 3;
            strength += randomValue;
            changeStress = 3;
            stress += changeStress;
            event += "\n1000, 1001,... bạn hoàn thành bài tập với cơ thể cường tráng (+" + randomValue + " sức mạnh) (+" + changeStress + " căng thẳng)";
            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });


        });
        btn2.setOnClickListener(v -> {
            int randomValue = random.nextInt(3) + 5;
            strength += randomValue;
            changeStress = 5;
            stress += changeStress;
            event += "\n1000, 1001,... bạn hoàn thành bài tập với cơ thể cường tráng (+" + randomValue + " sức mạnh) (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });

        btn3.setOnClickListener(v -> {
            int randomValue = random.nextInt(6) + 5;
            strength += randomValue;
            changeStress = 10;
            changeHeart = 5;
            stress += changeStress;
            heart += changeHeart;
            event += "\n1000, 1001,... bạn hoàn thành bài tập, từng dòng cơ cuồn cuộn trên cơ thể bạn (+" + changeHeart + " máu) (+" + randomValue + " sức mạnh) (+" + changeStress + " căng thẳng)";
            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseRunning() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);

        btn1.setText("Nhẹ nhàng");
        btn2.setText("Bình thường (nhanh nhẹn >30)");
        btn3.setText("Nhanh (nhanh nhẹn >50)");

        if(!(agility > 30)) btn2.setEnabled(false);
        if (!(agility > 50)) btn3.setEnabled(false);

        btn1.setOnClickListener(v -> {


            int randomAgility = random.nextInt(3) + 1;
            agility += randomAgility;
            int randomStrength = random.nextInt(3) + 1;
            strength += randomStrength;
            changeStress = 3;
            stress += changeStress;
            event += "\nHoàn thành quãng đường 1000km xuyên rừng mệt lã (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";
            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });


        });
        btn2.setOnClickListener(v -> {
            int randomAgility = random.nextInt(3) + 3;
            agility += randomAgility;
            int randomStrength = random.nextInt(3) + 3;
            strength += randomStrength;
            changeStress = 5;
            stress += changeStress;
            event += "\nHoàn thành quãng đường 1000km xuyên rừng mệt lã (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });

        btn3.setOnClickListener(v -> {
            int randomAgility = random.nextInt(6) + 3;
            agility += randomAgility;
            int randomStrength = random.nextInt(6) + 3;
            strength += randomStrength;
            changeStress = 10;
            changeHeart = 5;
            heart += changeHeart;
            stress += changeStress;
            event += "\nHoàn thành quãng đường 1000km xuyên rừng mệt lã (+" + changeHeart + " máu) (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseAbandonedVillage() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Khám phá");
        btn2.setText("Rời khỏi");


        btn1.setOnClickListener(v -> {
            int randomValue = random.nextInt(3) ;
            if (randomValue == 0){
                changeHeart = 10;
                changeMoney = 500;
                changeStress = -10;
                money += changeMoney;
                heart += changeHeart;
                stress += changeStress;
                event += "\nBạn đã tìm được chỗ dựng trại và kho báu ở một cái chồi trong làng (+" + changeHeart + " máu) (+" + changeMoney + " đồng vàng) (" + changeStress + " căng thẳng)";
                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else if (randomValue == 1) {
                changeHeart = -10;
                changeStress = 10;
                heart += changeHeart;
                stress += changeStress;
                event += "\nBạn đã bị tấn công bởi một con nhện độc (" + changeHeart + " máu) (+" + changeStress + " căng thẳng)";
                normalizeData();

                if (heart == 0){
                    isDead = true;
                    event += "\nBạn đã chết";
                }

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else {
                event += "\nBạn đã gặp được thương nhân";
                // TODO: Trader

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            }


        });
        btn2.setOnClickListener(v -> {
            event += "\nBạn đã quyết định rời khỏi ngôi làng, không có gì là ngon ăn cả";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });

    }

    private void chooseCaptured() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Explore");
        btn2.setText("Leave");


        btn1.setOnClickListener(v -> {
            int randomValue = random.nextInt(3) ;
            if (randomValue == 0){
                event = "You have found treasure in the village and the campfire";

                money += 500;
                heart += 10;
                stress -= 10;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else if (randomValue == 1) {
                event = "You are attacked by poisonous spiders";

                heart -= 10;
                stress += 10;

                normalizeData();

                if (heart == 0){
                    isDead = true;
                    event += "\nyou're dead";
                }

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            } else {
                event = "You meet a trader";
                // TODO: Trader

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                dismiss();
                            }
                        });
                    }
                });
            }


        });
        btn2.setOnClickListener(v -> {
            event = "You have made the choice to leave the village";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dismiss();
                        }
                    });
                }
            });
        });

    }
}
