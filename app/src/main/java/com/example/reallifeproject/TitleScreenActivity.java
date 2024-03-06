package com.example.reallifeproject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reallifeproject.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

public class TitleScreenActivity extends AppCompatActivity {
    private ImageView settingBtn;
    private FirebaseAuth mAuth;
    private Button newGameBtn, loadGameBtn, aboutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        initView();

        mAuth = FirebaseAuth.getInstance();

        settingBtn.setOnClickListener(v -> showMenu(settingBtn));

        newGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SexDeterminationActivity.class);
            startActivity(intent);
        });

        loadGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaitingActivity.class);
            startActivity(intent);
        });

        aboutBtn.setOnClickListener(v -> {
            // TODO: About
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.setting_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout){
                logout();
            }
            return false;
        });
        popup.setForceShowIcon(true);
        popup.show();
    }

    private void logout(){
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView(){
        settingBtn = findViewById(R.id.settingBtn);
        newGameBtn = findViewById(R.id.newGameBtn);
        loadGameBtn = findViewById(R.id.loadGameBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
    }
}