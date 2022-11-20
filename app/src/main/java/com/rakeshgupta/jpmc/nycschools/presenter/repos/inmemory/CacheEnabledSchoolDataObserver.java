package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.Repository;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

/*
this class will record the output of the network into the database and also in the in memory cache
 */
public abstract class CacheEnabledSchoolDataObserver
        extends DisposableSingleObserver<List<School>> {
    private Repository mRepository;
    public CacheEnabledSchoolDataObserver (Repository r) {
        super();
        mRepository = r;
    }
    @Override
    public void onSuccess(List<School> schools) {
        LocalInMemoryCache.INSTANCE.setSchoolsCache(schools);
        mRepository.saveAllSchoolsInDb(schools);
    }
}
