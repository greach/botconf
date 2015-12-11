package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface ITalk {

    Long getPrimaryKey()
    List<String> getTags()
    String getTrack()
    String getName()
    String getAbout()
    Date getStart()
    Date getEnd()
    String getSlidesUrl()
    String getVideoUrl()
    boolean isFavourite()

    List<ISpeaker> getSpeakers()

}