package com.example.android.davidek_popular_movies_stage1.data;


/**
 * Created by denis on 22.02.2017.
 */

public class Movie {

    private String originalTitle;
    private String posterUrl;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Movie(String originalTitle, String posterUrl, String plotSynopsis, double userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterUrl = posterUrl;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }
}
