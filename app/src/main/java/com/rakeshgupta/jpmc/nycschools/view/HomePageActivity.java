package com.rakeshgupta.jpmc.nycschools.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.rakeshgupta.jpmc.nycschools.R;
import com.rakeshgupta.jpmc.nycschools.databinding.ActivityHomePageBinding;
import com.rakeshgupta.jpmc.nycschools.presenter.viewmodel.HomePageVM;

public class HomePageActivity extends AppCompatActivity {
    private HomePageVM mViewModel;
    private ActivityHomePageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(HomePageVM.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        // default orientation is vertical
        mBinding.schoolList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.schoolList.setAdapter(new SchoolListAdapter(this, mViewModel));
        mBinding.schoolList.setHasFixedSize(true);

        mBinding.clearSearchButton.setOnClickListener(ignored -> mViewModel.clearSearch());
        mViewModel.getSearchQuery().observe(this, query -> handleSearchQuery(query));

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (query == null || query.isEmpty()) {
                mViewModel.clearSearch();
            } else {
                mViewModel.searchSchools(query);
            }

        } else {
            // nothing special is needed
        }
    }

    private void handleSearchQuery(String query) {
        if (query != null && !query.isEmpty()) {
            mBinding.searchInfoRow.setVisibility(View.VISIBLE);
            mBinding.searchQuery.setText(getString(R.string.searching) + query);
            mViewModel.searchSchools(query);
        } else {
            mBinding.searchInfoRow.setVisibility(View.GONE);
        }
    }
}