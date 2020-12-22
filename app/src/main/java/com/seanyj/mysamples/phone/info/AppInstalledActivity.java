package com.seanyj.mysamples.phone.info;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.AppUtil;

import java.util.List;

public class AppInstalledActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_installed);
        mListView = (ListView) findViewById(R.id.listView);

        List<AppUtil.AppInfo> list = AppUtil.getPackages(this);
        MyAdapter adapter = new MyAdapter(this, list);
        mListView.setAdapter(adapter);
    }

    private class MyAdapter extends BaseAdapter {
        private List<AppUtil.AppInfo> mData;
        private Context mContext;

        public MyAdapter(Context context, List<AppUtil.AppInfo> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_app_installed,
                        parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            AppUtil.AppInfo item = (AppUtil.AppInfo) getItem(position);
            holder.mIconImageView.setImageDrawable(item.getAppIcon());
            holder.mTitleTextView.setText(item.getAppName());
            String desc = "package: " + item.getPackageName() + "; version: " + item.getVersionName()
                    + "; versionCode: " + item.getVersionCode();
            holder.mDescTextView.setText(desc);

            return convertView;
        }

        private class ViewHolder {
            private ImageView mIconImageView;
            private TextView mTitleTextView;
            private TextView mDescTextView;

            ViewHolder(View v) {
                mIconImageView = (ImageView) v.findViewById(R.id.iconImageView);
                mTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
                mDescTextView = (TextView) v.findViewById(R.id.descTextView);
            }
        }
    }
}
