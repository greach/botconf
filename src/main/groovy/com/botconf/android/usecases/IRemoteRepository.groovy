package com.botconf.android.usecases

import groovy.transform.CompileStatic;

@CompileStatic
interface IRemoteRepository {

    void loadConferenceData(Closure closure)
}