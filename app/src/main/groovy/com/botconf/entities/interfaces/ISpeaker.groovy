package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface ISpeaker {
    Long getPrimaryKey()
    String getName()
    String getAbout()
    String getImageUrl()
}