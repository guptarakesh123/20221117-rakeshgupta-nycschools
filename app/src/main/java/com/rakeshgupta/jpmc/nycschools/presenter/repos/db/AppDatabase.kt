package com.rakeshgupta.jpmc.nycschools.presenter.repos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rakeshgupta.jpmc.nycschools.model.SatScore
import com.rakeshgupta.jpmc.nycschools.model.School
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = [School::class, SatScore::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun satScoresDao(): SatScoresDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val NUMBER_OF_THREADS = 10
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

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