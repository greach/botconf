package com.botconf.entities.interfaces

import com.botconf.entities.IAbstractTalk
import groovy.transform.CompileStatic

@CompileStatic
interface ITalk extends IAbstractTalk {

    String getAbout()
    String getSlidesUrl()
    String getVideoUrl()
    boolean isFavourite()
    List<ISpeaker> getSpeakers()

}