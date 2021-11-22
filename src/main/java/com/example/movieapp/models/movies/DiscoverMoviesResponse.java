package com.example.movieapp.models.movies;

import java.util.List;

public class DiscoverMoviesResponse {
    private int page;
    private List<MovieResponse> movies;
    private int totalResults;
    private int totalPages;

    public DiscoverMoviesResponse(int page, List<MovieResponse> movies, int totalResults, int totalPages) {
        this.page = page;
        this.movies = movies;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public List<MovieResponse> getMovies() {
        return movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "DiscoverMoviesResponse{" +
            "page=" + page +
            ", movies=" + movies +
            ", totalResults=" + totalResults +
            ", totalPages=" + totalPages +
            '}';
    }
}
