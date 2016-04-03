package com.botconf.android.fragments

import com.botconf.entities.interfaces.ITalkCard


class UpcomingTalksFragment extends AllTalksFragment {

    @Override
    void refreshUi() {
        List<ITalkCard> talkCards = localRepositoryUseCase.fetchUpcomingTalkCardsFromLocalRepository()
        if(talkCards) {
            talksFetched(talkCards)
        }
    }
}