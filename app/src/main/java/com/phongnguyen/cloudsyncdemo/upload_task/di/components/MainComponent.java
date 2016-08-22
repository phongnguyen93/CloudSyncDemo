package com.phongnguyen.cloudsyncdemo.upload_task.di.components;



import com.phongnguyen.cloudsyncdemo.MainActivity;
import com.phongnguyen.cloudsyncdemo.upload_task.di.modules.ApiModule;
import com.phongnguyen.cloudsyncdemo.upload_task.di.scopes.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = NetComponent.class, modules = ApiModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
