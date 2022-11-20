package com.rakeshgupta.jpmc.nycschools.presenter.repos.network;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface SchoolsApi {
    @GET("/resource/s3k6-pzi2.json")
    Single<List<School>> getAllSchools();

    @GET("/resource/f9bf-2cp4.json")
    Single<List<SatScore>> getAllSatScores();
}
