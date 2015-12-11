package com.botconf.android.fragments
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arasthel.swissknife.annotations.OnUIThread
import com.botconf.R
import com.botconf.TalkActivity
import com.botconf.android.adapters.CardAdapter
import com.botconf.android.adapters.ICardAdapterDelegate
import com.botconf.entities.interfaces.ITalkCard
import com.botconf.usecases.AgendaUseCase
import com.botconf.usecases.LocalRepositoryUseCase
import groovy.transform.CompileStatic

@CompileStatic
class FavouritesTalksFragment extends Fragment implements IUpdatableFragment, ICardAdapterDelegate {

    RecyclerView recyclerView

    CardAdapter adapter

    AgendaUseCase agendaUseCase

    LocalRepositoryUseCase localRepositoryUseCase

    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = (RecyclerView)rootView.findViewById(R.id.reyclerView)


        adapter = new CardAdapter(getActivity(),this)
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity())
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapter)

        agendaUseCase = new AgendaUseCase(getActivity())
        localRepositoryUseCase = new LocalRepositoryUseCase(getActivity())

        refreshUi()

        rootView
    }


    @OnUIThread
    void talksFetched(List<ITalkCard> talks) {
        adapter?.agendaSessions = agendaUseCase.buildAgenda(talks)
    }


    void refreshUi() {
        List<ITalkCard> talkCards = localRepositoryUseCase.fetchFavouriteTalkCardsFromLocalRepository()
        talksFetched(talkCards)
    }

    @Override
    void cardClicked(ITalkCard card) {
        if(card) {
            Intent i = new Intent(getActivity(), TalkActivity)
            i.putExtra(TalkActivity.EXTRA_PRIMARY_KEY, card.getPrimaryKey())
            startActivity(i)
        }
    }
}