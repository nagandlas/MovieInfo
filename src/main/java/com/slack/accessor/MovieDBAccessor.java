package com.slack.accessor;

import com.google.gson.Gson;
import com.slack.guice.MovieInfoApplicationModule.ApiEndPoint;
import com.slack.vo.MovieDetails;
import com.slack.vo.NewMovies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDBAccessor
{
    final Logger logger = LoggerFactory.getLogger(MovieDBAccessor.class);
    @Inject
    Gson gson;
    @Inject
    @ApiEndPoint
    private String apiEndPoint;

    public List<NewMovies> getMoviesThatArePlayingNow(boolean loadAllPages)
    {
        return getAllPages(loadAllPages);
    }

    private List<NewMovies> getAllPages(boolean loadAllPages)
    {
        //We can speed up this by parallelize calls with Executor Service
        int currentPage = 1;
        int totalPages = 5;
        List<NewMovies> newMovies = new ArrayList<>();
        try
        {
            do
            {
                String url = String.format(apiEndPoint, "now_playing") + "&page=" + currentPage;
                String response = invokeMoviesDB(url);
                NewMovies movies = convertResponseToMovies(response, NewMovies.class);
                newMovies.add(movies);
                //To control between Static Select vs ExternalSelect(TypeAhead)
                if (loadAllPages)
                {
                    totalPages = movies.getTotal_pages();
                }
            } while (totalPages != currentPage++);
        } catch (IOException e)
        {
            e.printStackTrace();
            logger.error("Exception occurred when getting all the Movies ", e);
        }
        return newMovies;
    }

    private String invokeMoviesDB(String url) throws IOException
    {
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream())))
        {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null)
            {
                response.append(line);
            }
            return response.toString();
        }
    }

    private <T> T convertResponseToMovies(String response, Class<T> classToConvertTo)
    {
        return gson.fromJson(response, classToConvertTo);
    }

    public MovieDetails getMovieDetails(String movieId)
    {
        try
        {
            String response = invokeMoviesDB(String.format(apiEndPoint, movieId));
            return convertResponseToMovies(response, MovieDetails.class);
        } catch (IOException e)
        {
            e.printStackTrace();
            logger.error("Exception occurred when getting Movie Details ", e);
        }
        return new MovieDetails();
    }
}
