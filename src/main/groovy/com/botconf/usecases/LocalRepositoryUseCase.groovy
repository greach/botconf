package com.botconf.usecases
import android.content.Context
import com.botconf.android.interfaceadapters.persistence.sqlite.AppPersistenceDataSource
import com.botconf.entities.interfaces.ITalk
import com.botconf.entities.interfaces.IAppPersistenceDataSource
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalkCard
import groovy.transform.CompileStatic

@CompileStatic
class LocalRepositoryUseCase {

    IAppPersistenceDataSource dataSource

    LocalRepositoryUseCase(Context ctx) {
        dataSource = new AppPersistenceDataSource(ctx)
    }

    int countTalkCardsFromLocalRepository() {

        dataSource.countTalks()
    }

    List<ITalkCard> fetchTalkCardsFromLocalRepository() {

        dataSource.findAllTalkCards()
    }

    List<ITalkCard> fetchFavouriteTalkCardsFromLocalRepository() {

        dataSource.findAllFavouriteTalkCards()
    }

    void addtoFavourites(ITalk talk) {
        dataSource.addToFavorites(talk)
    }

    void removeFromFavourites(ITalk talk) {
        dataSource.removeFromFavorites(talk)
    }


    void updateLocalRepositoryWithSpeakers(List<ISpeaker> speakers) {
        speakers.each { dataSource.insertSpeaker(it) }
    }

    void updateLocalRepositoryWithConference(List<IConference> conferences) {
        conferences.each { dataSource.insertConference(it) }
    }

    void updateLocalRepositoryWithTalks(List<ITalk> talks) {
        talks.each { dataSource.insertTalk(it) }
    }

    ITalk findTalkByPrimaryKey(Long primaryKey) {
        dataSource.findTalkByPrimaryKey(primaryKey)
    }


}