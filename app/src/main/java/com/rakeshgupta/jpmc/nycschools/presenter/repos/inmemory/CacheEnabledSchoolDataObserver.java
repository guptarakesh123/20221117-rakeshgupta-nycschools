package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class CacheEnabledSchoolDataObserver
        extends DisposableSingleObserver<List<School>> {

    @Override
    public void onSuccess(List<School> schools) {
        LocalInMemoryCache.INSTANCE.setSchoolsCache(schools);

        AppDatabase db = AppDatabase.Companion.getDatabase(NycSchoolsApp.getNycSchoolsAppContext());
        AppDatabase.Companion.getDatabaseWriteExecutor().execute(
                () -> db.schoolDao().insertAll(schools));
    }
}
