package com.botconf.entities.interfaces

import groovy.transform.CompileStatic

@CompileStatic
interface IAppPersistenceDataSource {

    long insertConference(IConference conference)
    long insertSpeaker(ISpeaker conference)
    long insertTalk(ITalk talk)
    List<ITalkCard> findAllTalkCards()
    ITalk findTalkByPrimaryKey(Long primaryKey)
    List<ITalkCard> findAllFavouriteTalkCards()
    void addToFavorites(ITalk talk)
    void removeFromFavorites(ITalk talk)
    int countTalks()
}