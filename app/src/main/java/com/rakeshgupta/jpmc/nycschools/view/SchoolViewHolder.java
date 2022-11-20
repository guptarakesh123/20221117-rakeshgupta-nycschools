package com.rakeshgupta.jpmc.nycschools.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rakeshgupta.jpmc.nycschools.R;
import com.rakeshgupta.jpmc.nycschools.common.serializer.GsonParser;
import com.rakeshgupta.jpmc.nycschools.model.School;

class SchoolViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private FragmentActivity mActivity;

    public SchoolViewHolder(@NonNull View itemView, FragmentActivity activity) {
        super(itemView);
        mActivity = activity;
        mView = itemView;
    }

    public void bind(School s) {
        String name = s.schoolName;
        TextView nameView = mView.findViewById(R.id.single_school);
        nameView.setText(name);
        mView.setOnClickListener(ignoredView -> {
            mActivity.startActivity(
                    new Intent(mActivity, DetailsActivity.class)
                            .putExtra("school", GsonParser.INSTANCE.gson.toJson(s))
            );
        });
    }
}
