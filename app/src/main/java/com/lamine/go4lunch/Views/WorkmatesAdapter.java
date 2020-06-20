package com.lamine.go4lunch.Views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.R;

import java.util.List;

/**
 * Created by Lamine MESSACI on 17/06/2020.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private List<User> user;

    public WorkmatesAdapter(List<User> user) {
        this.user = user;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_workmates_item, viewGroup, false);
        return new WorkmatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder workmatesViewHolder, int i) {
        workmatesViewHolder.updateData(user.get(i));
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public void refreshAdapter(List<User> userList) {
        user = userList;
        notifyDataSetChanged();
    }
}
