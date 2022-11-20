package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory

import com.rakeshgupta.jpmc.nycschools.model.SatScore
import com.rakeshgupta.jpmc.nycschools.model.School
/*
purpose of this class is to prevent any network calls and any database calls.
Since the entire data needed is very small (total < 500 schools) , why not just cache them in memory
for performance gains. Total memory size would be only a couple of MBs
 */
object LocalInMemoryCache {
    private const val MAX_TIMESTAMP = 15 * 1000 * 60; // 15 minutes for cache expiry

    private val emptySchoolsCache = ArrayList<School>()
    private var schoolsCacheTimestamp: Long = 0  // last time the schools cache was updated

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

    private val satScoreCache: MutableMap<String, SatScore> = HashMap()
    private var satScoreCacheTimestamp: Long = 0  // last time the scores cache was updated
    fun hasSatCache () : Boolean {
        if (System.currentTimeMillis() > satScoreCacheTimestamp + MAX_TIMESTAMP) {
            satScoreCache.clear()
        }

        return satScoreCache.isNotEmpty()
    }

    fun getSatScoreFromCache (dbn : String) : SatScore? {
        if (System.currentTimeMillis() > satScoreCacheTimestamp + MAX_TIMESTAMP) {
            satScoreCache.clear()
        }

        return satScoreCache[dbn]
    }

    fun setSatCache (satList : List<SatScore>) {
        satList.forEach {
            it.dbn.also{_ -> satScoreCache[it.dbn] = it}
        }
        satScoreCacheTimestamp = System.currentTimeMillis();
    }
}