package com.slack.events;

import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.Context;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.element.ImageElement;
import com.slack.component.MovieDetailsComponent;
import com.slack.enums.MovieInfoConstants;
import com.slack.guice.MovieInfoApplicationModule.PosterPath;
import com.slack.vo.MovieDetails;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

public class PostChatMessageAction
{
    @Inject
    MovieDetailsComponent movieDetailsActivity;
    @Inject
    App app;

    @Inject
    @PosterPath
    private String posterPathPrefix;

    public void externalSelectAction()
    {
        app.blockAction(MovieInfoConstants.TYPE_AHEAD_SELECT_MOVIE_ACTION.name(), (req, ctx) ->
        {
            Logger logger = ctx.logger;
            try
            {
                ChatPostMessageResponse result = postChatMessageThroughContext(req, ctx);
                logger.info("Chat Post Message: {}", result);
//                System.out.println("Chat Post Message: {}" + result);
            } catch (IOException | SlackApiException e)
            {
                logger.error("error: {}", e.getMessage(), e);
            }
            return ctx.ack();
        });
    }

    private ChatPostMessageResponse postChatMessageThroughContext(BlockActionRequest req, Context ctx) throws
                                                                                                       IOException,
                                                                                                       SlackApiException
    {
        return ctx.client().chatPostMessage(r -> r
                // The token you used to initialize your app
                .token(ctx.getBotToken())
                //https://api.slack.com/methods/chat.postMessage/test
                .channel(req.getPayload().getUser().getId())
                .username(req.getPayload().getUser().getUsername())
                .blocks(getHomePageLayoutBlocks(req)));
    }

    private List<LayoutBlock> getHomePageLayoutBlocks(BlockActionRequest req)
    {
        String movieId = req.getPayload().getActions().get(0).getSelectedOption().getValue();
        String movieName = req.getPayload().getActions().get(0).getSelectedOption().getText().getText();
        MovieDetails movieDetails = movieDetailsActivity.getMovieDetails(movieId);
        return asBlocks(
                section(s -> s.text(markdownText(mt ->
                        mt.text("Here's the movie info you requested!")))),
                section(s -> s.text(markdownText(mt ->
                        mt.text("*" + movieName + "*")))),
                section(s -> s.text(markdownText(mt ->
                        mt.text("Release Data: " + movieDetails.getRelease_date())))),
                section(s ->
                {
                    s.text(markdownText(mt ->
                            mt.text(movieDetails.getOverview())));
                    ImageElement b = new ImageElement();
                    b.setAltText(movieName);
                    b.setImageUrl(posterPathPrefix + movieDetails.getPoster_path());
                    s.accessory(b);
                    return s;
                }),
                divider()
        );
    }

    private ChatPostMessageResponse postChatMessageThroughMethodsApi(BlockActionRequest req, Context ctx) throws
                                                                                                          IOException,
                                                                                                          SlackApiException
    {
        MethodsClient client = Slack.getInstance().methods();
        return client.chatPostMessage(r -> r
                // The token you used to initialize your app
                .token(ctx.getBotToken())
                .channel(req.getPayload().getUser().getId())
                .username(req.getPayload().getUser().getUsername())
                .blocks(getHomePageLayoutBlocks(req)));
    }
}
