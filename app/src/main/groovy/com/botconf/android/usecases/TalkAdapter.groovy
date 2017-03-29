package com.botconf.android.usecases

import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import groovy.transform.CompileStatic

@CompileStatic
class TalkAdapter implements ITalk {
    Long primaryKey
    List<String> tags
    String track
    String name
    Date start
    Date end
    String about
    String slidesUrl
    String videoUrl
    boolean favourite
    List<ISpeaker> speakers

    @Override
    boolean isFavourite() {
        favourite
    }
}
