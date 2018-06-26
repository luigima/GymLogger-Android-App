package com.luigima.gymlogger.module;

import android.app.Application;
import android.net.Uri;
import com.luigima.gymlogger.dagger.PerApp;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;


@Module
public class NetworkModule {

    static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;

    @Provides
    @PerApp
    OkHttpClient provideOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }

    @Provides @PerApp
    Picasso providePicasso(Application app, OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttpDownloader(client))
                .listener((picasso, uri, e) -> Timber.e(e.getCause(), "Failed to load image"))
                .build();
    }
}
