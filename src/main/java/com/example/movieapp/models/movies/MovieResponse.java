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
    private LocalDate releaseDate;
    private List<Integer> genreIds;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private double popularity;
    private int voteCount;
    private boolean hasVideo;
    private double voteAverage;

    public MovieResponse(int id, String posterPath, boolean isAdult, String overview, LocalDate releaseDate, List<Integer> genreIds, String originalTitle, String originalLanguage, String title, String backdropPath, double popularity, int voteCount, boolean hasVideo, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.hasVideo = hasVideo;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public double getPopularity() {
        return popularity;
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

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return hasVideo;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public static MovieResponse parse(JSONObject json) {
        int id = json.getInt("id");
        String posterPath = "";
        boolean isAdult = false;
        String overview = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = null;

        List<Integer> genreIds = new ArrayList<>();
        String originalTitle = "";
        String originalLanguage = "";
        String title = "";
        String backdropPath = "";
        double popularity = 0.0;
        int voteCount = 0;
        boolean hasVideo = false;
        double voteAverage = 0.0;

        try {
            posterPath = json.getString("poster_path");
            overview = json.getString("overview");
            isAdult = json.getBoolean("adult");
            releaseDate = LocalDate.parse(json.getString("release_date"), formatter);

            // for genres
            JSONArray array = json.getJSONArray("genre_ids");
            for (Object o : array) {
                genreIds.add((Integer) o);
            }

            originalTitle = json.getString("original_title");
            originalLanguage = json.getString("original_language");
            title = json.getString("title");
            backdropPath = json.getString("backdrop_path");
            popularity = json.getDouble("popularity");
            voteCount = json.getInt("vote_count");
            hasVideo = json.getBoolean("video");
            voteAverage = json.getDouble("vote_average");
        } catch (JSONException e) {
            System.out.println("missing field...");
        }

        return new MovieResponse(
            id,
            posterPath,
            isAdult,
            overview,
            releaseDate,
            genreIds,
            originalTitle,
            originalLanguage,
            title,
            backdropPath,
            popularity,
            voteCount,
            hasVideo,
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
            ", originalTitle='" + originalTitle + '\'' +
            ", originalLanguage='" + originalLanguage + '\'' +
            ", title='" + title + '\'' +
            ", backdropPath='" + backdropPath + '\'' +
            ", popularity=" + popularity +
            ", voteCount=" + voteCount +
            ", hasVideo=" + hasVideo +
            ", voteAverage=" + voteAverage +
            '}';
    }
}
