package com.example.android.davidek_popular_movies_stage1;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.davidek_popular_movies_stage1.data.Movie;
import com.example.android.davidek_popular_movies_stage1.utilities.LayoutUtils;
import com.example.android.davidek_popular_movies_stage1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;

    private static final String MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIES_IMAGE_SIZE = "w500/";
    private static final String MOVIES_POPULAR = "/movie/popular";
    private static final String MOVIES_TOP_RATED = "/movie/top_rated";

    private TextView errorInternetConnectionTextView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        moviesRecyclerView.setHasFixedSize(true);
        errorInternetConnectionTextView = (TextView) findViewById(R.id.tv_error_no_internet_connection);
        loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, LayoutUtils.calculateNoOfColumns(this));
        moviesRecyclerView.setLayoutManager(layoutManager);

        if (checkInternetConnection()) {
            new GetMoviesTask().execute(MOVIES_TOP_RATED);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItemId = item.getItemId();

        switch (selectedItemId) {
            case R.id.action_show_popular_movies:
                Toast.makeText(getApplicationContext(), getString(R.string.showing_popular_movies), Toast.LENGTH_SHORT).show();
                if (checkInternetConnection()) {
                    moviesRecyclerView.setAdapter(null);
                    new GetMoviesTask().execute(MOVIES_POPULAR);
                }  else
                    showErrorMessage();
                break;

            case R.id.action_show_top_rated_movies:
                Toast.makeText(getApplicationContext(), getString(R.string.showing_top_rated_movies), Toast.LENGTH_SHORT).show();
                if (checkInternetConnection()) {
                    moviesRecyclerView.setAdapter(null);
                    new GetMoviesTask().execute(MOVIES_TOP_RATED);
                }  else
                    showErrorMessage();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessage() {

        moviesRecyclerView.setVisibility(View.INVISIBLE);
        errorInternetConnectionTextView.setVisibility(View.VISIBLE);
    }

    public void showMovieDataView() {

        moviesRecyclerView.setVisibility(View.VISIBLE);
        errorInternetConnectionTextView.setVisibility(View.INVISIBLE);
    }

// code taken from: http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public boolean checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> implements MoviesAdapter.MoviesAdapterOnClickHandler {

        private ArrayList<Movie> movies = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            loadingProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            String movieFilter = params[0];

          final String apiKey = BuildConfig.API_KEY;
//            String apiKey = getString(R.string.api_key);
            URL moviesRequestUrl = NetworkUtils.buildUrl(apiKey, movieFilter);

            String responseJSONString = NetworkUtils.getResponseFromHttp(moviesRequestUrl);

            try {
                JSONObject parentJSONObject = new JSONObject(responseJSONString);
                JSONArray moviesArray = parentJSONObject.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {

                    JSONObject childMovieObject = moviesArray.getJSONObject(i);

                    String originalTitle = childMovieObject.getString("original_title");
                    String moviePosterUrl = MOVIES_IMAGE_BASE_URL + MOVIES_IMAGE_SIZE + childMovieObject.getString("poster_path");
                    String plotSynopsis = childMovieObject.getString("overview");
                    double userRating = childMovieObject.getDouble("vote_average");
                    String releaseDate = childMovieObject.getString("release_date");

                    movies.add(new Movie(originalTitle, moviePosterUrl, plotSynopsis, userRating, releaseDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            loadingProgressBar.setVisibility(View.INVISIBLE);
            if (!movies.isEmpty()) {
                showMovieDataView();
                moviesAdapter = new MoviesAdapter(getApplicationContext(), movies, this);
                moviesRecyclerView.setAdapter(moviesAdapter);
            } else {
                showErrorMessage();
            }
        }


        @Override
        public void onClick(int position) {

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("originalTitle", movies.get(position).getOriginalTitle());
            intent.putExtra("moviePosterUrl", movies.get(position).getPosterUrl());
            intent.putExtra("plotSynopsis", movies.get(position).getPlotSynopsis());
            intent.putExtra("userRating", movies.get(position).getUserRating());
            intent.putExtra("releaseDate", movies.get(position).getReleaseDate());
            startActivity(intent);
        }
    }
}
