package com.rakeshgupta.jpmc.nycschools.presenter.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory.CacheEnabledSatDataObserver;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory.LocalInMemoryCache;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.network.Repository;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.DefaultSchedulerProvider;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.SchedulerProvider;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class SatPageVM extends ViewModel {
    private School mDisplayedSchool;
    private MutableLiveData<SatScore> mDisplayedSatScore;

    private CompositeDisposable mDisposables;
    private Repository mRepository;
    private SchedulerProvider mSchedulerProvider;

    public SatPageVM() {
        mDisposables = new CompositeDisposable();
        mDisplayedSatScore = new MutableLiveData<>();
        mRepository = Repository.getRepository();
        mSchedulerProvider = new DefaultSchedulerProvider();
    }

    public void init(School s) {
        mDisposables.add(
                Single.just(1)
                        .subscribeOn(mSchedulerProvider.io())
                        .doOnSuccess(x->asyncUpdateSatScores(s))
                        .subscribe()
        );
    }

    private void asyncUpdateSatScores(School s) {
        mDisplayedSchool = s;
        mDisposables.add(
                mRepository.getSatResults(s.dbn)
                        .observeOn(mSchedulerProvider.ui())
                        .subscribeWith(new CacheEnabledSatDataObserver() {
                            @Override
                            public void onSuccess(List<SatScore> scores) {
                                super.onSuccess(scores);
                                SatScore satScore =
                                        LocalInMemoryCache.INSTANCE.getSatScoreFromCache(s.dbn);

                                if (satScore != null) mDisplayedSatScore.setValue(satScore);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("network", "failed to fetch SAT Scores from network");
                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.dispose();
    }

    public LiveData<SatScore> getDisplayedSatScoreSchools() {
        return mDisplayedSatScore;
    }

    public School getDisplayedSchool() {
        return mDisplayedSchool;
    }
}
