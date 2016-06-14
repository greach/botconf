package com.botconf.android

import android.app.Application
import com.botconf.BotConfApplication
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
trait TraitGoogleAnalytics {

    abstract Application getApplication()
    abstract String screenName()

    void logScreen() {
        logScreen(screenName())
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    void logScreen(String name) {
        // Obtain the shared Tracker instance.
        BotConfApplication application = (BotConfApplication) getApplication()
        Tracker tracker = application.getDefaultTracker()
        tracker.screenName = name
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}