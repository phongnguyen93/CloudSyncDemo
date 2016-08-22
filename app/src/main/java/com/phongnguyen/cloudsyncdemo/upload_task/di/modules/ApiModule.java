package com.phongnguyen.cloudsyncdemo.upload_task.di.modules;


import com.phongnguyen.cloudsyncdemo.upload_task.di.scopes.UserScope;
import com.phongnguyen.cloudsyncdemo.upload_task.interfaces.ApiInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class ApiModule {

    @Provides
    @UserScope
    public ApiInterface providesApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
}
