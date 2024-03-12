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
    private String gender, scene;
    private List<String> activities;
    private List<Double> probabilities;
    private List<Double> cumulativeProbabilities;
    private String selectedActivity;
    private EventDialogListener listener;
    private Map<String, Object> hashMap = new HashMap<>();
    private static final String TAG = "EventDialog";

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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_event, null);

        builder.setView(view);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        randomEventTxt = view.findViewById(R.id.randomEventTxt);
        randomEventPic = view.findViewById(R.id.randomEventPic);


        if (1 <= day) {
            randomList(R.array.activities_outside, R.array.probabilities_outside);
//            selectedActivity = "You are lost in the forest";
            randomEventTxt.setText(selectedActivity);
            switch (selectedActivity) {
                case "You found a treasure":
                    randomEventPic.setImageResource(R.drawable.treasure_icon);
                    chooseTreasure();
                    break;
                case "You found a dungeon":
                    randomEventPic.setImageResource(R.drawable.dungeon_icon);
                    chooseDungeon();
                    break;
                case "You were given something by the nearby villagers":
                    randomEventPic.setImageResource(R.drawable.pocket_coins_icon);
                    chooseSomethingFromVil();
                    break;
                case "You wander in the forest and are chased by a bear":
                    randomEventPic.setImageResource(R.drawable.bear_icon);
                    chooseChasedByBear();
                    break;
                case "You are lost in the forest":
                    randomEventPic.setImageResource(R.drawable.forest_icon);
                    chooseLost();
                    break;
                case "You step into a trap":
                    randomEventPic.setImageResource(R.drawable.trap_icon);
                    chooseTrap();
                    break;
                default:
                    dismiss();
                    break;
            }

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

    private void updateMap(){
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
    }

    private void chooseTreasure() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Take");
        btn2.setText("Leave");
        btn1.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            int randomValue = random.nextInt(3);
            if (randomValue == 0) {
                randomEventTxt.setText("You receive a lot of gold coin");
                randomEventPic.setVisibility(View.GONE);
                money += 5000;
                stress += 5;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });

            } else if (randomValue == 1) {
                randomEventTxt.setText("You receive a blessing");

                strength += 3;
                smart += 3;
                stress += 5;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);
                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            } else {
                randomEventTxt.setText("You are attacked by a mimic");

                heart -= 10;
                stress += 10;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
    }

    private void chooseDungeon() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Entry");
        btn2.setText("Leave");
        btn1.setOnClickListener(v -> {
            // TODO: Entry Dungeon
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventTxt.setText("You are entering the dungeon");
            randomEventPic.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
    }

    private void chooseSomethingFromVil() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Take");
        btn2.setText("Refuse");
        btn1.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            int randomValue = random.nextInt(3);
            if (randomValue == 0) {
                randomEventTxt.setText("You receive a lot of gold coin");
                int[] coins = {50, 60, 70, 80, 90, 100, 500};
                int randomCoin = coins[random.nextInt(coins.length)];

                money += randomCoin;

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            } else if (randomValue == 1) {
                randomEventTxt.setText("You receive a special training key from villagers");
                strength += 5;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            } else {
                randomEventTxt.setText("You are kidnapped by villagers, but luckily manage to escape");
                heart -= 10;
                money -= 100;
                stress += 10;

                normalizeData();

                updateMap();

                listener.getAtt(hashMap);

                FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
    }

    private void chooseChasedByBear() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);

        btn1.setText("Attack it (100HP) (10ATK) (10DEF)");
        btn2.setText("Escape (AGI >20)");
        if (agility <= 20) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            boolean isDead = false;
            int atkBear = 10;
            int defBear = 10;
            int heartBear = 100;
            int damageReceived;
            int damageInflicted;
            int totalDamageReceived = 0;
            while (!isDead) {
                int randomValue = random.nextInt(2);
                damageInflicted = attack - defBear;
                damageReceived = atkBear - defense;
                totalDamageReceived += damageReceived;
                if (randomValue == 0) {
                    if (damageInflicted > 0) {
                        heartBear -= damageInflicted;
                        if (heartBear <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You defeated the bear");
                            heart -= totalDamageReceived;
                            strength += 5;
                            smart += 5;
                            money += 1000;
                            stress += 5;

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);
                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                            continue;
                        }
                    }

                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You're dead");
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                        }
                    }
                } else {
                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You're dead");
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                            continue;
                        }
                    }
                    if (damageInflicted > 0) {
                        heartBear -= damageInflicted;
                        if (heartBear <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You defeated the bear");
                            heart -= totalDamageReceived;
                            strength += 5;
                            smart += 5;
                            money += 1000;
                            stress += 5;

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                        }
                    }
                }
            }
        });
        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
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
                btn1.setText("You encounter a goblin (25HP) (5ATK) (2DEF)");
                break;
            case "rabbit":
                btn1.setText("You encounter a rabbit (10HP) (2ATK) (1DEF)");
                break;
            case "troll":
                btn1.setText("You encounter a troll (50HP) (10ATK) (10DEF)");
                break;
            case "boar":
                btn1.setText("You encounter a boar (25HP) (3ATK) (5DEF)");
                break;
            case "bat demon":
                btn1.setText("You encounter a bat demon (5HP) (20ATK) (0DEF)");
                break;
            case "slime":
                btn1.setText("You encounter a slime (25HP) (1ATK) (20DEF)");
                break;
            case "zombie":
                btn1.setText("You encounter a goblin (25HP) (3ATK) (3DEF)");
                break;
            case "villagers":
                btn1.setText("Villagers help you get out of the forest");
                btn2.setVisibility(View.GONE);
                break;
        }

        btn2.setText("Leave (AGI >20");
        if (!(agility > 20)) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            boolean isDead = false;
            int atkEnemy = 0;
            int defEnemy = 0;
            int heartEnemy = 0;
            switch (enemy) {
                case "goblin":
                    atkEnemy = 5;
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
                    atkEnemy = 3;
                    defEnemy = 5;
                    heartEnemy = 25;
                    break;
                case "bat demon":
                    atkEnemy = 20;
                    defEnemy = 0;
                    heartEnemy = 5;
                    break;
                case "slime":
                    atkEnemy = 1;
                    defEnemy = 20;
                    heartEnemy = 25;
                    break;
                case "zombie":
                    atkEnemy = 3;
                    defEnemy = 3;
                    heartEnemy = 25;
                    break;
                case "villagers":
                    int randomValue = random.nextInt(2);
                    if (randomValue == 0) {
                        randomEventTxt.setText("You have safely escaped the forest");
                        money -= 100;
                        stress += 10;
                    } else {
                        randomEventTxt.setText("You have been tricked and abandoned");
                        money -= 500;
                        heart -= 20;
                        stress += 20;
                    }

                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dismiss();
                        }
                    });
                    break;
            }
            int damageReceived;
            int damageInflicted;
            int totalDamageReceived = 0;
            while (!isDead) {
                int randomValue = random.nextInt(2);
                damageInflicted = attack - defEnemy;
                damageReceived = atkEnemy - defense;
                totalDamageReceived += damageReceived;
                if (randomValue == 0) {
                    if (damageInflicted > 0) {
                        heartEnemy -= damageInflicted;
                        if (heartEnemy <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You defeated the enemy");
                            heart -= totalDamageReceived;
                            switch (enemy) {
                                case "goblin":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "rabbit":
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "troll":
                                    strength += 5;
                                    smart += 5;
                                    money += 200;
                                    stress += 5;
                                    break;
                                case "boar":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "bat demon":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "slime":
                                    money += 1000;
                                    stress += 5;
                                    break;
                                case "zombie":
                                    strength += 1;
                                    smart += 1;
                                    money += 100;
                                    stress += 10;
                                    break;
                            }

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                            continue;
                        }
                    }

                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You're dead");
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                        }
                    }
                } else {
                    if (damageReceived > 0) {
                        heart -= damageReceived;
                        if (heart <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You're dead");
                            heart = 0;

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                            continue;
                        }
                    }
                    if (damageInflicted > 0) {
                        heartEnemy -= damageInflicted;
                        if (heartEnemy <= 0) {
                            isDead = true;
                            randomEventTxt.setText("You defeated the enemy");
                            heart -= totalDamageReceived;
                            switch (enemy) {
                                case "goblin":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "rabbit":
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "troll":
                                    strength += 5;
                                    smart += 5;
                                    money += 200;
                                    stress += 5;
                                    break;
                                case "boar":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "bat demon":
                                    strength += 3;
                                    smart += 3;
                                    money += 100;
                                    stress += 5;
                                    break;
                                case "slime":
                                    money += 1000;
                                    stress += 5;
                                    break;
                                case "zombie":
                                    strength += 1;
                                    smart += 1;
                                    money += 100;
                                    stress += 10;
                                    break;
                            }

                            normalizeData();

                            updateMap();

                            listener.getAtt(hashMap);

                            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                        }
                    }
                }
            }
        });

        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
    }

    private void chooseTrap() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);

        btn1.setText("Breaking (STR > 50)");
        btn2.setText("Open the trap (INT > 20) (AGI > 20)");
        btn3.setText("Ask for help");
        if (!(strength > 50)) btn1.setEnabled(false);
        if (!(smart > 20 && agility > 20)) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            randomEventTxt.setText("Successfully breaking the trap");
            heart -= 5;
            strength += 3;
            stress += 5;

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });


        });
        btn2.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            randomEventTxt.setText("Successfully open the trap");
            heart -= 5;
            smart += 2;
            agility += 2;
            stress += 5;

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });

        btn3.setOnClickListener(v -> {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            int randomValue = random.nextInt(2);
            if (randomValue == 0) {
                randomEventTxt.setText("You are fortunate to be rescued by nearby villagers");
                heart -= 5;
                stress += 5;
            } else {
                randomEventTxt.setText("No one helps you, and you have to struggle to escape the trap on your own");
                heart -= 30;
                stress += 20;
            }

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                }
            });
        });
    }
}
