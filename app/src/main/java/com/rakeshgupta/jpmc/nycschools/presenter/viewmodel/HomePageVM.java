package com.rakeshgupta.jpmc.nycschools.presenter.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory.CacheEnabledSchoolDataObserver;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.network.Repository;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.DefaultSchedulerProvider;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class HomePageVM extends ViewModel {
    private final MutableLiveData<List<School>> mDisplayedSchools;
    private final MutableLiveData<String> mSearchQuery;
    private final List<School> mAllSchools;
    private final CompositeDisposable mDisposables;
    private final Repository mRepository;
    private final SchedulerProvider mSchedulerProvider;

    public HomePageVM() {
        mDisposables = new CompositeDisposable();
        mDisplayedSchools = new MutableLiveData<>();
        mSearchQuery = new MutableLiveData<>();
        mAllSchools = new ArrayList<>();

        mRepository = Repository.getRepository();
        mSchedulerProvider = new DefaultSchedulerProvider();
        resetData();
    }

    public void resetData() {
        clearSearch();
        mDisposables.add(
            Single.just(1)
                .subscribeOn(mSchedulerProvider.io())
                .doOnSuccess(x->asyncUpdateAllSchools())
                .subscribe()
        );
    }

    private void asyncUpdateAllSchools() {
        mDisposables.add(
                mRepository.getAllSchools()
                        .observeOn(mSchedulerProvider.ui())
                        .subscribeWith(new CacheEnabledSchoolDataObserver() {
                            @Override
                            public void onSuccess(List<School> schools) {
                                super.onSuccess(schools);
                                mAllSchools.addAll(schools);
                                mDisplayedSchools.setValue(schools);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("network", "failed to fetch Schools from network");
                            }
                        }));
    }

    public void clearSearch() {
        mSearchQuery.setValue("");
        mDisplayedSchools.setValue(mAllSchools);
    }

    public void searchSchools(String query) {
        if (query == null) return;
        if (query.equals(mSearchQuery.getValue())) return;
        mSearchQuery.setValue(query);

        mDisplayedSchools.setValue(mAllSchools.stream()
                .filter(s -> s != null
                        && s.schoolName != null
                        && s.schoolName.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.dispose();
    }

    public LiveData<List<School>> getDisplayedSchools() {
        return mDisplayedSchools;
    }

    public LiveData<String> getSearchQuery() {
        return mSearchQuery;
    }
}
