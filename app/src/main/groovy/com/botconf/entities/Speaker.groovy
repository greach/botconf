package com.botconf.entities
import com.botconf.entities.interfaces.ISpeaker
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Speaker implements ISpeaker {
    Long primaryKey
    String name
    String about
    String imageUrl
}