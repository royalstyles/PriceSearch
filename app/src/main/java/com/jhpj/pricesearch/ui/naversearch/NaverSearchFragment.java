package com.jhpj.pricesearch.ui.naversearch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jhpj.pricesearch.databinding.FragmentNaversearchBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverSearchFragment extends Fragment {

    private FragmentNaversearchBinding binding;
    private final String TAG = this.getClass().getSimpleName();

    private String client_id = "6dBc7nX6d71CVZMb0Dap";
    private String client_pw = "WgN0GCiRcJ";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NaverSearchViewModel naverSearchViewModel =
                new ViewModelProvider(this).get(NaverSearchViewModel.class);

        binding = FragmentNaversearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        naverSearchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        getResultSearch();

        return root;
    }

    void getResultSearch() {
        ApiInterface apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        Call<String> call = apiInterface.getSearchResult(client_id, client_pw, "book.json", "안드로이드");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    Log.e(TAG, "성공 : " + result);
                } else {
                    Log.e(TAG, "실패 : " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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