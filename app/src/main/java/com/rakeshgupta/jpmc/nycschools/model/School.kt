package com.rakeshgupta.jpmc.nycschools.model

import com.google.gson.annotations.SerializedName

data class School (
    @SerializedName("dbn")
    @JvmField
    val dbn: String,

    @SerializedName("school_name")
    @JvmField
    val schoolName: String,

    @SerializedName("overview_paragraph")
    @JvmField
    val overview: String,

    @SerializedName("phone_number")
    @JvmField
    val phoneNumber: String,

    @SerializedName("school_email")
    @JvmField
    val schoolEmail: String,

    @SerializedName("website")
    @JvmField
    val website: String,

    @SerializedName("total_students")
    @JvmField
    val totalStudents: String,

    @SerializedName("latitude")
    @JvmField
    val latitude: String,

    @SerializedName("longitude")
    @JvmField
    val longitude: String,

    @SerializedName("primary_address_line_1")
    @JvmField
    val primaryAddress: String,

    @SerializedName("city")
    @JvmField
    val city: String,

    @SerializedName("zip")
    @JvmField
    val zip: String,

    @SerializedName("state_code")
    @JvmField
    val stateCode: String,

    @SerializedName("borough")
    @JvmField
    val borough: String

    )