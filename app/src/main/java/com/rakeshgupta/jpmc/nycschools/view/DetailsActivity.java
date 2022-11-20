package com.rakeshgupta.jpmc.nycschools.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.rakeshgupta.jpmc.nycschools.R;
import com.rakeshgupta.jpmc.nycschools.common.serializer.GsonParser;
import com.rakeshgupta.jpmc.nycschools.databinding.ActivityDetailsBinding;
import com.rakeshgupta.jpmc.nycschools.model.SatScore;
import com.rakeshgupta.jpmc.nycschools.model.School;
import com.rakeshgupta.jpmc.nycschools.presenter.viewmodel.SatPageVM;

public class DetailsActivity extends AppCompatActivity {
    private SatPageVM mViewModel;
    private ActivityDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SatPageVM.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        final School school = GsonParser.gson.fromJson(
                getIntent().getStringExtra("school"),School.class);

        mViewModel.getDisplayedSatScoreSchools().observe(this, ignored -> updateUi());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewModel.init(school);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private void updateUi() {
        final School school = mViewModel.getDisplayedSchool();
        final SatScore satScore = mViewModel.getDisplayedSatScoreSchools().getValue();
        if (school == null || satScore == null) return;

        mBinding.detailsSchoolName.setText(school.schoolName);
        mBinding.descriptionText.setText(school.overview);
        mBinding.criticalReadingScore.setText(satScore.satCriticalReadingAvgScore);
        mBinding.writingScore.setText(satScore.satWritingAvgScore);
        mBinding.mathScore.setText(satScore.satMathAvgScore);
        mBinding.address.setText(school.primaryAddress + " , " + school.city + " , " + school.stateCode + " - " + school.zip);
        mBinding.phone.setText(school.phoneNumber);
        mBinding.website.setText(school.website);

        new ActionBarManager().setupActionBar(
                this, school,
                mBinding.detailsContactActions.sendEmailButton,
                mBinding.detailsContactActions.dialButton,
                mBinding.detailsContactActions.locationButton,
                mBinding.detailsContactActions.openWebsiteButton
                );
    }
}