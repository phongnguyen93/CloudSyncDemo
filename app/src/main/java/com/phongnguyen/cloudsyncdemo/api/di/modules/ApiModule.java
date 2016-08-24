package com.phongnguyen.cloudsyncdemo.api.di.modules;


import com.phongnguyen.cloudsyncdemo.api.di.scopes.UserScope;
import com.phongnguyen.cloudsyncdemo.api.interfaces.ApiInterface;

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
