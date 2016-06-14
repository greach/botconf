package com.botconf.usecases

import com.botconf.entities.agendafilters.*
import groovy.transform.CompileStatic

@CompileStatic
class AgendaFilterUseCase {

    IAgendaFilterHeaderLocalization agendaFilterHeaderLocalization

    AgendaFilterUseCase(IAgendaFilterHeaderLocalization agendaFilterHeaderLocalization) {
        this.agendaFilterHeaderLocalization = agendaFilterHeaderLocalization
    }


    List<IAgendaFilter> fetchAgendaFilters() {
        List<IAgendaFilter> agendaFilters = []

        agendaFilters << new AgendaFilterHeader(name: agendaFilterHeaderLocalization.agendaFilterHeaderCongressString)

        congresses().each { agendaFilters << new AgendaFilterCongress(name: it)}

        agendaFilters << new AgendaFilterHeader(name: agendaFilterHeaderLocalization.agendaFilterHeaderTrackString)

        tracks().each { agendaFilters << new AgendaFilterTrack(name: it)}

        agendaFilters << new AgendaFilterHeader(name: agendaFilterHeaderLocalization.agendaFilterHeaderTagString)

        tags().each { agendaFilters << new AgendaFilterTag(name: it)}


        agendaFilters
    }

    List<String> congresses() {
        ['Greach 4.0']
    }

    List<String> tracks() {
        ['Track 1', 'Track 2']
    }

    List<String> tags() {
        ['Easy', 'Medium', 'Hard', 'Workshop']
    }
}