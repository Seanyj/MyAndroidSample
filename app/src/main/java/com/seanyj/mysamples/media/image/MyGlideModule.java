package com.seanyj.mysamples.media.image;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

@GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(2, TimeUnit.SECONDS);
        builder.writeTimeout(2, TimeUnit.SECONDS);
        builder.connectTimeout(2, TimeUnit.SECONDS);
        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }
}


