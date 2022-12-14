package com.jhpj.pricesearch.ui.movie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jhpj.pricesearch.R;
import com.jhpj.pricesearch.databinding.FragmentMovieBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private FragmentMovieBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private final String API_KEY = "bc7dc8d95109b9f51ad46cff5c60c14c";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        binding = FragmentMovieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMovie;
        movieViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = root.findViewById(R.id.movie_recylerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofitInterface();

        Log.d("retrofit", getDate());

        retrofitInterface.getBoxOffice(API_KEY, "20221003").enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result result = response.body();
                BoxOfficeResult boxOfficeResult = result.getBoxOfficeResult();
                Log.d("retrofit", "Data fetch success");
                mAdapter = new MovieAdapter(boxOfficeResult.getDailyBoxOfficeList());

                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onDestroyView();
        binding = null;
    }

    private String getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now - 4);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        return dateFormat.format(date);
    }
}