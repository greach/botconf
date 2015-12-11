import com.andrewreitz.spock.android.AndroidSpecification
import com.botconf.entities.AgendaSessionTimeHeader
import com.botconf.entities.TalkCard
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import com.botconf.usecases.AgendaUseCase
import spock.lang.Shared

class AgendaUseCaseSpec extends AndroidSpecification {

    @Shared
    def formatString = 'yyyy/MM/dd HH:mm'

    void "Given a talks set an agenda is built correctly"() {
        given:

        def talks = unsortedGreach2015Sessions()
        talks += unsortedGreach2016Sessions()
        AgendaUseCase useCase = new AgendaUseCase(null)

        def expected = sortedGreach2016Agenda()
        expected += sortedGreach2015Agenda()

        when:
        def agendaSessions = useCase.buildAgendaWithAnchorDate(talks, new Date().parse(formatString, '2015/12/12 09:00'))

        then:
        agendaSessions
        agendaSessions.size() == expected.size()
        for(int i = 0; i < agendaSessions.size();i++) {
            assert agendaSessions[i] == expected[i]
        }

    }

    List<ITalkCard> unsortedGreach2016Sessions() {
        def talks = []
        talks << new TalkCard(name: 'Second day Keynote', startDate: new Date().parse(formatString, '2016/04/09 09:00'), endDate: new Date().parse(formatString, '2016/04/09 10:45'))
        talks << new TalkCard(name: 'First day Keynote', startDate: new Date().parse(formatString, '2016/04/08 09:00'), endDate: new Date().parse(formatString, '2016/04/08 10:45'))
        talks
    }

