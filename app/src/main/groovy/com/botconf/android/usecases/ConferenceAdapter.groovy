package com.botconf.android.usecases

import com.botconf.entities.interfaces.IConference
import groovy.transform.CompileStatic

@CompileStatic
class ConferenceAdapter implements IConference {
    Long primaryKey
    String name
}
