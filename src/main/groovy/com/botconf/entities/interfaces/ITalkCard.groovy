package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface ITalkCard {

    Long getPrimaryKey()
    String getName()
    String getSpeakerName()
    Date getStart()
    Date getEnd()
    String getTrack()
    List<String> getTags()

}