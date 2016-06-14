package com.botconf.entities.agendafilters

import groovy.transform.CompileStatic

@CompileStatic
abstract class AgendaFilter implements IAgendaFilter {
    String name
}