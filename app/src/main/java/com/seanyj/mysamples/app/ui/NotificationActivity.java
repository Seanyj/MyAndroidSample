package com.seanyj.mysamples.app.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seanyj.mysamples.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Button notificationBtn = findViewById(R.id.notificationBtn);
        Button bigTextBtn = findViewById(R.id.bigTextBtn);
        Button bigImageBtn = findViewById(R.id.bigImgBtn);

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent i = new Intent(NotificationActivity.this, NotificationOpenActivity.class);
                PendingIntent p = PendingIntent.getActivity(NotificationActivity.this, 0,
                        i, 0);
                Notification notification = new NotificationCompat.Builder(NotificationActivity.this,
                        null)
                        .setContentTitle("content title")
                        .setContentText("content text")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.icon_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setContentIntent(p)
//                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
//                        .setVibrate(new long[] {0, 1000, 1000, 1000})
//                        .setLights(Color.GREEN, 1000, 1000)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .build();
                nm.notify(1, notification);
            }
        });

        bigTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent i = new Intent(NotificationActivity.this, NotificationOpenActivity.class);
                PendingIntent p = PendingIntent.getActivity(NotificationActivity.this, 0,
                        i, 0);
                Notification notification = new NotificationCompat.Builder(NotificationActivity.this,
                        null)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("this ksjllsj dsjallkj daljldj kj" +
                                "lsajdlsj kdlajl ajldja ljasld dlkajdklasjl djlsdj lajl dsjal jdlasj dl"))
                        .setContentTitle("content title")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.icon_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setContentIntent(p)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .build();
                nm.notify(1, notification);
            }
        });

        bigImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent i = new Intent(NotificationActivity.this, NotificationOpenActivity.class);
                PendingIntent p = PendingIntent.getActivity(NotificationActivity.this, 0,
                        i, 0);
                Notification notification = new NotificationCompat.Builder(NotificationActivity.this,
                        null)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(
                                BitmapFactory.decodeResource(getResources(), R.drawable.pic_small_1)
                        ))
                        .setContentTitle("content title")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.icon_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setContentIntent(p)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();
                nm.notify(1, notification);
            }
        });
    }
}