    List<ITalkCard> unsortedGreach2015Sessions() {
        def talks = []
        talks << new TalkCard(name: 'Gpars dataflow', startDate: new Date().parse(formatString, '2015/04/11 14:00'), endDate: new Date().parse(formatString, '2015/04/11 15:45'))
        talks << new TalkCard(name: 'Stateless Authentication for microservices',startDate:new Date().parse(formatString,'2015/04/11 14:00'), endDate: new Date().parse(formatString, '2015/04/11 14:45'))
        talks << new TalkCard(name: 'Is Groovy better for Testing than Java?',startDate:new Date().parse(formatString, '2015/04/11 14:00'), endDate: new Date().parse(formatString,'2015/04/11 14:45'))
        talks << new TalkCard(name: 'No-Nonsense NOSQL',startDate:new Date().parse(formatString, '2015/04/11 12:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(name: 'Groovy Environment Manager (2015)',startDate:new Date().parse(formatString, '2015/04/11 12:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(name: 'Intrastructure automation with gradle and puppet',startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(name: 'Securing Ratpack', startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 12:00'))
        talks << new TalkCard(name: 'DSL&#8217;ING YOUR GROOVY',startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 12:00'))
        talks << new TalkCard(name: 'Groovy options for reactive programming',startDate:new Date().parse(formatString, '2015/04/11 10:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(name: 'Documentation Brought to Life: ASCIIDOCTOR',startDate:new Date().parse(formatString, '2015/04/11 10:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(name: 'Hands on Ratpack',startDate:new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(name: 'Cut your grails application to pieces & build feature plugins',startDate:  new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString, '2015/04/11 09:45'))
        talks << new TalkCard(name: 'Grooscript in Action',startDate:new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString,'2015/04/11 09:45'))
        talks << new TalkCard(name: 'Grails and Cassandra',startDate:new Date().parse(formatString, '2015/04/10 17:15'), endDate: new Date().parse(formatString,'2015/04/10 18:00'))
        talks << new TalkCard(name: 'The Groovy Ecosystem',startDate:new Date().parse(formatString, '2015/04/10 17:15'), endDate: new Date().parse(formatString,'2015/04/10 18:00'))
        talks << new TalkCard(name: 'Advanced Microservices Concerns',startDate:new Date().parse(formatString,'2015/04/10 16:00'), endDate: new Date().parse(formatString,'2015/04/10 16:45'))
        talks << new TalkCard(name: 'Decathlon Sportmeeting &#8211; Sports, a new Grails Discipline',startDate:new Date().parse(formatString,'2015/04/10 16:00'), endDate: new Date().parse(formatString,'2015/04/10 16:45'))
        talks << new TalkCard(name: 'GR8Workshops: A Guided discussion about Teaching and Diversity in the Groovy Community',startDate:  new Date().parse(formatString,'2015/04/10 15:00'), endDate: new Date().parse(formatString,'2015/04/10 15:45'))
        talks << new TalkCard(name: 'Getting Started with Groovy on Android',startDate:new Date().parse(formatString,'2015/04/10 14:00'), endDate: new Date().parse(formatString,'2015/04/10 15:45'))
        talks << new TalkCard(name: 'Hacking the Grails Spring Security 2.0 Plugin',startDate:new Date().parse(formatString,'2015/04/10 14:00'), endDate: new Date().parse(formatString,'2015/04/10 14:45'))
        talks << new TalkCard(name: 'GPars Remoting',startDate:new Date().parse(formatString,'2015/04/10 12:15'), endDate: new Date().parse(formatString,'2015/04/10 13:00'))
        talks << new TalkCard(name: 'Grails Goodness',startDate:new Date().parse(formatString,'2015/04/10 12:15'), endDate: new Date().parse(formatString,'2015/04/10 13:00'))
        talks << new TalkCard(name: 'Building android apps with Gradle',startDate:new Date().parse(formatString,'2015/04/10 11:15'), endDate: new Date().parse(formatString,'2015/04/10 12:00'))
        talks << new TalkCard(name: 'Introducing Workflow Architectures using grails',startDate:new Date().parse(formatString,'2015/04/10 10:00'), endDate: new Date().parse(formatString,'2015/04/10 10:45'))
        talks << new TalkCard(name: 'Groovy and Scala: Friends or Foes',startDate:new Date().parse(formatString, '2015/04/10 10:00'), endDate: new Date().parse(formatString, '2015/04/10 10:45'))
        talks << new TalkCard(name: 'Groovy Past and Future',startDate:new Date().parse(formatString,'2015/04/10 09:00'), endDate: new Date().parse(formatString,'2015/04/10 09:45'))
        talks << new TalkCard(name: 'Beyond Gradle 2.0',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString, '2015/04/11 15:45'))
        talks << new TalkCard(name: 'Little did he know &#8230;',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString,'2015/04/11 15:45'))
        talks << new TalkCard(name: 'Use Groovy &#038; Grails in your Spring Boot Projects Don&#8217;t be afraid!',startDate:new Date().parse(formatString,'2015/04/11 16:00'), endDate: new Date().parse(formatString, '2015/04/11 16:45'))
        talks << new TalkCard(name: 'Idiomatic Gradle Plugin Writing',startDate:new Date().parse(formatString,'2015/04/11 16:45'), endDate: new Date().parse(formatString,'2015/04/11 16:45'))
        talks << new TalkCard(name: 'Spock: Testing (In the) Enterprise',startDate:new Date().parse(formatString,'2015/04/11 16:00'), endDate: new Date().parse(formatString, '2015/04/11 18:00'))
        talks << new TalkCard(name: 'Debugging your Legacy',startDate:new Date().parse(formatString,'2015/04/11 17:15'), endDate: new Date().parse(formatString,'2015/04/11 18:00'))
        talks << new TalkCard(name: 'Groovy on the shell',startDate:new Date().parse(formatString,'2015/04/11 17:15'), endDate: new Date().parse(formatString,'2015/04/11 18:00'))
        talks << new TalkCard(name: 'AST &#8211; Groovy Transformers: More than meets the Eye!',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString, '2015/05/10 12:00'))
        talks
    }

    List<IAgendaSession> sortedGreach2016Agenda() {
        def expected = []

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2016/04/08 09:00'))
        expected << new TalkCard(name: 'First day Keynote', startDate: new Date().parse(formatString, '2016/04/08 09:00'), endDate: new Date().parse(formatString, '2016/04/08 10:45'))
        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2016/04/09 09:00'))
        expected << new TalkCard(name: 'Second day Keynote', startDate: new Date().parse(formatString, '2016/04/09 09:00'), endDate: new Date().parse(formatString, '2016/04/09 10:45'))

        expected
    }

    List<IAgendaSession> sortedGreach2015Agenda() {
        def expected = []


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 17:15'))
        expected << new TalkCard(name: 'Debugging your Legacy',startDate:new Date().parse(formatString,'2015/04/11 17:15'), endDate: new Date().parse(formatString,'2015/04/11 18:00'))
        expected << new TalkCard(name: 'Groovy on the shell',startDate:new Date().parse(formatString,'2015/04/11 17:15'), endDate: new Date().parse(formatString,'2015/04/11 18:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 16:45'))
        expected << new TalkCard(name: 'Idiomatic Gradle Plugin Writing',startDate:new Date().parse(formatString,'2015/04/11 16:45'), endDate: new Date().parse(formatString,'2015/04/11 16:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 16:00'))
        expected << new TalkCard(name: 'Spock: Testing (In the) Enterprise',startDate:new Date().parse(formatString,'2015/04/11 16:00'), endDate: new Date().parse(formatString, '2015/04/11 18:00'))
        expected << new TalkCard(name: 'Use Groovy &#038; Grails in your Spring Boot Projects Don&#8217;t be afraid!',startDate:new Date().parse(formatString,'2015/04/11 16:00'), endDate: new Date().parse(formatString, '2015/04/11 16:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 15:00'))
        expected << new TalkCard(name: 'AST &#8211; Groovy Transformers: More than meets the Eye!',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString, '2015/05/10 12:00'))
        expected << new TalkCard(name: 'Beyond Gradle 2.0',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString, '2015/04/11 15:45'))
        expected << new TalkCard(name: 'Little did he know &#8230;',startDate:new Date().parse(formatString,'2015/04/11 15:00'), endDate: new Date().parse(formatString,'2015/04/11 15:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 14:00'))
        expected << new TalkCard(name: 'Gpars dataflow', startDate: new Date().parse(formatString, '2015/04/11 14:00'), endDate: new Date().parse(formatString, '2015/04/11 15:45'))
        expected << new TalkCard(name: 'Is Groovy better for Testing than Java?',startDate:new Date().parse(formatString, '2015/04/11 14:00'), endDate: new Date().parse(formatString,'2015/04/11 14:45'))
        expected << new TalkCard(name: 'Stateless Authentication for microservices',startDate:new Date().parse(formatString,'2015/04/11 14:00'), endDate: new Date().parse(formatString, '2015/04/11 14:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 12:15'))
        expected << new TalkCard(name: 'Groovy Environment Manager (2015)',startDate:new Date().parse(formatString, '2015/04/11 12:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))
        expected << new TalkCard(name: 'No-Nonsense NOSQL',startDate:new Date().parse(formatString, '2015/04/11 12:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 11:15'))
        expected << new TalkCard(name: 'DSL&#8217;ING YOUR GROOVY',startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 12:00'))
        expected << new TalkCard(name: 'Intrastructure automation with gradle and puppet',startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 13:00'))
        expected << new TalkCard(name: 'Securing Ratpack', startDate:new Date().parse(formatString, '2015/04/11 11:15'), endDate: new Date().parse(formatString, '2015/04/11 12:00'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 10:00'))
        expected << new TalkCard(name: 'Documentation Brought to Life: ASCIIDOCTOR',startDate:new Date().parse(formatString, '2015/04/11 10:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))
        expected << new TalkCard(name: 'Groovy options for reactive programming',startDate:new Date().parse(formatString, '2015/04/11 10:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 09:00'))
        expected << new TalkCard(name: 'Cut your grails application to pieces & build feature plugins',startDate:  new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString, '2015/04/11 09:45'))
        expected << new TalkCard(name: 'Grooscript in Action',startDate:new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString,'2015/04/11 09:45'))
        expected << new TalkCard(name: 'Hands on Ratpack',startDate:new Date().parse(formatString, '2015/04/11 09:00'), endDate: new Date().parse(formatString, '2015/04/11 10:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/10 17:15'))
        expected << new TalkCard(name: 'Grails and Cassandra',startDate:new Date().parse(formatString, '2015/04/10 17:15'), endDate: new Date().parse(formatString,'2015/04/10 18:00'))
        expected << new TalkCard(name: 'The Groovy Ecosystem',startDate:new Date().parse(formatString, '2015/04/10 17:15'), endDate: new Date().parse(formatString,'2015/04/10 18:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 16:00'))
        expected << new TalkCard(name: 'Advanced Microservices Concerns',startDate:new Date().parse(formatString,'2015/04/10 16:00'), endDate: new Date().parse(formatString,'2015/04/10 16:45'))
        expected << new TalkCard(name: 'Decathlon Sportmeeting &#8211; Sports, a new Grails Discipline',startDate:new Date().parse(formatString,'2015/04/10 16:00'), endDate: new Date().parse(formatString,'2015/04/10 16:45'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 15:00'))
        expected << new TalkCard(name: 'GR8Workshops: A Guided discussion about Teaching and Diversity in the Groovy Community',startDate:  new Date().parse(formatString,'2015/04/10 15:00'), endDate: new Date().parse(formatString,'2015/04/10 15:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 14:00'))
        expected << new TalkCard(name: 'Getting Started with Groovy on Android',startDate:new Date().parse(formatString,'2015/04/10 14:00'), endDate: new Date().parse(formatString,'2015/04/10 15:45'))
        expected << new TalkCard(name: 'Hacking the Grails Spring Security 2.0 Plugin',startDate:new Date().parse(formatString,'2015/04/10 14:00'), endDate: new Date().parse(formatString,'2015/04/10 14:45'))

        expected << new AgendaSessionTimeHeader(sessionDate: new Date().parse(formatString,'2015/04/10 12:15'))
        expected << new TalkCard(name: 'GPars Remoting',startDate:new Date().parse(formatString,'2015/04/10 12:15'), endDate: new Date().parse(formatString,'2015/04/10 13:00'))
        expected << new TalkCard(name: 'Grails Goodness',startDate:new Date().parse(formatString,'2015/04/10 12:15'), endDate: new Date().parse(formatString,'2015/04/10 13:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 11:15'))
        expected << new TalkCard(name: 'Building android apps with Gradle',startDate:new Date().parse(formatString,'2015/04/10 11:15'), endDate: new Date().parse(formatString,'2015/04/10 12:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/10 10:00'))
        expected << new TalkCard(name: 'Groovy and Scala: Friends or Foes',startDate:new Date().parse(formatString, '2015/04/10 10:00'), endDate: new Date().parse(formatString, '2015/04/10 10:45'))
        expected << new TalkCard(name: 'Introducing Workflow Architectures using grails',startDate:new Date().parse(formatString,'2015/04/10 10:00'), endDate: new Date().parse(formatString,'2015/04/10 10:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 09:00'))
        expected << new TalkCard(name: 'Groovy Past and Future',startDate:new Date().parse(formatString,'2015/04/10 09:00'), endDate: new Date().parse(formatString,'2015/04/10 09:45'))

        expected
    }
}

