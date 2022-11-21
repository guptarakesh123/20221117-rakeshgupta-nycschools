package com.rakeshgupta.jpmc.nycschools.presenter.repos.inmemory;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
public class CacheEnabledSatDataObserverTest {
    @Test
    public void testOnSuccess() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // mock the app database
        AppDatabase db = mock(AppDatabase.class);
        SatScoresDao satScoresDao = mock(SatScoresDao.class);
        SchoolDao schoolDao = mock(SchoolDao.class);
        when(db.satScoresDao()).thenReturn(satScoresDao);
        when(db.schoolDao()).thenReturn(schoolDao);

        // mock the repository
        Repository repository = spy(Repository.getRepository(context, db));

        CacheEnabledSatDataObserver observer = new CacheEnabledSatDataObserver(repository) {
            @Override
            public void onError(Throwable e) {
                // ignore
            }
        };

        List<SatScore> satScores = new ArrayList<>();
        satScores.add(new SatScore("1", "", "", "", ""));
        satScores.add(new SatScore("2", "", "", "", ""));
        satScores.add(new SatScore("3", "", "", "", ""));

        observer.onSuccess(satScores);
        verify(repository, times(1)).saveAllSatScoresInDb(satScores);
    }
}