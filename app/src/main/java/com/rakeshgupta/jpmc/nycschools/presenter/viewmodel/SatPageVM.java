package com.rakeshgupta.jpmc.nycschools.presenter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.CacheEnabledSatDataObserver;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.network.NetworkRepository;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.DefaultSchedulerProvider;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.SchedulerProvider;

import java.util.List;
import java.util.Optional;

import io.reactivex.disposables.CompositeDisposable;

public class SatPageVM extends ViewModel {
    private School mDisplayedSchool;
    private MutableLiveData<SatScore> mDisplayedSatScore;

    private CompositeDisposable mDisposables;
    private NetworkRepository mNetworkRepository;
    private SchedulerProvider mSchedulerProvider;

    public SatPageVM() {
        mDisposables = new CompositeDisposable();
        mDisplayedSatScore = new MutableLiveData<>();
        mNetworkRepository = new NetworkRepository();
        mSchedulerProvider = new DefaultSchedulerProvider();
    }

    public void asyncUpdateSatScores(School s) {
        mDisplayedSchool = s;
        mDisposables.add(
                mNetworkRepository.getAllSatResults()
                        .subscribeOn(mSchedulerProvider.io())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribeWith(new CacheEnabledSatDataObserver() {
                            @Override
                            public void onSuccess(List<SatScore> scores) {
                                super.onSuccess(scores);
                                Optional<SatScore> satScore = scores.stream().filter(
                                        score -> s != null
                                                && score.dbn != null
                                                && score.dbn.equals(s.dbn) ).findFirst();

                                if (satScore.isPresent()) mDisplayedSatScore.setValue(satScore.get());
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("sat score not loaded " + e);

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
