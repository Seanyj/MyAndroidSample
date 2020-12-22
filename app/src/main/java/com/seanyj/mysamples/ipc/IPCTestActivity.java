package com.seanyj.mysamples.ipc;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seanyj.mysamples.R;

import java.util.List;

public class IPCTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PROVIDER_BASE = "content://com.seansunyj.book_provider";

    private ServiceConnection mServiceConnection;
    private Handler mHandler;
    private TextView mTextView;
    private IBookManager mBookManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);

        mTextView = findViewById(R.id.textView);
        findViewById(R.id.messengerTestBtn).setOnClickListener(this);
        findViewById(R.id.aidlTestBtn).setOnClickListener(this);
        findViewById(R.id.addTestBtn).setOnClickListener(this);
        findViewById(R.id.binderPoolBtn).setOnClickListener(this);

        findViewById(R.id.queryBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messengerTestBtn:
                testMessenger();
                break;
            case R.id.aidlTestBtn:
                bindInAidl();
                break;
            case R.id.addTestBtn:
                addBook();
                break;
            case R.id.queryBtn:
                queryBook();
                break;
            case R.id.binderPoolBtn:
                poolTest();
                break;
        }
    }

    private void poolTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IBinder binder = BinderPool.getInstance(IPCTestActivity.this).getBinder(1);
                    ICompute iCompute = ICompute.Stub.asInterface(binder);
                    Log.e("hello", "compute: " + iCompute.add(1, 2));
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void queryBook() {
        if (mBookManager != null) {
            List<Book> bookList = null;
            try {
                bookList = mBookManager.getBookList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (bookList != null) {
                Log.e("hello", bookList.getClass().getCanonicalName());
                String data = new Gson().toJson(bookList);
                Log.e("hello", "list: " + data);
                mTextView.setText(data);
            }
        }
    }

    private void addBook() {
        if (mBookManager != null) {
            try {
                Book book = new Book();
                book.setId((int) (Math.random() * 100));
                book.setName("Book " + System.currentTimeMillis());
                mBookManager.addBook(book);
                List<Book> bookList = mBookManager.getBookList();
                if (bookList != null) {
                    Log.e("hello", bookList.getClass().getCanonicalName());
                    String data = new Gson().toJson(bookList);
                    Log.e("hello", "list: " + data);
                    mTextView.setText(data);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    private IBinder.DeathRecipient mRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null) {
                return;
            }

            mBookManager.asBinder().unlinkToDeath(mRecipient, 0);
            mBookManager = null;
            Log.e("hello", "binderDied");
        }
    };

    // 重复调用这个方法service的onBind()只会调用一次
    private void bindInAidl() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    service.linkToDeath(mRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mBookManager = IBookManager.Stub.asInterface(service);
                try {
                    List<Book> bookList = mBookManager.getBookList();
                    if (bookList != null) {
                        Log.e("hello", bookList.getClass().getCanonicalName());
                        String data = new Gson().toJson(bookList);
                        Log.e("hello", "list: " + data);
                        mTextView.setText(data);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("hello", "onServiceDisconnected: " + name);
            }

            @Override
            public void onBindingDied(ComponentName name) {
                Log.e("hello", "onBindingDied: " + name);
            }
        };

        Intent i = new Intent(this, BookService.class);
        bindService(i, mServiceConnection, BIND_AUTO_CREATE);
    }

    @SuppressLint("HandlerLeak")
    private void testMessenger() {
        clear();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String reply = msg.getData().getString("msg");
                        mTextView.setText(reply);
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Messenger messenger = new Messenger(service);
                Message message = Message.obtain(null, 1);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "hello msg from client: " + Process.myPid());
                message.setData(bundle);
                message.replyTo = new Messenger(mHandler);
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        Intent i = new Intent(this, MessengerService.class);
        bindService(i, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void clear() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        mServiceConnection = null;
    }

    @Override
    protected void onDestroy() {
        clear();
        super.onDestroy();
    }
}
