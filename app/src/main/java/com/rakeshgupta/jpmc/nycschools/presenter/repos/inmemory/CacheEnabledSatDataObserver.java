package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class CacheEnabledSatDataObserver
        extends DisposableSingleObserver<List<SatScore>> {

    @Override
    public void onSuccess(List<SatScore> satScores) {
        LocalInMemoryCache.INSTANCE.setSatCache(satScores);

        AppDatabase db = AppDatabase.Companion.getDatabase(NycSchoolsApp.getNycSchoolsAppContext());
        AppDatabase.Companion.getDatabaseWriteExecutor().execute(
                () -> db.satScoresDao().insertAll(satScores));
    }
}
