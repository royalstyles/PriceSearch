package com.jhpj.pricesearch.ui.naversearch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jhpj.pricesearch.R;
import com.jhpj.pricesearch.databinding.FragmentNaversearchBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverSearchFragment extends Fragment {

    private FragmentNaversearchBinding binding;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.Adapter mAdapter;

    private ApiInterface apiInterface;

    private final String TAG = this.getClass().getSimpleName();

    private String client_id = "6dBc7nX6d71CVZMb0Dap";
    private String client_pw = "WgN0GCiRcJ";

    private Toast toast;

    // 1페이지에 10개씩 데이터를 불러온다
    int page = 1, limit = 10;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NaverSearchViewModel naverSearchViewModel =
                new ViewModelProvider(this).get(NaverSearchViewModel.class);

        binding = FragmentNaversearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        naverSearchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        SearchView searchView = binding.searchView;
        recyclerView = root.findViewById(R.id.naversearch_recylerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = root.findViewById(R.id.progress_bar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 키보드 내림
                searchView.clearFocus();

                // 검색 버튼이 눌러졌을 때 이벤트 처리
                toast = Toast.makeText(getContext(), "검색 처리됨 : " + query, Toast.LENGTH_SHORT);
                toast.show();
                getResultSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경되었을 때 이벤트 처리s
                return false;
            }
        });

        return root;
    }

    void getResultSearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getInstance().create(ApiInterface.class);
//        apiInterface.getSearchResult(client_id, client_pw, "book.json", query).enqueue(new Callback<SearchResult>() {
        apiInterface.getSearchResult(client_id, client_pw, "shop.json", query).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchResult result = response.body();
                    Log.d("retrofit", "Data fetch success");
                    mAdapter = new NaverSearchAdapter(result.getSearchDataList());
                    recyclerView.setAdapter(mAdapter);
                    progressBar.setVisibility(View.GONE);

//                    for (int i = 0; i < result.getSearchDataList().size(); i++) {
//                        Log.e(TAG, "성공 : " + result.getSearchDataList().get(i));
//                    }

                    Log.e(TAG, "성공 : " + result.getSearchDataList().toString());
                } else {
                    Log.e(TAG, "실패 : " + response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}