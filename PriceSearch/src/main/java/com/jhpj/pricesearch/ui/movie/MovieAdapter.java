package com.jhpj.pricesearch.ui.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhpj.pricesearch.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final List<DailyBoxOfficeList> items;

    public MovieAdapter(List<DailyBoxOfficeList> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        DailyBoxOfficeList item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView rank;
        private final TextView movieNm;
        private final TextView openDt;

        public ViewHolder(View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            movieNm = itemView.findViewById(R.id.movieNM);
            openDt = itemView.findViewById(R.id.openDt);
        }

        public void setItem(DailyBoxOfficeList item) {
            rank.setText(item.getRank());
            movieNm.setText(item.getMovieNm());
            openDt.setText("개봉일 : " + item.getOpenDt());
        }
    }
}
