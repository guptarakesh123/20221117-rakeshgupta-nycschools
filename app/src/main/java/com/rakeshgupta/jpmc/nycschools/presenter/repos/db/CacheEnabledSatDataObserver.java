package com.rakeshgupta.jpmc.nycschools.presenter.repos.db;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class CacheEnabledSatDataObserver
        extends DisposableSingleObserver<List<SatScore>> {

    @Override
    public void onSuccess(List<SatScore> satScores) {
        LocalInMemoryCache.INSTANCE.setSatScoreCache(satScores);
    }
}
