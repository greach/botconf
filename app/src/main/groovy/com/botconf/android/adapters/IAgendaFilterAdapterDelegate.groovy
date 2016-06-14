package com.botconf.android.adapters

import com.botconf.entities.agendafilters.IAgendaFilter
import groovy.transform.CompileStatic

@CompileStatic
interface IAgendaFilterAdapterDelegate {

    void agendaFilterClicked(IAgendaFilter agendaFilter)

}