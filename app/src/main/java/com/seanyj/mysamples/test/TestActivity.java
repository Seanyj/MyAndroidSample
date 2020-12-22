package com.seanyj.mysamples.test;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seanyj.mysamples.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private Button mButton;
    private EditText mEditText;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mButton = (Button) findViewById(R.id.button);
        mEditText = (EditText) findViewById(R.id.editText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("test");
        setSupportActionBar(toolbar);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //利用Intent打开微信
                    Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=HTTPS://QR.ALIPAY.COM/FKX06238OKNIRMWRNRNVCE");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    //若无法正常跳转，在此进行错误处理
                    Toast.makeText(TestActivity.this, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
                }

//                try {
//                    Intent intent = new Intent();
//                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//                    intent.setAction(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setComponent(cmp);
//                    startActivity(intent);
//                } catch (Exception e) {
//                }

//                try {
//                    Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
//                    Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=20000056");
//                    Uri uri = Uri.parse("weixin://dl/business/?ticket=ta428dhj739hg3efe6e");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                } catch (Exception e) {
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    public native String stringFromJNI();

    private class MyAdapter extends BaseAdapter {
        private List<TestEntity> mList;

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_test, parent, false);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            Log.e("hello", position + convertView.toString());
            TestEntity entity = (TestEntity) getItem(position);
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.titleView.setText(entity.getTitle());
            holder.valueView.setText(entity.getValue());

            return convertView;
        }

        private void setData(List<TestEntity> list) {
            mList = list;
            notifyDataSetChanged();
        }

        private void addLast(TestEntity entity) {
            if (entity == null) {
                return;
            }

            if (mList == null) {
                mList = new ArrayList<>();
            }

            mList.add(entity);
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView titleView;
            private TextView valueView;

            ViewHolder(View v) {
                titleView = (TextView) v.findViewById(R.id.titleTextView);
                valueView = (TextView) v.findViewById(R.id.valueTextView);
            }
        }
    }

}
