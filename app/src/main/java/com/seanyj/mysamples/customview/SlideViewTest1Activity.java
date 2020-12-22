package com.seanyj.mysamples.customview;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.widget.MyListView1;
import com.seanyj.mysamples.widget.SlideView1;

import java.util.ArrayList;
import java.util.List;

public class SlideViewTest1Activity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener, SlideView1.OnSlideListener {
    private ListView mListView;
    private List<MessageItem> mMessageItems = new ArrayList<>();
    private SlideAdapter mSlideAdapter;
    private SlideView1 mLastSlideViewWithStatusOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = new MyListView1(this);
        setContentView(mListView);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 20; i++) {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) {
                item.iconRes = android.R.drawable.ic_btn_speak_now;
                item.title = "中国新闻";
                item.msg = "青岛爆照， 大量雨水下";
                item.time = "18:18 pm";
            } else {
                item.iconRes = android.R.drawable.ic_dialog_info;
                item.title = "美国新闻";
                item.msg = "one river has been spooned";
                item.time = "12:23 pm";
            }
            mMessageItems.add(item);
        }

        mSlideAdapter = new SlideAdapter();
        mListView.setAdapter(mSlideAdapter);
        mListView.setOnItemClickListener(this);
    }

    private class SlideAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        SlideAdapter() {
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideView1 slideView = (SlideView1) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.list_item_slide_1, parent, false);
                slideView = new SlideView1(getBaseContext());
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(SlideViewTest1Activity.this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }

            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            holder.icon.setImageResource(item.iconRes);
            holder.title.setText(item.title);
            holder.msg.setText(item.msg);
            holder.time.setText(item.time);
            holder.deleteHolder.setOnClickListener(SlideViewTest1Activity.this);

            return slideView;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.holder) {
            int position = mListView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION) {
                mMessageItems.remove(position);
                mSlideAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getBaseContext(), "onItemClick: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView1) view;
        }
    }

    public class MessageItem {
        public int iconRes;
        public String title;
        public String msg;
        public String time;
        public SlideView1 slideView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView msg;
        TextView time;
        ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = view.findViewById(R.id.icon);
            title = view.findViewById(R.id.title);
            msg = view.findViewById(R.id.msg);
            time = view.findViewById(R.id.time);
            deleteHolder = view.findViewById(R.id.holder);
        }
    }
}
