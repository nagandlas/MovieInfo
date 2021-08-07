package com.slack.events;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ExternalSelectElement;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewTitle;
import com.slack.enums.MovieInfoConstants;
import com.slack.enums.ViewTypes;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.view.Views.view;

public class TypeAheadSelectMovieBlockAction
{
    private static final Integer MIN_QUERY_LENGTH = 1;
    @Inject
    App app;

    public void selectMovieAction()
    {
        app.blockAction(MovieInfoConstants.HOME_BUTTON_ACTION.name(), (req, ctx) ->
        {
            Logger logger = ctx.logger;
            try
            {
                ViewsOpenResponse result = ctx.client().viewsOpen(r -> r
                        // The token you used to initialize app
                        .token(ctx.getBotToken())
                        .triggerId(req.getPayload().getTriggerId())
                        .view(getModalView())
                );
                logger.info("result: {}", result);
            } catch (IOException | SlackApiException e)
            {
                logger.error("error: {}", e.getMessage(), e);
            }
            return ctx.ack();
        });
    }

    private View getModalView()
    {
        return view(v -> v
                .title(getViewTitle())
                .blocks(getModalLayoutBlocks())
                .type(ViewTypes.modal.name()));
    }

    private ViewTitle getViewTitle()
    {
        ViewTitle title = new ViewTitle();
        title.setText("Movie Info");
        title.setType("plain_text");
        return title;
    }

    private List<LayoutBlock> getModalLayoutBlocks()
    {
        return asBlocks(
                section(s -> s.text(markdownText(mt ->
                        mt.text("*Select A Movie:* ")))),
                actions(Collections.singletonList(getSelectElement())));
    }

    private ExternalSelectElement getSelectElement()
    {
        ExternalSelectElement externalSelectElement = new ExternalSelectElement();
        PlainTextObject plainTextObject = new PlainTextObject();
        plainTextObject.setText("Search Movie");
        externalSelectElement.setPlaceholder(plainTextObject);
        externalSelectElement.setActionId(MovieInfoConstants.TYPE_AHEAD_SELECT_MOVIE_ACTION.name());
        externalSelectElement.setMinQueryLength(MIN_QUERY_LENGTH);
        return externalSelectElement;
    }
}

