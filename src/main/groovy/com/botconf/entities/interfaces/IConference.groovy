package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface IConference {

    Long getPrimaryKey()
    String getName()
}