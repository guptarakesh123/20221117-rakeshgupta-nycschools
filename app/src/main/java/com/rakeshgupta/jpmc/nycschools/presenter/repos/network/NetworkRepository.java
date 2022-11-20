package com.rakeshgupta.jpmc.nycschools.presenter.repos.network;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.LocalInMemoryCache;

import java.util.List;

import io.reactivex.Single;

public class NetworkRepository {

    protected SchoolsApi getSchoolApi() {
        return RetrofitClientInstance
                .getRetrofitInstance()
                .create(SchoolsApi.class);
    }

    public Single<List<School>> getAllSchools() {
        List<School> cache = LocalInMemoryCache.INSTANCE.getSchoolsCache();
        if (cache != null && !cache.isEmpty()) return Single.<List<School>>just(cache);

        return getSchoolApi().getAllSchools();
    }

    public Single<List<SatScore>> getAllSatResults() {
        List<SatScore> cache = LocalInMemoryCache.INSTANCE.getSatScoreCache();
        if (cache != null && !cache.isEmpty()) return Single.<List<SatScore>>just(cache);

        return getSchoolApi().getAllSatScores();
    }
}
