package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import com.rakeshgupta.jpmc.nycschools.model.SatScore
import com.rakeshgupta.jpmc.nycschools.model.School

object LocalInMemoryCache {
    private const val MAX_TIMESTAMP = 15 * 1000 * 60; // 15 minutes for cache expiry
    private val emptySchoolsCache = ArrayList<School>()
    private val emptySatScoreCache = ArrayList<SatScore>()

    private var schoolsCacheTimestamp: Long = 0
    private var satScoreCacheTimestamp: Long = 0

    var schoolsCache: List<School> = emptySchoolsCache
        get() {
            if (System.currentTimeMillis() > schoolsCacheTimestamp + MAX_TIMESTAMP)
                return emptySchoolsCache;
            return field;
        }
        set(value) {
            if (System.currentTimeMillis() > schoolsCacheTimestamp + MAX_TIMESTAMP) {
                schoolsCacheTimestamp = System.currentTimeMillis();
                field = value;
            }
        }

    var satScoreCache: List<SatScore> = emptySatScoreCache
        get() {
            if (System.currentTimeMillis() > satScoreCacheTimestamp + MAX_TIMESTAMP)
                return emptySatScoreCache;
            return field;
        }
        set(value) {
            if (field != null && field.isNotEmpty()) {
                satScoreCacheTimestamp = System.currentTimeMillis();
                field = value;
            }
        }
}