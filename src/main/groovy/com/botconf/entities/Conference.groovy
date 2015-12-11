package com.botconf.entities
import com.botconf.entities.interfaces.IConference
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Conference implements IConference {
    Long primaryKey
    String name
}