package com.rakeshgupta.jpmc.nycschools.common.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonParser {
    @JvmField
    val gson : Gson = GsonBuilder().create();
}