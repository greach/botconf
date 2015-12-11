package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface ITalkCard {

    Long getPrimaryKey()
    String getName()
    Date getStartDate()
    Date getEndDate()
    String getTrackName()
    List<String> getTags()

}