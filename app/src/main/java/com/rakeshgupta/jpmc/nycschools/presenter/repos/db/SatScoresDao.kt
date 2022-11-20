package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakeshgupta.jpmc.nycschools.model.SatScore

@Dao
interface SatScoresDao {
    @Query("SELECT * FROM sat_scores where dbn = :dbn")
    fun get(dbn : String): SatScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(satScores : List<SatScore>)

    @Query("DELETE FROM sat_scores")
    fun deleteAll()
}