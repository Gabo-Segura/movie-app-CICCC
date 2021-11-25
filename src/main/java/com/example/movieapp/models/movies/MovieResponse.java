package com.example.movieapp.models.movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MovieResponse {
    private int id;
    private String posterPath;
    private boolean isAdult;
    private String overview;
    private String releaseDate;
    private List<Integer> genreIds;
    private String title;
    private String backdropPath;
    private double voteAverage;

    public MovieResponse(){}

    public MovieResponse(int id, String posterPath, boolean isAdult, String overview, String releaseDate, List<Integer> genreIds, String title, String backdropPath, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public static MovieResponse parse(JSONObject json) throws JSONException {
        int id = json.getInt("id");

        String posterPath = "";
        try {
            posterPath = json.getString("poster_path");
        } catch (JSONException e) {
            System.out.println("missing poster path");
        }

        boolean isAdult = false;
        try {
            isAdult = json.getBoolean("adult");
        } catch (JSONException e) {
            System.out.println("missing isAdult");
        }

        String overview = "";
        try {
            overview = json.getString("overview");
        } catch (JSONException e) {
            System.out.println("missing overview");
        }

        String releaseDate = "";
        try {
            releaseDate = json.getString("release_date");
        } catch (JSONException e) {
            System.out.println("missing release date");
        }

        List<Integer> genreIds = new ArrayList<>();
        try {
            JSONArray array = json.getJSONArray("genre_ids");
            for (Object o : array) {
                genreIds.add((Integer) o);
            }
        } catch (JSONException e) {
            System.out.println("missing genre ids");
        }

        String title = "";
        try {
            title = json.getString("title");
        } catch (JSONException e) {
            System.out.println("missing title");
        }

        String backdropPath = "";
        try {
            backdropPath = json.getString("backdrop_path");
        } catch (JSONException e) {
            System.out.println("missing backdrop path");
        }

        double voteAverage = 0.0;
        try {
            voteAverage = json.getDouble("vote_average");
        } catch (JSONException e) {
            System.out.println("missing vote average");
        }

        return new MovieResponse(
            id,
            posterPath,
            isAdult,
            overview,
            releaseDate,
            genreIds,
            title,
            backdropPath,
            voteAverage
        );
    }

    public static List<MovieResponse> parseMovies(JSONArray results) {
        List<MovieResponse> movies = new ArrayList<>();
        for (Object result : results) {
            JSONObject json = (JSONObject) result;

            MovieResponse newMovie = MovieResponse.parse(json);
            movies.add(newMovie);
        }
        return movies;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
            "id=" + id +
            ", posterPath='" + posterPath + '\'' +
            ", isAdult=" + isAdult +
            ", overview='" + overview + '\'' +
            ", releaseDate=" + releaseDate +
            ", genreIds=" + genreIds +
            ", title='" + title + '\'' +
            ", backdropPath='" + backdropPath + '\'' +
            ", voteAverage=" + voteAverage +
            '}';
    }
}
