package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.DefaultSchedulerProvider;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

/*
this class will record the output of the network into the database and also in the in memory cache
 */
public abstract class CacheEnabledSatDataObserver
        extends DisposableSingleObserver<List<SatScore>> {

    @Override
    public void onSuccess(List<SatScore> satScores) {
        LocalInMemoryCache.INSTANCE.setSatCache(satScores);

        AppDatabase db = AppDatabase.Companion.getDatabase(NycSchoolsApp.getNycSchoolsAppContext());
        Single.just(1)
                .subscribeOn(new DefaultSchedulerProvider().io())
                .doOnSuccess(x->db.satScoresDao().insertAll(satScores))
                .subscribe();
    }
}
