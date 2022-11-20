package com.rakeshgupta.jpmc.nycschools.presenter.viewmodel;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.Repository;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.AppDatabase;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.SatScoresDao;
import com.rakeshgupta.jpmc.nycschools.presenter.repos.db.SchoolDao;
import com.rakeshgupta.jpmc.nycschools.presenter.schedulers.TrampolineSchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

@RunWith(RobolectricTestRunner.class)
public class SatPageVMTest {
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    private Repository repository;
    private AppDatabase db;
    private SchoolDao schoolDao;
    private SatScoresDao satScoresDao;
    private List<SatScore> satScores;
    private List<School> schools;

    @Before
    public void setUp() throws Exception {
        satScores = new ArrayList<>();
        satScores.add(new SatScore("1", "", "", "", ""));
        satScores.add(new SatScore("2", "", "", "", ""));
        satScores.add(new SatScore("3", "", "", "", ""));

        schools = new ArrayList<>();
        schools.add(new School("1", "liberty", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("2", "liberty2", "", "", "", "", "", "", "", "", "", "", "", ""));
        schools.add(new School("3", "Sequoia", "", "", "", "", "", "", "", "", "", "", "", ""));

        System.setProperty("javax.net.ssl.trustStore", "NONE");
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // mock the app database
        db = mock(AppDatabase.class);
        satScoresDao = mock(SatScoresDao.class);
        schoolDao = mock(SchoolDao.class);
        when(db.satScoresDao()).thenReturn(satScoresDao);
        when(db.schoolDao()).thenReturn(schoolDao);

        // mock the repository
        repository = spy(Repository.getRepository(context, db));
        when(repository.getAllSchools()).thenReturn(Single .<List<School>>just(schools));
        when(repository.getSatResults(any())).thenReturn(Single .<List<SatScore>>just(satScores));
    }

    @Test
    public void testAsyncUpdate() {
        SatPageVM vm = new SatPageVM(repository, new TrampolineSchedulerProvider());
        School s = schools.get(1);
        vm.init(s);
        assert (s.dbn.equals(vm.getDisplayedSatScoreSchools().getValue().dbn));

        s = schools.get(2);
        vm.init(s);
        assert (s.dbn.equals(vm.getDisplayedSatScoreSchools().getValue().dbn));

        School another = new School("4", "Sequoia2", "", "", "", "", "", "", "", "", "", "", "", "");
        assert (s.dbn.equals(vm.getDisplayedSatScoreSchools().getValue().dbn));
    }
}