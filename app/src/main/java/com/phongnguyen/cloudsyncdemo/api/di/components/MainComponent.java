package com.phongnguyen.cloudsyncdemo.api.di.components;



import com.phongnguyen.cloudsyncdemo.MainActivity;
import com.phongnguyen.cloudsyncdemo.api.di.modules.ApiModule;
import com.phongnguyen.cloudsyncdemo.api.di.scopes.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = NetComponent.class, modules = ApiModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
