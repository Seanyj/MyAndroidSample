package com.seanyj.mysamples;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seanyj.mysamples.util.AppUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String EXTRA_PATH = "extra_path";
    public static final String CATEGORY_MY_SAMPLE = "com.seansunyj.intent.category.sample";

    static {
        System.loadLibrary("native-lib");
    }

    public native String transformToUpper(String s);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1}));

        Log.e("hello", "count: " + listView.getCount());
        Map<String, Intent> itemAtPosition = (Map<String, Intent>) listView.getItemAtPosition(0);
        Log.e("hello", "title: " + itemAtPosition.get("title"));

        Log.e("hello", transformToUpper("hello"));
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> myData = new ArrayList<>();

        String prefix = getIntent().getStringExtra(EXTRA_PATH);
        if (prefix == null) {
            prefix = "";
        }

        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(CATEGORY_MY_SAMPLE);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);

        if (resolveInfos == null) {
            return myData;
        }

        String[] prefixPaths = prefix.equals("") ? null : prefix.split("/");
        Map<String, Boolean> entries = new HashMap<>();

        for (ResolveInfo info : resolveInfos) {
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;

            if (prefix.equals("") || label.startsWith(prefix + "/")) {
                String[] labelPaths = label.split("/");
                String nextLabel = prefixPaths == null ? labelPaths[0] : labelPaths[prefixPaths.length];

                if ((prefixPaths != null ? prefixPaths.length : 0) == labelPaths.length - 1) {
                    addItem(myData, nextLabel, activityIntent(info.activityInfo.packageName, info.activityInfo.name));
                } else if (entries.get(nextLabel) == null) {
                    addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                    entries.put(nextLabel, true);
                }
            }
        }

        Collections.sort(myData, sDisplayComparator);

        return myData;
    }

    private Comparator<Map<String, Object>> sDisplayComparator = new Comparator<Map<String, Object>>() {
        Collator mCollator = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            return mCollator.compare(o1.get("title"), o2.get("title"));
        }
    };

    private void addItem(List<Map<String, Object>> list, String name, Intent intent) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", name);
        map.put("intent", intent);
        list.add(map);
    }

    private Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra(MainActivity.EXTRA_PATH, path);
        return result;
    }

    private Intent activityIntent(String packageName, String componentName) {
        Intent result = new Intent();
        result.setComponent(new ComponentName(packageName, componentName));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Intent> map = (Map<String, Intent>) parent.getItemAtPosition(position);
        Intent i = map.get("intent");
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AppUtil.cleanAppData();
        super.onDestroy();
    }
}
