package com.rakeshgupta.jpmc.nycschools.presenter.viewmodel;

import static org.mockito.Mockito.*;

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
public class HomePageVMTest {
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
    public void testSearch() {
        HomePageVM vm = new HomePageVM(repository, new TrampolineSchedulerProvider());
        assert (vm.getDisplayedSchools().getValue().size() == 3);

        vm.searchSchools("liber");
        assert (vm.getDisplayedSchools().getValue().size() == 2);
        assert ("liber".equals(vm.getSearchQuery().getValue()));

        vm.searchSchools("seq");
        assert (vm.getDisplayedSchools().getValue().size() == 1);
        assert ("seq".equals(vm.getSearchQuery().getValue()));

        vm.clearSearch();
        assert (vm.getDisplayedSchools().getValue().size() == 3);
    }

    @Test
    public void testAsyncUpdate() {
        HomePageVM vm = new HomePageVM(repository, new TrampolineSchedulerProvider());
        vm.clearSearch();
        assert (vm.getDisplayedSchools().getValue().size() == 3);

        schools.add(new School("4", "Sequoia2", "", "", "", "", "", "", "", "", "", "", "", ""));
        assert (vm.getDisplayedSchools().getValue().size() == 3);

        vm.resetData();
        vm.clearSearch();
        assert (vm.getDisplayedSchools().getValue().size() == 4);
    }
}