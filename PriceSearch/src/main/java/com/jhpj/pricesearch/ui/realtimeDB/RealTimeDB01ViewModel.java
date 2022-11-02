package com.jhpj.pricesearch.ui.realtimeDB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RealTimeDB01ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RealTimeDB01ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is RealTimeDB fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}