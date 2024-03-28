package com.example.reallifeproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reallifeproject.R;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class EventDialog extends DialogFragment {
    private Random random = new Random();
    private Button btn1, btn2, btn3, btn4, btn5, btn6;
    private TextView randomEventTxt;
    private ImageView randomEventPic;
    private ProgressBar loadProgress;
    private int day, heart, stress, strength, smart, attack, magic, defense, resistance,agility, money;
    private String event;
    private String gender, scene;
    private int changeHeart, changeStress, changeStrength, changeSmart, changeAgility, changeAttack, changeMagic, changeDefense, changeMoney;
//    private List<String> activities;
//    private List<Double> probabilities;
//    private List<Double> cumulativeProbabilities;
    private String selectedActivity = "";
    private EventDialogListener listener;
    private String dialog;
    private String[] activities;
    private String[] probabilities ;
    private String choose = "";
    private boolean isDead = false;
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
        resistance = bundle.getInt("resistance");
        agility = bundle.getInt("agility");
        money = bundle.getInt("money");
        day = bundle.getInt("day");
        event = bundle.getString("event");
        dialog = bundle.getString("dialog");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_event, null);

        builder.setView(view);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        randomEventTxt = view.findViewById(R.id.randomEventTxt);
        randomEventPic = view.findViewById(R.id.randomEventPic);
        activities = getResources().getStringArray(R.array.activities_outside);
        probabilities = getResources().getStringArray(R.array.probabilities_outside);
        loadProgress = view.findViewById(R.id.loadProgress);
        loadProgress.setVisibility(View.GONE);
        randomSelectedActivity();

        if (dialog.equals("day")){
//                        selectedActivity = "Bạn lạc vào rừng và trời thì sắp tối";
            event = selectedActivity;
            randomEventTxt.setText(selectedActivity);
            switch (selectedActivity) {
                case "":
                    randomEventPic.setVisibility(View.GONE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.GONE);
                    btn3.setVisibility(View.GONE);
                    btn4.setVisibility(View.GONE);
                    btn5.setVisibility(View.GONE);
                    btn6.setVisibility(View.GONE);
                    event = "Hôm nay là một ngày yên ắng, bạn thấy thời gian trôi qua thật mau";
                    randomEventTxt.setText(event);
                    btn1.setText("Tiếp");
                    btn1.setOnClickListener(v -> {
                        loadProgress.setVisibility(View.VISIBLE);

                        event += "\nGiữa đêm cảnh vật hữu tình, bạn nhớ lại những ngày còn thơ bé";

                        updateMap();

                        listener.getAtt(hashMap);

                        FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loadProgress.setVisibility(View.GONE);
                                        dismiss();
                                    }
                                });
                            }
                        });
                    });
                    break;
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
                case "Bạn tìm thấy ngôi làng bỏ hoang gần đó":
                    randomEventPic.setImageResource(R.drawable.village_icon);
                    chooseAbandonedVillage();
                    break;
                case "Bạn bị một nhóm cướp chặn đường":
                    randomEventPic.setImageResource(R.drawable.rogue_icon);
                    chooseCaptured();
                    break;
                case "Bạn được ủy thác đi hộ tống":
                    randomEventPic.setImageResource(R.drawable.quest_icon);
                    chooseEscort();
                    break;
                case "Bạn được ủy thác đi giải cứu":
                    randomEventPic.setImageResource(R.drawable.quest_icon);
                    chooseRescue();
                    break;
                case "Bạn được mời tham gia vào một giải đấu":
                    randomEventPic.setImageResource(R.drawable.tournament_icon);
                    chooseTournament();
                    break;
                default:
                    dismiss();
                    break;
            }
        } else if (dialog.equals("training")) {
            event = "Bạn đang suy nghĩ nên lựa chọn bài tập nào?";
            randomEventTxt.setText(event);
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
            btn6.setVisibility(View.GONE);

            btn1.setText("Cơ bắp");
            btn2.setText("Trí tuệ");

            btn1.setOnClickListener(v -> {
                loadProgress.setVisibility(View.GONE);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn4.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
                event += "\nBạn đã lựa chọn những bài tập cơ bắp để nâng sức mạnh của mình";
                btn4.setText("Nhẹ nhàng");
                btn5.setText("Bình thường (sức mạnh >20) (nhanh nhẹn >20)");
                btn6.setText("Nhanh (sức mạnh >35) (nhanh nhẹn >35)");
                if(!(strength > 20 && agility > 20)) btn5.setEnabled(false);
                if (!(strength > 35 && agility > 35)) btn6.setEnabled(false);
                btn4.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomStrength = random.nextInt(3) + 1;
                    strength += randomStrength;
                    int randomAgility = random.nextInt(3) + 1;
                    agility += randomAgility;
                    changeStress = 3;
                    stress += changeStress;
                    event +="\n1000, 1001,... bạn hoàn thành bài tập với cơ thể cường tráng (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });


                });
                btn5.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomStrength = random.nextInt(3) + 2;
                    strength += randomStrength;
                    int randomAgility = random.nextInt(3) + 2;
                    agility += randomAgility;
                    changeStress = 5;
                    stress += changeStress;
                    event +="\n1000, 1001,... bạn hoàn thành bài tập với cơ thể cường tráng (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });
                });

                btn6.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomStrength = random.nextInt(3) + 3;
                    strength += randomStrength;
                    int randomAgility = random.nextInt(3) + 3;
                    agility += randomAgility;
                    changeStress = 10;
                    changeHeart = 3;
                    stress += changeStress;
                    heart += changeHeart;
                    event += "\n1000, 1001,... bạn hoàn thành bài tập, từng dòng cơ cuồn cuộn trên cơ thể bạn (+" + changeHeart + " máu) (+" + randomStrength + " sức mạnh) (+" + randomAgility + " nhanh nhẹn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });
                });
            });
            btn2.setOnClickListener(v -> {
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn4.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
                event += "\nHiểu ra được giá trị của tri thức, bạn vùi đầu vào đọc sách ";
                btn4.setText("Hiểu biết");
                btn5.setText("Vận dụng (minh mẫn >20)");
                btn6.setText("Thực hành (minh mẫn >35) (có cơ hội học được kỹ năng mới)");
                if(!(smart > 20 )) btn5.setEnabled(false);
                if (!(smart > 35 )) btn6.setEnabled(false);
                btn4.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomSmart = random.nextInt(3) + 2;
                    smart += randomSmart;
                    changeStress = 3;
                    stress += changeStress;
                    event +="\nBạn cảm thấy minh mẫn hơn thường ngày (+" + randomSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });


                });
                btn5.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomSmart = random.nextInt(3) + 3;
                    smart += randomSmart;
                    changeStress = 3;
                    stress += changeStress;
                    event +="\nBạn cảm thấy minh mẫn hơn thường ngày (+" + randomSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });
                });

                btn6.setOnClickListener(v1 -> {
                    loadProgress.setVisibility(View.VISIBLE);
                    int randomSmart = random.nextInt(3) + 3;
                    smart += randomSmart;
                    changeStress = 3;
                    stress += changeStress;
                    event +="\nBạn cảm thấy minh mẫn hơn thường ngày (+" + randomSmart + " minh mẫn) (+" + changeStress + " căng thẳng)";
                    normalizeData();

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                    });
                });
            });
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

    private void randomSelectedActivity(){
        List<Map.Entry<String, Double>> activityProbabilities = new ArrayList<>();
        for (int i = 0; i < activities.length; i++) {
            activityProbabilities.add(new AbstractMap.SimpleEntry<>(activities[i], Double.parseDouble(probabilities[i])));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(activityProbabilities, Comparator.comparingDouble(Map.Entry::getValue));
        }

        List<List<Map.Entry<String, Double>>> groups = new ArrayList<>();
        double currentProbability = -1;
        List<Map.Entry<String, Double>> currentGroup = null;
        for (Map.Entry<String, Double> entry : activityProbabilities) {
            if (!Objects.equals(entry.getValue(), currentProbability)) {
                currentGroup = new ArrayList<>();
                groups.add(currentGroup);
                currentProbability = entry.getValue();
            }
            currentGroup.add(entry);
        }

        List<Map.Entry<String, Double>> selectedActivities = new ArrayList<>();
        Random random = new Random();
        for (List<Map.Entry<String, Double>> group : groups) {
            Map.Entry<String, Double> selectedActivity = group.get(random.nextInt(group.size()));
            selectedActivities.add(selectedActivity);
            group.remove(selectedActivity);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(selectedActivities, Comparator.comparingDouble(Map.Entry::getValue));
        }

        double randomNumber = random.nextDouble();
        Log.d(TAG, "randomSelectedActivity: " + randomNumber);
        for (Map.Entry<String, Double> entry : selectedActivities) {
            String key = entry.getKey();
            if (key != null) {
                if (randomNumber < entry.getValue()) {
                    selectedActivity = key;
                    break;
                }
            }
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Mở ra");
        btn2.setText("Bỏ đi");
        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                                loadProgress.setVisibility(View.GONE);
                                dismiss();
                            }
                        });
                    }
                });

            } else if (randomValue == 1) {
                loadProgress.setVisibility(View.VISIBLE);
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
                                loadProgress.setVisibility(View.GONE);
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
                                loadProgress.setVisibility(View.GONE);
                                dismiss();
                            }
                        });
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn chọn bỏ qua kho báu và tiếp tục cuộc hành trình, có thể đó là quyết định đúng đắn";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Chinh phạt");
        btn2.setText("Rời đi");
        btn1.setOnClickListener(v -> {
            // TODO: Entry Dungeon
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã tiến vào dungeon";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn chưa chắc chắn vào thực lực lúc này và quyết định rời đi";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Nhận");
        btn2.setText("Từ chối");
        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                                loadProgress.setVisibility(View.GONE);
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
                                loadProgress.setVisibility(View.GONE);
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
                                loadProgress.setVisibility(View.GONE);
                                dismiss();
                            }
                        });
                    }
                });
            }
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã từ chối lòng tốt của người dân, tuy nhiên ai biết được lòng người thế nào";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Tấn công (100  máu) (10 tấn công) (10 phòng thủ)");
        btn2.setText("Chạy thoát (nhanh nhẹn >20)");
        if (agility <= 20) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                if (damageInflicted < 0) { damageInflicted = 0; }
                if (damageReceived < 0) { damageReceived = 0; }
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
                                            loadProgress.setVisibility(View.GONE);
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
                                            loadProgress.setVisibility(View.GONE);
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
                                            loadProgress.setVisibility(View.GONE);
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
                                            loadProgress.setVisibility(View.GONE);
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
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã chạy thoát thành công";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

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
                btn1.setText("Bạn đụng độ với boar (25 máu) (7 tấn công) (5 phòng thủ)");
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
            loadProgress.setVisibility(View.VISIBLE);
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
                        event += "\nBạn đã chết";
                    }

                    updateMap();

                    listener.getAtt(hashMap);

                    FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    loadProgress.setVisibility(View.GONE);
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
            int randomValue = random.nextInt(2);
            boolean isEnd = false;
            while (!isEnd) {
                damageInflicted = attack - defEnemy;
                damageReceived = atkEnemy - defense;
                if (damageInflicted < 0) { damageInflicted = 0; }
                if (damageReceived < 0) { damageReceived = 0; }
                if (randomValue == 0) {
                    heartEnemy -= damageInflicted;
                    if (heartEnemy <= 0) {
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
                                event += "\nBạn đã đánh bại được slime (-" + totalDamageReceived + " máu) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
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

                        isEnd = true;

                        normalizeData();

                        updateMap();

                        listener.getAtt(hashMap);

                        FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loadProgress.setVisibility(View.GONE);
                                        dismiss();
                                    }
                                });
                            }
                        });
                        continue;
                    }
                    totalDamageReceived += damageReceived;
                    heart -= damageReceived;
                    if (heart <= 0) {
                        isEnd = true;
                        isDead = true;
                        event += "\nBạn đã chết";
                        heart = 0;

                        updateMap();

                        listener.getAtt(hashMap);

                        FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loadProgress.setVisibility(View.GONE);
                                        dismiss();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    totalDamageReceived += damageReceived;
                    heart -= damageReceived;
                    if (heart <= 0) {
                        isEnd = true;
                        isDead = true;
                        event += "\nBạn đã chết";
                        heart = 0;

                        updateMap();

                        listener.getAtt(hashMap);

                        FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loadProgress.setVisibility(View.GONE);
                                        dismiss();
                                    }
                                });
                            }
                        });
                        continue;
                    }
                    heartEnemy -= damageInflicted;
                    if (heartEnemy <= 0) {
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

                        isEnd = true;

                        normalizeData();

                        updateMap();

                        listener.getAtt(hashMap);

                        FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loadProgress.setVisibility(View.GONE);
                                        dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });

        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã chạy thoát thành công";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Dùng lực để phá (sức mạnh > 50)");
        btn2.setText("Sử dụng minh mẫn để mở (minh mẫn  > 20) (nhanh nhẹn > 20)");
        btn3.setText("Hét to để cầu cứu");
        if (!(strength > 50)) btn1.setEnabled(false);
        if (!(smart > 20 && agility > 20)) btn2.setEnabled(false);
        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });


        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                event += "\nBạn đã chết";
            }

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });

        btn3.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                event += "\nBạn đã chết";
            }

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Khám phá");
        btn2.setText("Rời khỏi");


        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
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
                                loadProgress.setVisibility(View.GONE);
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
                                loadProgress.setVisibility(View.GONE);
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
                                loadProgress.setVisibility(View.GONE);
                                dismiss();
                            }
                        });
                    }
                });
            }


        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã quyết định rời khỏi ngôi làng, không có gì là ngon ăn cả";

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
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
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Chiến đấu (sức mạnh > 30)");
        btn2.setText("Chạy khỏi (nhanh nhẹn >20)");
        btn3.setText("Đầu hàng");
        if (!(strength >30)) btn1.setEnabled(false);
        if (!(agility >20)) btn2.setEnabled(false);

        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeMoney = 500;
            changeStrength = 5;
            changeSmart = 5;
            changeStress = 3;
            money += changeMoney;
            strength += changeStrength;
            smart += changeSmart;
            stress += changeStress;
            event += "\nBạn đã đánh bại bè lũ cướp (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeStress = 10;
            stress += changeStress;
            event += "\nSau một hồi giằn co, với sự nhanh nhẹn của mình bạn đã trốn thoát thành công (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn3.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeStress = 10;
            changeMoney = -1000;
            changeHeart = -10;
            money += changeMoney;
            heart += changeHeart;
            stress += changeStress;
            event += "\nBạn cầu xin được tha mạng và giao hết đồ đạc tiền bạc cho lũ cướp để giữ cái mạng này (" + changeHeart + " máu) (" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";

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
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });

    }
    private void chooseEscort() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Đồng ý (sức mạnh >30) (minh mẫn >30) (căng thẳng <50)");
        btn2.setText("Từ chối");
        if (!(strength > 30 && smart > 30 && stress < 50)) btn1.setEnabled(false);

        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeMoney = 2000;
            changeStrength = 3;
            changeSmart = 3;
            changeStress = 3;
            money += changeMoney;
            strength += changeStrength;
            smart += changeSmart;
            stress += changeStress;
            event += "\nBạn đã may mắn thành công nhiệm vụ ủy thác (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
            int randomValue = random.nextInt(2);

            if (randomValue == 0){
                changeMoney = 1000;
                changeStress = -10;
                event += "\nVới sự giúp đỡ nhiệt tình, quý tộc được hộ tống đã đưa thêm phần thưởng để khích lệ bạn (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
            }

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeStress = 3;
            stress += changeStress;
            event += "\nBạn đã từ chối nhiệm vụ ủy thác (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
    }

    private void chooseRescue() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Đồng ý (sức mạnh >40) (minh mẫn >20) (căng thẳng <50)");
        btn2.setText("Từ chối");
        if (!(strength > 40 && smart > 20 && stress < 50)) btn1.setEnabled(false);

        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeMoney = 2000;
            changeStrength = 4;
            changeSmart = 2;
            changeStress = 3;
            money += changeMoney;
            strength += changeStrength;
            smart += changeSmart;
            stress += changeStress;
            event += "\nBạn đã may mắn thành công nhiệm vụ ủy thác (+" + changeStrength + " sức mạnh) (+" + changeSmart + " minh mẫn) (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
            int randomValue = random.nextInt(2);

            if (randomValue == 0){
                changeMoney = 1000;
                changeStress = -10;
                event += "\nVới sự giúp đỡ nhiệt tình, quý tộc được hộ tống đã đưa thêm phần thưởng để khích lệ bạn (+" + changeMoney + " đồng vàng) (+" + changeStress + " căng thẳng)";
            }

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            changeStress = 3;
            stress += changeStress;
            event += "\nBạn đã từ chối nhiệm vụ ủy thác (+" + changeStress + " căng thẳng)";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
    }
    private void chooseTournament() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);

        btn1.setText("Chấp nhận");
        btn2.setText("Từ chối");
        btn1.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã đăng ký và tiến vào giải đấu để đụng độ đối thủ đầu tiên ở vòng bảng";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
        btn2.setOnClickListener(v -> {
            loadProgress.setVisibility(View.VISIBLE);
            event += "\nBạn đã từ chối lời mời";

            normalizeData();

            updateMap();

            listener.getAtt(hashMap);

            FirebaseUtil.getPlayerModelReferenceWithId().update(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUtil.getDayModelReference().add(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            loadProgress.setVisibility(View.GONE);
                            dismiss();
                        }
                    });
                }
            });
        });
    }
}
