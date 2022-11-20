package com.rakeshgupta.jpmc.nycschools.model

import com.google.gson.annotations.SerializedName

data class SatScore (
    @SerializedName("dbn")
    @JvmField
    val dbn: String,

    @SerializedName("num_of_sat_test_takers")
    @JvmField
    val numOfTestTakers: String,

    @SerializedName("sat_critical_reading_avg_score")
    @JvmField
    val satCriticalReadingAvgScore: String,

    @SerializedName("sat_math_avg_score")
    @JvmField
    val satMathAvgScore: String,

    @SerializedName("sat_writing_avg_score")
    @JvmField
    val satWritingAvgScore: String
    )