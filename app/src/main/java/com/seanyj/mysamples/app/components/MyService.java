package com.seanyj.mysamples.app.components;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.seanyj.mysamples.ipc.Book;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = intent.getIntExtra("value", 0);

        Log.e("hello", "value: " + i);
        if ((int)(Math.random() * 20) == 10) {
            i = 10;
            Log.e("hello", "value: " + i);
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("hello", "onDestroy");
        super.onDestroy();
    }

    class MyBinder extends Binder {
        public void salutation(String name) {
            Toast.makeText(MyService.this, "hello " + name, Toast.LENGTH_SHORT).show();
        }

        public List<Book> getBooks() {
            List<Book> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Book book = new Book();
                book.setId(i + 1);
                book.setName("book" + (i + 1));
                list.add(book);
            }
            return list;
        }
    }
}
