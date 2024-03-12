package com.example.reallifeproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reallifeproject.ml.Modellandscape;
import com.example.reallifeproject.model.PlayerModel;
import com.example.reallifeproject.utils.AndroidUtil;
import com.example.reallifeproject.utils.FirebaseUtil;
import com.google.firebase.firestore.DocumentSnapshot;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;


public class SceneDeterminationActivity extends AppCompatActivity {
    private ImageView imagePic;
    private TextView resultTxt;
    private Button takePicBtn, gallaryBtn, nextBtn;
    private String gender, scene;
    private int money, strength, smart, attack, magic, defense, agility;
    private String event;
    int imageSize = 64;

    private static final String TAG = "SceneDeterminationActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_determination);

        initView();

        gender = getIntent().getStringExtra("gender").toString();

        takePicBtn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallaryBtn.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(cameraIntent, 1);
        });

        nextBtn.setOnClickListener(v -> {

            if (imagePic.getDrawable() == null) {
                AndroidUtil.showToast(this, "Please take a picture");
            } else {
                String[] events = getApplicationContext().getResources().getStringArray(R.array.event_age0);
                if (gender.equals("Man")) {
                    if (scene.equals("Noble")) {
                        event = events[0];
                        strength = 0;
                        smart = 0;
                        Random random = new Random();
                        int randomAttack = random.nextInt(3) + 1;
                        int randomMagic = random.nextInt(3) + 1;
                        int randomDefense = random.nextInt(3) + 1;
                        int randomAgility = random.nextInt(3) + 1;
                        attack = randomAttack;
                        magic = randomMagic;
                        defense = randomDefense;
                        agility = randomAgility;
                        money = 500;
                    } else {
                        event = events[1];
                        strength = 0;
                        smart = 0;
                        Random random = new Random();
                        int randomAttack = random.nextInt(4) + 2;
                        int randomMagic = random.nextInt(4) + 2;
                        int randomDefense = random.nextInt(4) + 2;
                        int randomAgility = random.nextInt(4) + 2;
                        attack = randomAttack;
                        magic = randomMagic;
                        defense = randomDefense;
                        agility = randomAgility;
                        money = 200;
                    }
                } else {
                    if (scene.equals("Noble")) {
                        event = events[2];
                        strength = 0;
                        smart = 0;
                        Random random = new Random();
                        int randomAttack = random.nextInt(4) + 1;
                        int randomMagic = random.nextInt(4) + 1;
                        int randomDefense = random.nextInt(2) + 1;
                        int randomAgility = random.nextInt(4) + 1;
                        attack = randomAttack;
                        magic = randomMagic;
                        defense = randomDefense;
                        agility = randomAgility;
                        money = 500;
                    } else {
                        event = events[3];
                        strength = 0;
                        smart = 0;
                        Random random = new Random();
                        int randomAttack = random.nextInt(5) + 2;
                        int randomMagic = random.nextInt(5) + 2;
                        int randomDefense = random.nextInt(3) + 2;
                        int randomAgility = random.nextInt(5) + 2;
                        attack = randomAttack;
                        magic = randomMagic;
                        defense = randomDefense;
                        agility = randomAgility;
                        money = 200;
                    }
                }


                PlayerModel playerModel = new PlayerModel(gender, scene, FirebaseUtil.currentUserId(), money, 0, event, 100, 0, strength, smart, attack, magic, defense, agility);


                FirebaseUtil.getPlayerModelReference().get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(playerModel.getPlayerId())) {
                                    document.getReference().delete();
                                }
                            }
                            FirebaseUtil.getPlayerModelReferenceWithId().set(playerModel).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    navigateToInGame();
                                }
                            });
                        } else {
                            FirebaseUtil.getPlayerModelReferenceWithId().set(playerModel).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    navigateToInGame();
                                }
                            });
                            Log.d(TAG, "Collection does not exist or is empty");
                        }
                    } else {
                        Log.d(TAG, "Error getting collection: ", task.getException());
                    }
                });
            }
        });
    }

    private void navigateToInGame() {
        Intent intent = new Intent(this, WaitingActivity.class);
        startActivity(intent);
    }

    public void classifyImage(Bitmap image) {
        try {
            Modellandscape model = Modellandscape.newInstance(getApplicationContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            Modellandscape.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++) {
                if (confidence[i] > maxConfidence) {
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Noble", "Farmer"};
            resultTxt.setText(classes[maxPos]);
            scene = classes[maxPos];
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imagePic.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } else {
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePic.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        imagePic = findViewById(R.id.imagePic);
        takePicBtn = findViewById(R.id.takePicBtn);
        gallaryBtn = findViewById(R.id.gallaryBtn);
        nextBtn = findViewById(R.id.nextBtn);
        resultTxt = findViewById(R.id.resultTxt);
    }

}