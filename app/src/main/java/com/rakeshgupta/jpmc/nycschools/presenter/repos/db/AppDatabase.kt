package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rakeshgupta.jpmc.nycschools.model.SatScore
import com.rakeshgupta.jpmc.nycschools.model.School

@Database(entities = [School::class, SatScore::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun satScoresDao(): SatScoresDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java, "nyc_schools_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}