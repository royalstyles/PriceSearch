package com.jhpj.pricesearch.ui.cloudstorage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhpj.pricesearch.databinding.FragmentCloudstorageBinding;

public class CloudStorageFragment extends Fragment {
    private FragmentCloudstorageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCloudstorageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}