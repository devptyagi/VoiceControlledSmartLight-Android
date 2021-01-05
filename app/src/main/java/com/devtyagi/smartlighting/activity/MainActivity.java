package com.devtyagi.smartlighting.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devtyagi.smartlighting.R;
import com.devtyagi.smartlighting.util.VolleySingleton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int REQ_CODE = 100;
    Button btnON, btnOFF;
    String ipAddress;
    ImageView imgBulb, imgSpeak;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        ipAddress = getIntent().getStringExtra("ip");
        Log.d(TAG, "Ip Address : " + ipAddress);
        btnOFF = findViewById(R.id.btnOFF);
        btnON = findViewById(R.id.btnON);
        imgBulb = findViewById(R.id.imgBulb);
        imgSpeak = findViewById(R.id.imgSpeak);
        imgSpeak.setOnClickListener(v -> recogniseSpeech());
        btnON.setOnClickListener(v -> turnOnLight());
        btnOFF.setOnClickListener(v -> turnOffLight());
    }

    private void recogniseSpeech() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(i, REQ_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Device Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOffLight() {
        String url = "http://" + ipAddress + "/OFF";
        Log.d(TAG, "Turn Off URL: " + url);
        StringRequest turnOffRequest = new StringRequest(Request.Method.GET, url,
                response -> imgBulb.setImageResource(R.drawable.bulb_off),
                error -> {

                });
        requestQueue.add(turnOffRequest);
    }

    private void turnOnLight() {
        String url = "http://" + ipAddress + "/ON";
        Log.d(TAG, "Turn On URL: " + url);
        StringRequest turnOnRequest = new StringRequest(Request.Method.GET, url,
                response -> imgBulb.setImageResource(R.drawable.bulb_on),
                error -> {

                });
        requestQueue.add(turnOnRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sendCommandFromSpeech(result.get(0));
            }
        }
    }

    private void sendCommandFromSpeech(String s) {
        if(s.equalsIgnoreCase("turn on")) turnOnLight();
        else if(s.equalsIgnoreCase("turn off")) turnOffLight();
        else Toast.makeText(MainActivity.this, "Invalid Command", Toast.LENGTH_SHORT).show();
    }
}