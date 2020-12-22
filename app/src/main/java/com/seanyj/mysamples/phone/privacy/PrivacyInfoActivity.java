package com.seanyj.mysamples.phone.privacy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.phone.CallEntity;
import com.seanyj.mysamples.util.phone.ContactEntity;
import com.seanyj.mysamples.util.phone.ContactsUtil;
import com.seanyj.mysamples.util.phone.SmsEntity;

import java.util.ArrayList;
import java.util.List;

public class PrivacyInfoActivity extends AppCompatActivity {
    private static final int RC_CONTACTS = 120;
    private static final int RC_CALLS = 121;
    private static final int RC_SMSES = 122;

    private ListView mListView;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_info);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.privacy_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_contacts) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        RC_CONTACTS);
            } else {
                showContacts();
            }
            return true;
        } else if (id == R.id.action_show_calls) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG},
                        RC_CALLS);
            } else {
                showCalls();
            }
            return true;
        } else if (id == R.id.action_show_SMSes) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                        RC_SMSES);
            } else {
                showSMSes();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<String> getStringList(List list) {
        if (list == null) {
            return null;
        }

        List<String> ret = new ArrayList<>();

        for (Object obj : list) {
            ret.add(obj.toString());
        }

        return ret;
    }

    private void showContacts() {
        List<ContactEntity> list = ContactsUtil.getContactListSync(this, true);
        mAdapter.clear();
        if (list != null) {
            mAdapter.addAll(getStringList(list));
        }
    }

    private void showCalls() {
        List<CallEntity> list = ContactsUtil.getCallListSync(this);
        mAdapter.clear();
        if (list != null) {
            mAdapter.addAll(getStringList(list));
        }
    }

    private void showSMSes() {
        List<SmsEntity> list = ContactsUtil.getSmsListSync(this);
        mAdapter.clear();
        if (list != null) {
            mAdapter.addAll(getStringList(list));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_CONTACTS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                } else {
                    showContacts();
                }
                break;
            case RC_CALLS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                } else {
                    showCalls();
                }
                break;
            case RC_SMSES:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                } else {
                    showSMSes();
                }
                break;
        }
    }
}
