package com.seanyj.mysamples.ipc;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.util.Log;

public class MessengerService extends Service {
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e("hello", Process.myPid() + " receive: " + msg.getData().getString("msg"));
                    Messenger replyTo = msg.replyTo;
                    Message replyMsg = Message.obtain(null, 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "mypid is: " + Process.myPid());
                    replyMsg.setData(bundle);
                    try {
                        replyTo.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;

            }
        }
    };

    private Messenger mMessenger = new Messenger(mHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
