package com.rakeshgupta.jpmc.nycschools.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        TextView nameView = mView.findViewById(R.id.home_school_name);
        nameView.setText(s.schoolName);

        TextView numStudentsView = mView.findViewById(R.id.num_students);
        numStudentsView.setText(s.totalStudents);

        TextView borough = mView.findViewById(R.id.borough_name);
        borough.setText(s.borough);

        mView.setOnClickListener(ignoredView -> {
            mActivity.startActivity(
                    new Intent(mActivity, DetailsActivity.class)
                            .putExtra("school", GsonParser.INSTANCE.gson.toJson(s))
            );
        });

        new ActionBarManager().setupActionBar(
                mActivity, s,
                mView.findViewById(R.id.send_email_button),
                mView.findViewById(R.id.dial_button),
                mView.findViewById(R.id.location_button),
                mView.findViewById(R.id.open_website_button)
        );
    }
}
