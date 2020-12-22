package com.seanyj.mysamples.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MyService extends Service {
    private NotificationManager mNotificationManager;
    private boolean canRun = true;
    private String retString = null;

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        displayNotificationMessage("service started");
        return mBinder;
    }

    @Override
    public void onCreate() {
//        Thread thread = new Thread(null, new ServiceWorder(), "backgroundService");
//        thread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        displayNotificationMessage("service started", true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        canRun = false;
        super.onDestroy();
    }

    public String getImage(String url) {
        return "19";
    }

    public String getRetString() {
        return retString;
    }

    public boolean loginValidate(String userName, String password) throws Exception {
        String uriStr = "http://www.renyugang.cn/blog/admin/admin_check.jsp";
        boolean ret = false;
        return true;
    }

    void displayNotificationMessage(String msg) {
    }
}
