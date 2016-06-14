package com.botconf.entities

import groovy.transform.CompileStatic;

@CompileStatic
class AbstractTalk {

    Long primaryKey
    String name
    Date start
    Date end
    List<String> tags = []
    String track
}