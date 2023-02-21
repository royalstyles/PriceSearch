package com.jhpj.pricesearch.ui.lotto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhpj.pricesearch.R;
import com.jhpj.pricesearch.databinding.FragmentCloudstorageBinding;
import com.jhpj.pricesearch.databinding.FragmentLottoBinding;

public class LottoFragment extends Fragment {

    private FragmentLottoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLottoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        return root;
    }
}