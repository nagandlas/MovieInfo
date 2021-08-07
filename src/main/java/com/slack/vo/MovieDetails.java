package com.slack.vo;

public class MovieDetails
{
    public String title;
    public String overview;
    public int id;
    public String release_date;
    public String poster_path;

    public String getPoster_path()
    {
        return poster_path;
    }

    public void setPoster_path(String poster_path)
    {
        this.poster_path = poster_path;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(final String overview)
    {
        this.overview = overview;
    }

    public int getId()
    {
        return id;
    }

    public void setId(final int id)
    {
        this.id = id;
    }

    public String getRelease_date()
    {
        return release_date;
    }

    public void setRelease_date(final String release_date)
    {
        this.release_date = release_date;
    }

    @Override
    public String toString()
    {
        return "MovieDetails{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", id=" + id +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                '}';
    }
}
