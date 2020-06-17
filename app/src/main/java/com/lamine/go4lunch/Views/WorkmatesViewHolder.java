package com.lamine.go4lunch.Views;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lamine.go4lunch.Models.Helper.User;
import com.lamine.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lamine MESSACI on 17/06/2020.
 */
public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.workmates_name) TextView name;
    @BindView(R.id.workmates_picture) ImageView picture;

    WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    // Get the username and picture of the user in fireBase and display it
    @SuppressLint("NewApi")
    void updateData(User user) {
        if (user.getJoinedRestaurant() != null) {
            name.setText(itemView.getContext().getString(R.string.user_name, user.getUsername(), user.getJoinedRestaurant()));
            name.setTypeface(name.getTypeface(), Typeface.NORMAL);
            name.setTextColor(itemView.getContext().getColor(R.color.colorBlack));
            name.setAlpha((float) 1);

        } else {
            name.setText(itemView.getContext().getString(R.string.decided));
            name.setTypeface(name.getTypeface(), Typeface.ITALIC);
            name.setTextColor(itemView.getContext().getColor(R.color.colorGray));
            name.setAlpha((float) 0.5);
        }

        if (user.getUrlPicture() != null) {
            Glide.with(itemView)
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(picture);
        } else {
            Glide.with(itemView)
                    .load(R.drawable.nopicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(picture);
        }
    }
}

