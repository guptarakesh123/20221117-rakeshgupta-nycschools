package com.rakeshgupta.jpmc.nycschools.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

        mViewModel.asyncUpdateSatScores(school);
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

        if (school.schoolEmail != null && !school.schoolEmail.isEmpty()) {
            mBinding.sendEmailButton.setOnClickListener(ignored -> {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{school.schoolEmail});
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send email to school"));
            });
        }

        if (school.phoneNumber != null && !school.phoneNumber.isEmpty()) {
            mBinding.dialButton.setOnClickListener(ignored -> {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", school.phoneNumber, null)));
            });
        }

        if (school.latitude != null && !school.latitude.isEmpty()
                && school.longitude != null && !school.longitude.isEmpty()) {
            mBinding.locationButton.setOnClickListener(ignored -> {
                String address = school.latitude + "," + school.longitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:" + address + "?q="+address +"("+school.schoolName+")"));
                startActivity(intent);
            });
        }

        if (school.website != null && !school.website.isEmpty()) {
            mBinding.openWebsiteButton.setOnClickListener(ignored -> {
                final Uri uri = Uri.parse(school.website).buildUpon().scheme("https").build();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,uri);
                startActivity(intent);
            });
        }

    }
}