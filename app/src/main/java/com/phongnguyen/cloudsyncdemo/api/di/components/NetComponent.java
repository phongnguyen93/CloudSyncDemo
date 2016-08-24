package com.phongnguyen.cloudsyncdemo.api.di.components;


import android.content.SharedPreferences;


import com.phongnguyen.cloudsyncdemo.api.di.modules.AppModule;
import com.phongnguyen.cloudsyncdemo.api.di.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;


@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    Retrofit retrofit();
    okhttp3.OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}