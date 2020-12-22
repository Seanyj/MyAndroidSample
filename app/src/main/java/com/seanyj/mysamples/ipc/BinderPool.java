package com.seanyj.mysamples.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import java.util.concurrent.CountDownLatch;

public class BinderPool {
    private static BinderPool sBinderPool;
    private IPool mPool;
    private Context mContext;
    private final CountDownLatch mLatch;

    synchronized public static BinderPool getInstance(Context context) throws InterruptedException {
        if (sBinderPool == null) {
            sBinderPool = new BinderPool(context);
        }

        return sBinderPool;
    }

    private BinderPool(final Context context) throws InterruptedException {
        mContext = context.getApplicationContext();
        mLatch = new CountDownLatch(1);

        final ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("hello", "onServiceConnected: " + Thread.currentThread());
                mPool = IPool.Stub.asInterface(service);
                mLatch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Log.e("hello", "thread: " + Thread.currentThread());
        Intent i = new Intent(context, PoolService.class);
        mContext.bindService(i, connection, Context.BIND_AUTO_CREATE);

        try {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder getBinder(int type) throws RemoteException {
        IBinder binder = null;
        switch (type) {
            case 1:
                binder = mPool.getBinder(1);
                break;
            case 2:
                binder = mPool.getBinder(2);
                break;
        }

        return binder;
    }
}
