package com.seanyj.mysamples.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.test.JsCallAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    public static final String URL = "web_url";
    public static final String TITLE = "web_title";

    @BindView(R.id.webView)
    WebView mWebView;

    private String mUrl;
    private String mTitle;

    public static void startThisActivity(Context context, String url, String title) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(URL, url);
        i.putExtra(TITLE, title);
        context.startActivity(i);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTitle = intent.getStringExtra(TITLE);
        mUrl = intent.getStringExtra(URL);
        initLoad();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web_view);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
        initLoad();
    }

    public void initData() {
        mTitle = getIntent().getStringExtra(TITLE);
        mUrl = getIntent().getStringExtra(URL);
    }

    public void initView() {
        initWebView();
    }

    public void initListener() {
    }

    public void initLoad() {
//        mWebView.loadUrl(mUrl);
        mWebView.loadUrl("file:///android_asset/javascript.html");
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.addJavascriptInterface(new JsCallAndroid(this), "android");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });
    }

}
