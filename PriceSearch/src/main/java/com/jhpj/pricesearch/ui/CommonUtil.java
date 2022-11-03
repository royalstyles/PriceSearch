package com.jhpj.pricesearch.ui;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

public class CommonUtil {
    public void hideKeyboard(Fragment fragment){
        if (fragment.getActivity() != null && fragment.getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(fragment.getActivity().getCurrentFocus().getWindowToken(), inputManager.HIDE_NOT_ALWAYS);
        }
    }
}
