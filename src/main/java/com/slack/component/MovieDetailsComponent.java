package com.slack.component;

import com.slack.accessor.MovieDBAccessor;
import com.slack.cache.Trie;
import com.slack.vo.MovieDetails;

import javax.inject.Inject;
import java.util.List;

public class MovieDetailsComponent
{
    @Inject
    MovieDBAccessor accessor;
    @Inject
    Trie trie;

    public MovieDetails getMovieDetails(String movieId)
    {
        return accessor.getMovieDetails(movieId);
    }

    public List<String> getSuggestions(String prefix)
    {
        return trie.getRecommendations(prefix);
    }
}
