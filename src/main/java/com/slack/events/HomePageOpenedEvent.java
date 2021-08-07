package com.slack.events;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
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

public class HomePageOpenedEvent
{
    @Inject
    App app;

    public void homePageOpened()
    {
        app.event(AppHomeOpenedEvent.class, (req, ctx) ->
        {
            Logger logger = ctx.logger;
            String userId = req.getEvent().getUser();
            try
            {
                ViewsPublishResponse result = ctx.client().viewsPublish(r -> r
                        // The token you used to initialize app
                        .token(ctx.getBotToken())
                        .userId(userId)
                        .view(getHomeView())
                );
                logger.info("result: {}", result);
            } catch (IOException | SlackApiException e)
            {
                logger.error("error: {}", e.getMessage(), e);
            }
            return ctx.ack();
        });
    }

    private View getHomeView()
    {
        return view(v -> v
                .type(ViewTypes.home.name())
                .blocks(getHomePageLayoutBlocks()));
    }

    private List<LayoutBlock> getHomePageLayoutBlocks()
    {
        return asBlocks(
                section(s -> s.text(markdownText(mt ->
                        mt.text("*Welcome to Movie Info!* :tada:")))),
                section(s -> s.text(markdownText(mt ->
                        mt.text("Click the button below to pick a movie!")))),
                actions(Collections.singletonList(getSelectMovieButtonElement())));
    }

    private ButtonElement getSelectMovieButtonElement()
    {
        ButtonElement buttonElement = new ButtonElement();
        PlainTextObject plainTextObject = new PlainTextObject();
        plainTextObject.setText("Select A Movie!");
        buttonElement.setActionId(MovieInfoConstants.HOME_BUTTON_ACTION.name());
        buttonElement.setText(plainTextObject);
        return buttonElement;
    }
}
