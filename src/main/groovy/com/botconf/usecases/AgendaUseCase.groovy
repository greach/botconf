package com.botconf.usecases
import android.content.Context
import com.botconf.R
import com.botconf.entities.AgendaSessionTimeHeader
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class AgendaUseCase {

    Context context
    String dateFormat = 'yyyy/MM/dd'

    AgendaUseCase(Context context) {
        this.context = context
        if(context) {
            dateFormat = context.getResources().getString(R.string.botconf_compact_date_format)
        }
    }

    List<IAgendaSession> buildAgendaWithAnchorDate(List<ITalkCard> talks, Date anchorDate) {
        def agenda = []

        talks.sort { a, b ->
            boolean chronologicalOrder = a.startDate.after(anchorDate) && b.startDate.after(anchorDate)
            int compareTo = (chronologicalOrder) ? a.startDate <=> b.startDate : b.startDate <=> a.startDate
            (compareTo == 0) ? a.name <=> b.name : compareTo
        }

        for(ITalkCard talk in talks) {

            def timeHeader = new AgendaSessionTimeHeader(sessionDate: talk.startDate,dateFormat: dateFormat)
            if(!agenda.any { it == timeHeader }) {
                agenda << timeHeader
            }
            agenda << talk
        }

        agenda
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    Date agendaAnchorDate() {
        def currentDate = new Date()
        use( TimeCategory ) {
            currentDate = currentDate - 9.hours
        }
        currentDate
    }

    List<IAgendaSession> buildAgenda(List<ITalkCard> talks) {
        buildAgendaWithAnchorDate(talks, agendaAnchorDate())
    }
}