package com.devtyagi.smartlighting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devtyagi.smartlighting.R;

public class IPAddressActivity extends AppCompatActivity {

    ImageView imgNext;
    EditText etIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_address);
        etIPAddress = findViewById(R.id.etIPAddress);
        imgNext = findViewById(R.id.imgNext);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = etIPAddress.getText().toString();
                if(ipAddress.isEmpty()) {
                    Toast.makeText(IPAddressActivity.this,"Enter IP Address", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String[] ipSplit = ipAddress.split("\\.");
                    if(ipSplit.length != 4) {
                        Toast.makeText(IPAddressActivity.this,"Invalid IP Address", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        try {
                            for(String s : ipSplit) {
                                int i = Integer.parseInt(s);
                            }
                        } catch(NumberFormatException e) {
                            Toast.makeText(IPAddressActivity.this,"Invalid IP Address", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                Intent i = new Intent(IPAddressActivity.this, MainActivity.class);
                i.putExtra("ip", ipAddress);
                startActivity(i);
                finish();
            }
        });
    }
}