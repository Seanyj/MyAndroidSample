package com.seanyj.mysamples.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.util.Log;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookService extends Service{
    private CopyOnWriteArrayList<Book> mBooks;

    private IBinder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("hello", getClass().getSimpleName() + " onCreate");
        mBooks = new CopyOnWriteArrayList<>();
        Book book = new Book();
        book.setId(1);
        book.setName("Jane Eyre");
        mBooks.add(book);
        book = new Book();
        book.setId(2);
        book.setName("Gone with the Wind");
        mBooks.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("hello", "onBind" + mBinder);
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("hello", getClass().getSimpleName() + " onDestroy");
    }
}
