package com.example.android.davidek_popular_movies_stage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.davidek_popular_movies_stage1.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by denis on 22.02.2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private ArrayList<Movie> movies;
    private Context mContext;
    private final MoviesAdapterOnClickHandler mClickListener;

    public interface MoviesAdapterOnClickHandler {
        void onClick(int position);
    }

    public MoviesAdapter(Context context, ArrayList<Movie> movies, MoviesAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.movies = movies;
        this.mClickListener = clickHandler;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView moviePosterImageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            moviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onClick(clickedPosition);

        }
    }


    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;

        View movieView = inflater.inflate(R.layout.list_item, parent, shouldAttachToRoot);
        MoviesViewHolder viewHolder = new MoviesViewHolder(movieView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {

        String movieImageUrl = movies.get(position).getPosterUrl();
        Picasso.with(mContext)
                .load(movieImageUrl)
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(holder.moviePosterImageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
