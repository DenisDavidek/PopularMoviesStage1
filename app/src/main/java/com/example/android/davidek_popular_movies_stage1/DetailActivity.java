package com.example.android.davidek_popular_movies_stage1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView moviePlotSynopsisTextView;
    private TextView movieUserRatingTextView;
    private TextView movieReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        moviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        moviePlotSynopsisTextView = (TextView) findViewById(R.id.tv_movie_plot_synopsis);
        movieUserRatingTextView = (TextView) findViewById(R.id.tv_movie_user_rating);
        movieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);

        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();

        if ((intent.hasExtra("originalTitle")) || intent.hasExtra("moviePosterUrl") || (intent.hasExtra("plotSynopsis")) || (intent.hasExtra("userRating")) || (intent.hasExtra("releaseDate"))) {

            String originalTitle = intent.getStringExtra("originalTitle");
            String posterUrl = intent.getStringExtra("moviePosterUrl");
            String plotSynopsis = intent.getStringExtra("plotSynopsis");
            double userRating = intent.getDoubleExtra("userRating", 0);
            String releaseDate = intent.getStringExtra("releaseDate");

            actionBar.setTitle(originalTitle);

            movieTitleTextView.setText(originalTitle);
            Picasso.with(this).load(posterUrl).into(moviePosterImageView);
            moviePlotSynopsisTextView.setText(plotSynopsis);
            movieUserRatingTextView.setText(String.valueOf(userRating));
            movieReleaseDateTextView.setText(releaseDate);
        }

    }
}
