package com.botconf.entities.agendafilters

import groovy.transform.CompileStatic

@CompileStatic
interface IAgendaFilterHeaderLocalization {

    String getAgendaFilterHeaderCongressString()
    String getAgendaFilterHeaderTrackString()
    String getAgendaFilterHeaderTagString()
}