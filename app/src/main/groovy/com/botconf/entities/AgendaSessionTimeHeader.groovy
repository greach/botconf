package com.botconf.entities
import com.botconf.entities.interfaces.IAgendaSession
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeCheckingMode

@ToString
@EqualsAndHashCode(excludes = ['dateFormat'])
@CompileStatic
class AgendaSessionTimeHeader implements IAgendaSession {

    Date sessionDate

    String dateFormat = "yyyy/MM/dd"

    String getName() {

        String format = "EEEE HH:mm (${dateFormat})"

        if( sessionDate.after(nineHoursAgo()) && sessionDate.before(aWeekFromNow())) {
            format = "EEEE HH:mm"
        }
        sessionDate.format(format)
    }


    Date nineHoursAgo() {
        int noOfDays = -9
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date())
        calendar.add(Calendar.HOUR, noOfDays)
        calendar.getTime()
    }

    Date aWeekFromNow() {
        int noOfDays = 7
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date())
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays)
        calendar.getTime()
    }

}