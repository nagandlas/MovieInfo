package com.slack.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class MovieInfoApplicationModule extends AbstractModule
{
    private static final String POSTER_PATH_PREFIX = "https://image.tmdb.org/t/p/w100_and_h100_bestv2";
    private static final String PREFIX_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_DEV_KEY = "?api_key=3dec3ef465bc6f71ae7089dcbe120fbd&language=en-US";

    @Override
    protected void configure()
    {
    }

    @Provides
    @ApiEndPoint
    @Singleton
    String getMovieDBEndPoint()
    {
        return PREFIX_URL + "%s" + API_DEV_KEY;
    }

    @Provides
    @PosterPath
    @Singleton
    String getPosterPath()
    {
        return POSTER_PATH_PREFIX;
    }

    @Provides
    @Singleton
    App getApp()
    {
        return new App();
    }

    @Provides
    @Singleton
    SlackAppServer getSlackServer(App app)
    {
        return new SlackAppServer(app);
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface ApiEndPoint
    {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface PosterPath
    {
    }
}
