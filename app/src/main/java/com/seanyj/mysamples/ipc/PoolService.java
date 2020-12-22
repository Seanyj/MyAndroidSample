package com.seanyj.mysamples.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.util.Log;


import java.util.List;

public class PoolService extends Service{

    private IBinder mComputeBinder = new ICompute.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }
    };

    private IBinder mManagerBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return null;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
        }
    };

    private IBinder mBinder = new IPool.Stub() {
        @Override
        public IBinder getBinder(int type) throws RemoteException {
            switch (type) {
                case 1:
                    return mComputeBinder;
                case 2:
                    return  mManagerBinder;
            }
            return null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("hello", "onBind");
        return mBinder;
    }
}
