package com.phongnguyen.cloudsyncdemo.api.di.modules;

import android.app.Application;

import com.phongnguyen.cloudsyncdemo.dropbox.FileThumbnailRequestHandler;
import com.squareup.picasso.Cache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application mApplication;
    Picasso picasso;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


}
