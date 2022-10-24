package com.jhpj.pricesearch.ui.naversearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jhpj.pricesearch.R;

import java.util.List;

public class NaverSearchAdapter extends RecyclerView.Adapter<NaverSearchAdapter.ViewHolder> {
    private List<SearchDataList> datas;
    private Activity activity;

    public NaverSearchAdapter(List<SearchDataList> data) {
        this.datas = data;
    }

    @NonNull
    @Override
    public NaverSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_naversearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NaverSearchAdapter.ViewHolder holder, int position) {
        SearchDataList data = datas.get(position);
        holder.setItem(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
        }

        public void setItem(SearchDataList data) {
            Glide.with(image.getContext()).load(data.getImage()).into(image);
            title.setText(data.getTitle());
        }
    }
}