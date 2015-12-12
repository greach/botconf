package com.botconf.entities

import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Talk extends AbstractTalk implements ITalk {

    String about
    boolean favourite
    String slidesUrl
    String videoUrl
    List<ISpeaker> speakers



}