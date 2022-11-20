package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakeshgupta.jpmc.nycschools.model.SatScore
import com.rakeshgupta.jpmc.nycschools.model.School

@Dao
interface SatScoresDao {
    @Query("SELECT * FROM sat_scores where dbn = :dbn")
    fun get(dbn : String): SatScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(satScores : List<SatScore>)

    @Query("DELETE FROM sat_scores")
    fun deleteAll()
}