package com.jhpj.pricesearch.ui.naversearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NaverSearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NaverSearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is naversearch fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}