package com.rakeshgupta.jpmc.nycschools.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sat_scores")
data class SatScore (
    @PrimaryKey
    @SerializedName("dbn")
    @JvmField
    val dbn: String,

    @ColumnInfo(name = "num_of_sat_test_takers")
    @SerializedName("num_of_sat_test_takers")
    @JvmField
    val numOfTestTakers: String,

    @ColumnInfo(name = "sat_critical_reading_avg_score")
    @SerializedName("sat_critical_reading_avg_score")
    @JvmField
    val satCriticalReadingAvgScore: String,

    @ColumnInfo(name = "sat_math_avg_score")
    @SerializedName("sat_math_avg_score")
    @JvmField
    val satMathAvgScore: String,

    @ColumnInfo(name = "sat_writing_avg_score")
    @SerializedName("sat_writing_avg_score")
    @JvmField
    val satWritingAvgScore: String
    )