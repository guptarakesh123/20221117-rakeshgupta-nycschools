package com.rakeshgupta.jpmc.nycschools.presenter.repos.db;

import com.rakeshgupta.jpmc.nycschools.model.School;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class CacheEnabledSchoolDataObserver
        extends DisposableSingleObserver<List<School>> {

    @Override
    public void onSuccess(List<School> schools) {
        LocalInMemoryCache.INSTANCE.setSchoolsCache(schools);
    }
}
