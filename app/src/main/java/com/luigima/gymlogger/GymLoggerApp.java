package com.luigima.gymlogger;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import timber.log.Timber;


public class GymLoggerApp extends Application {
    private GymLoggerAppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerGymLoggerAppComponent.builder()
                .gymLoggerAppModule(new GymLoggerAppModule(this))
                .build();

        /* Using Timber for logging.
         * Log everything in debug mode and only errors/wtfs in release mode
         */
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                //Adds the line number to the tag
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return "Timber/" + super.createStackElementTag(element) + ":" + element.getLineNumber();
                }

                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    super.log(priority, tag, message, t);
                    if (priority >= Log.WARN) {
                        Toast.makeText(getBaseContext(), message,Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Timber.plant(new ReleaseTree());
        }
    }

    public static GymLoggerAppComponent getComponent(Context context) {
        return ((GymLoggerApp) context.getApplicationContext()).mComponent;
    }

    class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            // Only errors and wtfs
            if (priority > Log.WARN) {
                Log.println(priority, tag, message);
            }
        }
    }
}
