package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.Repository;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.SatScoresDao;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.SchoolDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class LocalInMemoryCacheTest {
    @Test
    public void testSchoolsCache() {

        List<School> schools = new ArrayList<>();
        schools.add(new School("1", "liberty", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("2", "liberty2", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("3", "Sequoia", "", "", "", "", "", "", "", "", "", "", "", ""));

        LocalInMemoryCache.INSTANCE.setSchoolsCache(schools);

        assert(LocalInMemoryCache.INSTANCE.getSchoolsCache()).equals(schools);

    }

    @Test
    public void testSatCache() {

        List<SatScore> satScores = new ArrayList<>();
        satScores.add(new SatScore("1", "", "", "", ""));
        satScores.add(new SatScore("2", "", "", "", ""));
        satScores.add(new SatScore("3", "", "", "", ""));

        LocalInMemoryCache.INSTANCE.setSatCache(satScores);

        assert(LocalInMemoryCache.INSTANCE.getSatScoreFromCache("1")).equals(satScores.get(0));
        assert(LocalInMemoryCache.INSTANCE.getSatScoreFromCache("2")).equals(satScores.get(1));
        assert(LocalInMemoryCache.INSTANCE.getSatScoreFromCache("3")).equals(satScores.get(2));
        assert(LocalInMemoryCache.INSTANCE.getSatScoreFromCache("4") == null);

    }
}