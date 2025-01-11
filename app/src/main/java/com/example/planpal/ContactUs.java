package com.example.planpal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactUs extends AppCompatActivity {

    ImageButton ContactUs_back, ContactUs_send;
    EditText ContactUs_message;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ContactUs_back = findViewById(R.id.ContactUs_back);
        ContactUs_send = findViewById(R.id.ContactUs_send);
        ContactUs_message = findViewById(R.id.ContactUs_message);

        ContactUs_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_homepage = new Intent(ContactUs.this, HomeActivity.class);
                startActivity(intent_homepage);
            }
        });


        ContactUs_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ph = "9822457348";
                String message = ContactUs_message.getText().toString().trim();

                if (ContextCompat.checkSelfPermission(
                        ContactUs.this, android.Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    sendSMS(ph, message);
                    ContactUs_message.setText("");
                }
                else {
                    ActivityCompat.requestPermissions(ContactUs.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
        });

    }


    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(ContactUs.this, "Message sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ContactUs.this, "Error: the message wasn't sent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, resend the SMS
                String ph = "9822457348";
                String message = ContactUs_message.getText().toString().trim();
                sendSMS(ph, message);
            } else {
                // Permission denied, notify the user
                Toast.makeText(ContactUs.this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

}