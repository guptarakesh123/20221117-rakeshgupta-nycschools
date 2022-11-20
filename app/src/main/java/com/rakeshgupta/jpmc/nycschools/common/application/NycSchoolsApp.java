package com.rakeshgupta.jpmc.nycschools.common.application;

import android.app.Application;
import android.content.Context;

public class NycSchoolsApp extends Application {
    private static Context sApplicationContext;

    public NycSchoolsApp() {
        sApplicationContext = this;
    }

    public static Context getNycSchoolsAppContext() {
        return sApplicationContext;
    }
}
