package com.botconf.entities
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class TalkCard extends AbstractTalk implements ITalkCard, IAgendaSession {

    String speakerName

}