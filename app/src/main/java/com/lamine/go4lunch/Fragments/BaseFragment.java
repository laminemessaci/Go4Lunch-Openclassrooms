package com.lamine.go4lunch.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.disposables.Disposable;


public abstract class BaseFragment extends Fragment {

    static final String ID = "ID";
    static final String RESTAURANT = "restaurant";
    Disposable disposable;

    // ----------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy() {
        if(this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
}
