package com.seanyj.mysamples.app.ui;

import android.app.NotificationManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.seanyj.mysamples.R;

public class NotificationOpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_open);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1);
    }
}
