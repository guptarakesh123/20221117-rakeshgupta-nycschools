package com.rakeshgupta.jpmc.nycschools.presenter.repos.network;

import android.content.Context;

import com.rakeshgupta.jpmc.nycschools.common.application.NycSchoolsApp;
import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory.LocalInMemoryCache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class Repository {
    private static volatile Repository INSTANCE;

    private Repository(){}

    public static Repository getRepository() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }

    protected SchoolsApi getSchoolApi() {
        return RetrofitClientInstance
                .getRetrofitInstance()
                .create(SchoolsApi.class);
    }

    public Single<List<School>> getAllSchools() {
        // check in local cache
        List<School> cache = LocalInMemoryCache.INSTANCE.getSchoolsCache();
        if (cache != null && !cache.isEmpty()) return Single.<List<School>>just(cache);

        //check in database
        AppDatabase db = AppDatabase.Companion.getDatabase(NycSchoolsApp.getNycSchoolsAppContext());
        List<School> dbList = db.schoolDao().getAll();
        if (dbList != null && !dbList.isEmpty()) return Single.<List<School>>just(dbList);

        // local cache is not built yet, db is empty, so make network call
        return getSchoolApi().getAllSchools();
    }

    public Single<List<SatScore>> getSatResults(String dbn) {
        // check in local cache
        if (LocalInMemoryCache.INSTANCE.hasSatCache()) {
            List<SatScore> cachedScores = new ArrayList<>(1);
            SatScore cache = LocalInMemoryCache.INSTANCE.getSatScoreFromCache(dbn);
            if (cache != null) cachedScores.add(cache);

            return Single.<List<SatScore>>just(cachedScores);
        }

        // check in database
        AppDatabase db = AppDatabase.Companion.getDatabase(NycSchoolsApp.getNycSchoolsAppContext());
        SatScore satScore = db.satScoresDao().get(dbn);
        List<SatScore> dbScores = new ArrayList<>(1);
        if (satScore != null) dbScores.add(satScore);
        if (satScore != null) return Single.<List<SatScore>>just(dbScores);

        // local cache not built yet, make a network call
        return getSchoolApi().getAllSatScores();
    }
}
