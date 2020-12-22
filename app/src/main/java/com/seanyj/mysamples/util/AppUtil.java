package com.seanyj.mysamples.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.seanyj.mysamples.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class AppUtil {
    public static List<AppInfo> getPackages(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        if (packages == null) {
            return null;
        }

        List<AppInfo> list = new ArrayList<>();

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(
                        packageInfo.applicationInfo.loadLabel(pm).toString());//获取应用名称
                appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));//获取应用图标
                list.add(appInfo);
            } else { // 系统应用
            }
        }

        return list;
    }

    public static List<AppInfo> getPackages1(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        if (resolveInfos == null) {
            return null;
        }

        List<AppInfo> list = new ArrayList<>();
        for (int i = 0; i < resolveInfos.size(); i++) {
            ResolveInfo info = resolveInfos.get(i);
            if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String packageName = info.activityInfo.packageName;
                String label = info.loadLabel(pm).toString();
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(label);
                appInfo.setPackageName(packageName); //获取应用包名，可用于卸载和启动应用
                appInfo.setVersionName(null);//获取应用版本名
                appInfo.setVersionCode(0);//获取应用版本号
                appInfo.setAppIcon(info.activityInfo.applicationInfo.loadIcon(pm));//获取应用图标
                list.add(appInfo);
            }
        }

        return list;
    }

    public static void cleanAppData() {
        File dataDir = App.getInstance().getDir("null", Context.MODE_PRIVATE);
//        deleteFile(new File("data/data/" + App.getInstance().getPackageName()));
        Log.e("hello", "dataDir: " + dataDir.getParentFile().getAbsolutePath());
        deleteFile(dataDir.getParentFile());
    }

    private static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static class AppInfo {
        private String appName;
        private String packageName;
        private String versionName;
        private int versionCode;
        private Drawable appIcon;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }
    }
}
