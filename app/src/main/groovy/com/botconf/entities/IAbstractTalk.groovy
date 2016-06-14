package com.botconf.entities

import groovy.transform.CompileStatic;

@CompileStatic
interface IAbstractTalk {

    Long getPrimaryKey()
    List<String> getTags()
    String getTrack()
    String getName()
    Date getStart()
    Date getEnd()
}