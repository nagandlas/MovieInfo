package com.slack;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.component.MovieCacheBuildComponent;
import com.slack.events.*;
import com.slack.guice.MovieInfoApplicationModule;

import javax.inject.Inject;

public class MovieInfoApplicationService
{
    @Inject
    MovieCacheBuildComponent cacheBuildComponent;
    @Inject
    App app;
    @Inject
    HomePageOpenedEvent homePageOpenedEvent;
    @Inject
    StaticSelectMovieBlockAction selectMovieBlockAction;
    @Inject
    PostChatMessageAction externalSelectBlockAction;

    @Inject
    TypeAheadSelectMovieBlockAction externalDataSource;

    @Inject
    TypeAheadMovieTitleSuggestionsAction movieTitleSuggestionsAction;

    @Inject
    SlackAppServer server;

    public static void main(String[] args) throws Exception
    {
        Injector injector = Guice.createInjector(new MovieInfoApplicationModule());
        MovieInfoApplicationService service = injector.getInstance(MovieInfoApplicationService.class);
        service.loadAllMoviesIntoCache();
        service.startSlackAppServer();
    }

    private void loadAllMoviesIntoCache()
    {
        cacheBuildComponent.buildMovieCacheAndRecommendations(true);
    }

    public void startSlackAppServer() throws Exception
    {
        initializeApp();
        server.start(); // http://localhost:3000/slack/events
    }

    private void initializeApp()
    {
        homePageOpenedEvent.homePageOpened();
        //Enable below commented Code for Static Movie Select
//        selectMovieBlockAction.selectMovieAction();
        externalSelectBlockAction.externalSelectAction();
        externalDataSource.selectMovieAction();
        movieTitleSuggestionsAction.movieSuggestions();
    }
}
