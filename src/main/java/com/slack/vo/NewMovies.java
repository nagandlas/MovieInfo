package com.slack.vo;

import java.util.List;

public class NewMovies
{
    public int total_pages;
    private int page;
    private List<MovieMetaInfo> results;

    public List<MovieMetaInfo> getResults()
    {
        return results;
    }

    public void setResults(final List<MovieMetaInfo> results)
    {
        this.results = results;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(final int page)
    {
        this.page = page;
    }

    public int getTotal_pages()
    {
        return total_pages;
    }

    public void setTotal_pages(final int total_pages)
    {
        this.total_pages = total_pages;
    }

    @Override
    public String toString()
    {
        return "NewMovies{" +
                "page=" + page +
                ", total_pages=" + total_pages +
                ", movies=" + results +
                '}';
    }
}
