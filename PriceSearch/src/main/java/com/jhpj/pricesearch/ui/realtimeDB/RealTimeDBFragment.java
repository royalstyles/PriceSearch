package com.jhpj.pricesearch.ui.realtimeDB;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhpj.pricesearch.databinding.FragmentRealtimedbBinding;

public class RealTimeDBFragment extends Fragment {

    private FragmentRealtimedbBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RealTimeDBViewModel realTimeDBViewModel =
                new ViewModelProvider(this).get(RealTimeDBViewModel.class);

        binding = FragmentRealtimedbBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtMainview;
        realTimeDBViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}