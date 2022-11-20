package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakeshgupta.jpmc.nycschools.model.School

@Dao
interface SchoolDao {
    @Query("SELECT * FROM schools")
    fun getAll(): List<School>

    @Query("SELECT count(*) FROM schools")
    fun getCount(): Integer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(school : List<School>)

    @Query("DELETE FROM schools")
    fun deleteAll()
}