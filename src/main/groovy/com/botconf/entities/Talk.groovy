package com.botconf.entities

import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Talk implements ITalk {
    Long primaryKey
    String name
    String about
    boolean favourite
    Date start
    Date end
    String slidesUrl
    String videoUrl
    List<String> tags = []
    String track
    List<ISpeaker> speakers



}