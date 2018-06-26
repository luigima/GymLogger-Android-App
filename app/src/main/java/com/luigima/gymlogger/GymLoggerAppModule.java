package com.luigima.gymlogger;

import android.app.Application;
import com.luigima.gymlogger.dagger.PerApp;


import dagger.Module;
import dagger.Provides;


@Module
public class GymLoggerAppModule {
    private final GymLoggerApp app;

    public GymLoggerAppModule(GymLoggerApp app) {
        this.app = app;
    }

    @Provides @PerApp
    GymLoggerApp provideGymLoggerApp() {
        return app;
    }

    @Provides @PerApp
    Application provideApplication(GymLoggerApp app) {
        return app;
    }
}
