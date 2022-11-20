package com.rakeshgupta.jpmc.nycschools.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rakeshgupta.jpmc.nycschools.R;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.viewmodel.HomePageVM;

import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolViewHolder> {
    private FragmentActivity mActivity;
    private HomePageVM mViewModel;

    public SchoolListAdapter(FragmentActivity activity, HomePageVM vm) {
        mActivity = activity;
        mViewModel = vm;

        mViewModel.getDisplayedSchools().observe(mActivity,
                schools -> onSchoolsDataChange(schools));
    }

    private void onSchoolsDataChange(List<School> schools) {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mActivity.getLayoutInflater().inflate(
                R.layout.home_page_single_school,
                parent,
                false);

        return new SchoolViewHolder(itemView, mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        School school = mViewModel.getDisplayedSchools().getValue().get(position);
        holder.bind(school);
    }

    @Override
    public int getItemCount() {
        return mViewModel.getDisplayedSchools().getValue().size();
    }
}