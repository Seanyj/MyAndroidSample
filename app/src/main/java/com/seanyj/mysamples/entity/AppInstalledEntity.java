package com.seanyj.mysamples.entity;

/**
 * Created by Administrator on 2017/11/21.
 */

public class AppInstalledEntity {
    public String appId;
    public String appName;

    @Override
    public String toString() {
        return appId + " | " + appName;
    }
}
