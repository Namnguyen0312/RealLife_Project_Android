package com.example.reallifeproject.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reallifeproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EventDialog extends DialogFragment {
    private Button btn1, btn2, btn3;
    private TextView randomEventTxt;
    private ImageView randomEventPic;
    private int age;
    private List<String> activities;
    private List<Double> probabilities;
    private List<Double> cumulativeProbabilities;
    private String selectedActivity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();
        age = bundle.getInt("age");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_event, null);

        builder.setView(view);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        randomEventTxt = view.findViewById(R.id.randomEventTxt);
        randomEventPic = view.findViewById(R.id.randomEventPic);

        activities = new ArrayList<>();
        probabilities = new ArrayList<>();

        String[] activityArray = getResources().getStringArray(R.array.activities_age1_age10);
        String[] probabilityArray = getResources().getStringArray(R.array.probabilities_age1_age10);

        for (String activity : activityArray) {
            activities.add(activity);
        }
        for (String probability : probabilityArray) {
            probabilities.add(Double.parseDouble(probability));
        }

        calculateCumulativeProbabilities();

        generateRandomActivity();

        if (selectedActivity == null){
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            randomEventPic.setVisibility(View.GONE);
            btn1.setOnClickListener(v -> {
            });
        } else if (selectedActivity.equals(activities.get(0))) {
            chooseStumble();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }else if (selectedActivity.equals("Choose your sword")) {
//            chooseSword();
        }













        setCancelable(false);

        return builder.create();
    }

    private void chooseStumble(){
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        randomEventPic.setVisibility(View.VISIBLE);
        randomEventPic.setImageResource(R.drawable.sword_icon);

        btn1.setText("Crying and talking to mother (+2 Smart)");
        btn2.setText("Longsword (+2 Strength)");
        btn3.setText("Rapier (+2 Smart)");
        btn1.setOnClickListener(v -> {

        });
        btn2.setOnClickListener(v -> {

        });
        btn3.setOnClickListener(v -> {

        });
    }

    private void calculateCumulativeProbabilities() {
        cumulativeProbabilities = new ArrayList<>();
        double cumulativeProbability = 0;
        for (Double probability : probabilities) {
            cumulativeProbability += probability;
            cumulativeProbabilities.add(cumulativeProbability);
        }
    }

    private void generateRandomActivity() {
        Random random = new Random();
        double randomValue = random.nextDouble();

        selectedActivity = null;
        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (randomValue <= cumulativeProbabilities.get(i)) {
                selectedActivity = activities.get(i);
                break;
            }
        }

        // Hiển thị kết quả lên TextView
        if (selectedActivity != null) {
            randomEventTxt.setText(selectedActivity);
        }
    }
}
