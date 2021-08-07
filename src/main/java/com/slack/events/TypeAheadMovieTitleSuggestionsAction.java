package com.slack.events;

import com.slack.api.app_backend.interactive_components.response.BlockSuggestionResponse;
import com.slack.api.app_backend.interactive_components.response.Option;
import com.slack.api.bolt.App;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.cache.MovieCache;
import com.slack.cache.Trie;
import com.slack.enums.MovieInfoConstants;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TypeAheadMovieTitleSuggestionsAction
{
    @Inject
    App app;
    @Inject
    Trie trie;
    @Inject
    MovieCache cache;

    public void movieSuggestions()
    {
        app.blockSuggestion(MovieInfoConstants.TYPE_AHEAD_SELECT_MOVIE_ACTION.name(), (req, ctx) ->
                ctx.ack(getSuggestions(req.getPayload().getValue())));
    }

    private BlockSuggestionResponse getSuggestions(String prefix)
    {
        BlockSuggestionResponse res = new BlockSuggestionResponse();
        List<Option> options = new ArrayList<>();
        List<String> suggestions = trie.getRecommendations(prefix);
        for (String movieName : suggestions)
        {
            Option option = new Option();
            PlainTextObject plainTextObject = new PlainTextObject();
            plainTextObject.setText(movieName);
            option.setText(plainTextObject);
            option.setValue(cache.getMovieId(movieName));
            if (cache.getMovieId(movieName) != null)
            {
                options.add(option);
            }
        }
        res.setOptions(options);
        return res;
    }
}
