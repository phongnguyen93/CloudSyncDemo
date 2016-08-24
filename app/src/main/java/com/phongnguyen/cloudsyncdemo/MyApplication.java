package com.phongnguyen.cloudsyncdemo;

import android.app.Application;

import com.phongnguyen.cloudsyncdemo.api.di.components.DaggerMainComponent;
import com.phongnguyen.cloudsyncdemo.api.di.components.DaggerNetComponent;
import com.phongnguyen.cloudsyncdemo.api.di.components.MainComponent;
import com.phongnguyen.cloudsyncdemo.api.di.components.NetComponent;
import com.phongnguyen.cloudsyncdemo.api.di.modules.ApiModule;
import com.phongnguyen.cloudsyncdemo.api.di.modules.AppModule;
import com.phongnguyen.cloudsyncdemo.api.di.modules.NetModule;

/**
 * Created by phongnguyen on 8/23/16.
 */
public class MyApplication extends Application {

    public static final String BASE_URL = "http://aphong.cloudapp.net:9092/";

    private NetComponent mNetComponent;
    private MainComponent mMainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BASE_URL))
                .build();

        mMainComponent = DaggerMainComponent.builder()
                .netComponent(mNetComponent)
                .apiModule(new ApiModule())
                .build();
    }
    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public MainComponent getMainComponent() {
        return mMainComponent;
    }
}
