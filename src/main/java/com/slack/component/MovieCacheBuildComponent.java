package com.slack.component;

import com.slack.accessor.MovieDBAccessor;
import com.slack.cache.MovieCache;
import com.slack.cache.Trie;
import com.slack.vo.NewMovies;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieCacheBuildComponent
{
    @Inject
    MovieDBAccessor movieDBAccessor;
    @Inject
    MovieCache movieCache;
    @Inject
    Trie trie;

    public void buildMovieCacheAndRecommendations(boolean loadAllPages)
    {
        List<NewMovies> newMovies = movieDBAccessor.getMoviesThatArePlayingNow(loadAllPages);
        Map<String, String> mappings = createMovieMapFromTheList(newMovies);
        movieCache.setMovieMapping(mappings);
        trie.populateTrie(mappings.keySet());
    }

    private Map<String, String> createMovieMapFromTheList(List<NewMovies> movies)
    {
        Map<String, String> nowPlayingMovies = new HashMap<>();
        movies.forEach(result -> result.getResults()
                .forEach(m -> nowPlayingMovies.put(m.getTitle(), "" + m.getId())));
        return nowPlayingMovies;
    }
}
