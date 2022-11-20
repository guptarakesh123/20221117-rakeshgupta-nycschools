package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.Repository;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

/*
this class will record the output of the network into the database and also in the in memory cache
 */
public abstract class CacheEnabledSatDataObserver
        extends DisposableSingleObserver<List<SatScore>> {

    private Repository mRepository;
    public CacheEnabledSatDataObserver(Repository r) {
        super();
        mRepository = r;
    }

    @Override
    public void onSuccess(List<SatScore> satScores) {
        LocalInMemoryCache.INSTANCE.setSatCache(satScores);
        if (satScores.size() > 1) mRepository.saveAllSatScoresInDb(satScores);
    }
}
