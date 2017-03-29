package com.botconf.android.usecases

import com.botconf.entities.interfaces.ISpeaker
import groovy.transform.CompileStatic

@CompileStatic
class SpeakerAdapter implements ISpeaker {
    Long primaryKey
    String name
    String about
    String imageUrl
    String twitter
}
