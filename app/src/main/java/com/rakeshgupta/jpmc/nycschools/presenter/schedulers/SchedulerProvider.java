package com.rakeshgupta.jpmc.nycschools.presenter.schedulers;

import io.reactivex.Scheduler;

public interface SchedulerProvider {
    Scheduler io();
    Scheduler computation();
    Scheduler ui();
}
