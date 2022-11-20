package com.rakeshgupta.jpmc.nycschools.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "schools")
data class School (
    @PrimaryKey
    @SerializedName("dbn")
    @JvmField
    val dbn: String,

    @ColumnInfo(name = "school_name")
    @SerializedName("school_name")
    @JvmField
    val schoolName: String,

    @ColumnInfo(name = "overview_paragraph")
    @SerializedName("overview_paragraph")
    @JvmField
    val overview: String,

    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    @JvmField
    val phoneNumber: String,

    @ColumnInfo(name = "school_email")
    @SerializedName("school_email")
    @JvmField
    val schoolEmail: String,

    @ColumnInfo(name = "website")
    @SerializedName("website")
    @JvmField
    val website: String,

    @ColumnInfo(name = "total_students")
    @SerializedName("total_students")
    @JvmField
    val totalStudents: String,

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @JvmField
    val latitude: String,

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @JvmField
    val longitude: String,

    @ColumnInfo(name = "primary_address_line_1")
    @SerializedName("primary_address_line_1")
    @JvmField
    val primaryAddress: String,

    @ColumnInfo(name = "city")
    @SerializedName("city")
    @JvmField
    val city: String,

    @ColumnInfo(name = "zip")
    @SerializedName("zip")
    @JvmField
    val zip: String,

    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @JvmField
    val stateCode: String,

    @ColumnInfo(name = "borough")
    @SerializedName("borough")
    @JvmField
    val borough: String
    )