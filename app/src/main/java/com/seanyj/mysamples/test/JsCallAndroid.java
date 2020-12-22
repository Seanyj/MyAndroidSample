package com.seanyj.mysamples.test;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2018/4/19.
 */

public class JsCallAndroid {
    Context mContext;

    public JsCallAndroid(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void closePage(String msg) {
        if (mContext instanceof Activity) {
//            ((Activity) mContext).finish();
        }
    }
}
