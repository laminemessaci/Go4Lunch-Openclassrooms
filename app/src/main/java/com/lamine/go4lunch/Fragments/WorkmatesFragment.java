package com.lamine.go4lunch.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.Models.Helper.UserHelper;
import com.lamine.go4lunch.R;
import com.lamine.go4lunch.Views.WorkmatesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends BaseFragment {

    @BindView(R.id.fragment_workmates_recyclerview) RecyclerView recyclerView;

    private List<User> userList;
    private WorkmatesAdapter workmatesAdapter;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        UserHelper.getCoWorkers(userList -> {
            WorkmatesFragment.this.userList = userList;
            workmatesAdapter.refreshAdapter(userList);
        });
        return view;
    }

    private void configureRecyclerView() {
        this.userList = new ArrayList<>();
        this.workmatesAdapter = new WorkmatesAdapter(userList);
        this.recyclerView.setAdapter(this.workmatesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}