package com.slack.cache;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class MovieCache
{
    private Map<String, String> movieMapping = new HashMap<>();

    public String getMovieId(String movieName)
    {
        return getMovieMapping().get(movieName);
    }

    public Map<String, String> getMovieMapping()
    {
        return movieMapping;
    }

    public void setMovieMapping(final Map<String, String> movieMapping)
    {
        this.movieMapping = movieMapping;
    }
}
