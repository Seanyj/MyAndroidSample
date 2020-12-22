package com.seanyj.mysamples.customview.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.widget.MessageListItemView;

public class CustomDrawableStateActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_drawable_state);
        mListView = findViewById(R.id.listView);

        mListView.setAdapter(new ExampleListAdapter());
    }


    private static class ExampleListAdapter extends BaseAdapter {

        private Message[] messages;

        private ExampleListAdapter() {
            messages = new Message[]{
                    new Message("Gas bill overdue", true),
                    new Message("Congratulations, you've won!", true),
                    new Message("I love you!", false),
                    new Message("Please reply!", false),
                    new Message("You ignoring me?", false),
                    new Message("Not heard from you", false),
                    new Message("Electricity bill", true),
                    new Message("Gas bill", true),
                    new Message("Holiday plans", false),
                    new Message("Marketing stuff", false),
            };
        }

        @Override
        public int getCount() {
            return messages.length;
        }

        @Override
        public Object getItem(int position) {
            return messages[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            MessageListItemView messageListItemView = (MessageListItemView) convertView;

            if (messageListItemView == null) {
                messageListItemView = new MessageListItemView(viewGroup.getContext());
            }

            Message message = (Message) getItem(position);
            messageListItemView.setMessageSubject(message.subject);
            messageListItemView.setMessageUnread(message.unread);

            return messageListItemView;
        }

    }

    private static class Message {

        private String subject;
        private boolean unread;

        private Message(String subject, boolean unread) {
            this.subject = subject;
            this.unread = unread;
        }

    }
}
