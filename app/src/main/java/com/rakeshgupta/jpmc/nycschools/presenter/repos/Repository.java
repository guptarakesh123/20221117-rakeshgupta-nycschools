package com.rakeshgupta.jpmc.nycschools.presenter.repos;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory.LocalInMemoryCache;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.network.RetrofitClientInstance;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.network.SchoolsApi;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.DefaultSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class Repository {
    private static volatile Repository INSTANCE;
    private AppDatabase mDb;

    private Repository(@NonNull Context context, @Nullable AppDatabase db) {
        if (db == null) {
            mDb = AppDatabase.Companion.getDatabase(context);
        } else {
            mDb = db;
        }
    }

    public static Repository getRepository(@NonNull Context context, @Nullable AppDatabase db) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(context, db);
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
        Log.d("repository", "Local cache lookup access - reading school records");
        List<School> cache = LocalInMemoryCache.INSTANCE.getSchoolsCache();
        if (cache != null && !cache.isEmpty()) return Single.<List<School>>just(cache);

        // check in database
        //TODO: implement cache expiry for database also
        Log.d("repository", "Database access - reading school records from sqlite");
        List<School> dbList = mDb.schoolDao().getAll();
        if (dbList != null && !dbList.isEmpty()) return Single.<List<School>>just(dbList);

        // local cache is not built yet, db is empty, so make network call
        Log.d("repository", "Network access - reading school records from internet");
        return getSchoolApi().getAllSchools();
    }

    /*
    while we only want one SatScore object, the api is not set up to return just one. So reading all
    as long as this object is not in local memory or in the database
     */
    public Single<List<SatScore>> getSatResults(String dbn) {
        // check in local cache
        Log.d("repository", "Local cache lookup access - reading sat records");
        if (LocalInMemoryCache.INSTANCE.hasSatCache()) {
            List<SatScore> cachedScores = new ArrayList<>(1);
            SatScore cache = LocalInMemoryCache.INSTANCE.getSatScoreFromCache(dbn);
            if (cache != null) {
                cachedScores.add(cache);
                return Single.<List<SatScore>>just(cachedScores);
            }
        }

        // check in database
        //TODO: implement cache expiry for database also
        Log.d("repository", "Database access - reading sat records from sqlite");
        SatScore satScore = mDb.satScoresDao().get(dbn);
        List<SatScore> dbScores = new ArrayList<>(1);
        if (satScore != null) dbScores.add(satScore);
        if (satScore != null && satScore.dbn != null && !satScore.dbn.isEmpty())
            return Single.<List<SatScore>>just(dbScores);

        // local cache not built yet, db is empty, make a network call
        Log.d("repository", "Network access - reading sat records from internet");
        return getSchoolApi().getAllSatScores();
    }

    public void saveAllSchoolsInDb(List<School> schools) {
        Single.just(1)
                .subscribeOn(new DefaultSchedulerProvider().io())
                .doOnSuccess(x->{
                    if (mDb.schoolDao().getCount() == 0) {
                        Log.d("repository",
                                "Database access - adding "
                                        + schools.size()
                                        + " school records to sqlite");
                        mDb.schoolDao().insertAll(schools);
                    }
                })
                .subscribe();
    }

    public void saveAllSatScoresInDb(List<SatScore> satScores) {
        Log.d("repository",
                "Database access - adding " + satScores.size() + " sat records to sqlite");
        Single.just(1)
                .subscribeOn(new DefaultSchedulerProvider().io())
                .doOnSuccess(x->mDb.satScoresDao().insertAll(satScores))
                .subscribe();
    }
}
