package com.seanyj.mysamples.app.components;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.ipc.Book;

import java.util.List;

public class ServiceTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent mI;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        findViewById(R.id.startBtn).setOnClickListener(this);
        findViewById(R.id.stopBtn).setOnClickListener(this);
        findViewById(R.id.bindBtn).setOnClickListener(this);
        findViewById(R.id.unbindBtn).setOnClickListener(this);

        mI = new Intent(this, MyService.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myBinder.salutation("Jack");
                List<Book> books = myBinder.getBooks();
                if (books != null) {
                    for (int i = 0; i < books.size(); i++) {
                        Log.e("hello", books.get(i).getName());
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("hello", "onServiceDisconnected: " + name);
            }
        };
    }

    @Override
    public void onClick(View v) {

       switch (v.getId()) {
           case R.id.startBtn:
               mI.putExtra("value", 1);
               startService(mI);
               break;
           case R.id.stopBtn:
               stopService(mI);
               break;
           case R.id.bindBtn:
               bindService(mI, mServiceConnection,BIND_AUTO_CREATE);
               break;
           case R.id.unbindBtn:
               unbindService(mServiceConnection);
               break;
       }
    }
}
