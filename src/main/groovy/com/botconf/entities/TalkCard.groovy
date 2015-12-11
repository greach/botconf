package com.botconf.entities
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class TalkCard implements ITalkCard, IAgendaSession {

    Long primaryKey
    String name
    Date startDate
    Date endDate
    String trackName
    List<String> tags
}