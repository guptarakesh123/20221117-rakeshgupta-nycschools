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
public class CacheEnabledSchoolDataObserverTest {
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

        CacheEnabledSchoolDataObserver observer = new CacheEnabledSchoolDataObserver(repository) {
            @Override
            public void onError(Throwable e) {
                // ignore
            }
        };

        List<School> schools = new ArrayList<>();
        schools.add(new School("1", "liberty", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("2", "liberty2", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("3", "Sequoia", "", "", "", "", "", "", "", "", "", "", "", ""));

        observer.onSuccess(schools);
        verify(repository, times(1)).saveAllSchoolsInDb(schools);
    }
}