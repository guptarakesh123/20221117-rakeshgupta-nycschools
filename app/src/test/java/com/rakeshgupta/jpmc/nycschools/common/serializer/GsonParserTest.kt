package com.rakeshgupta.jpmc.nycschools.common.serializer

import com.rakeshgupta.jpmc.nycschools.model.School
import org.junit.Test

class GsonParserTest {
    @Test
    fun testSearch() {
        val school = School("1", "liberty", "some overview", "+1 23456",
            "a@b.com", "abc.xyz", "500", "45", "35", "no address", "nyc", "45678", "NY", "queens")
        val schoolStr = GsonParser.gson.toJson(school);

        val reverseSchool = GsonParser.gson.fromJson<School>(
            schoolStr, School::class.java
        )

        assert(school.equals(reverseSchool))
    }
}