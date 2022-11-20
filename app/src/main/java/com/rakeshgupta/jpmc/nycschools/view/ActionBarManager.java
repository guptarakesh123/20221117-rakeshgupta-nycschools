package com.rakeshgupta.jpmc.nycschools.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.rakeshgupta.jpmc.nycschools.model.School;

public class ActionBarManager {
    public void setupActionBar(
            Context context,
            School school,
            View sendEmailButton,
            View dialButton,
            View locationButton,
            View openWebsiteButton
    ) {
        if (school.schoolEmail != null && !school.schoolEmail.isEmpty()) {
            sendEmailButton.setOnClickListener(ignored -> {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{school.schoolEmail});
                intent.setType("text/html");
                context.startActivity(Intent.createChooser(intent, "Send email to school"));
            });
        }

        if (school.phoneNumber != null && !school.phoneNumber.isEmpty()) {
            dialButton.setOnClickListener(ignored ->
                context.startActivity(
                        new Intent(
                                Intent.ACTION_DIAL,
                                Uri.fromParts("tel", school.phoneNumber, null)
                        ))
            );
        }

        if (school.latitude != null && !school.latitude.isEmpty()
                && school.longitude != null && !school.longitude.isEmpty()) {
            locationButton.setOnClickListener(ignored -> {
                String address = school.latitude + "," + school.longitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:" + address + "?q="+address +"("+school.schoolName+")"));
                context.startActivity(intent);
            });
        }

        if (school.website != null && !school.website.isEmpty()) {
            openWebsiteButton.setOnClickListener(ignored -> {
                final Uri uri = Uri.parse(school.website).buildUpon().scheme("https").build();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,uri);
                context.startActivity(intent);
            });
        }
    }
}
