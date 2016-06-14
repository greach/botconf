package com.botconf
import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnUIThread
import com.botconf.android.adapters.AgendaFilterAdapter
import com.botconf.android.adapters.IAgendaFilterAdapterDelegate
import com.botconf.entities.agendafilters.IAgendaFilter
import com.botconf.entities.agendafilters.IAgendaFilterHeaderLocalization
import com.botconf.usecases.AgendaFilterUseCase
import groovy.transform.CompileStatic

@CompileStatic
class AgendaFilterActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, IAgendaFilterAdapterDelegate, IAgendaFilterHeaderLocalization {

    AgendaFilterAdapter adapter

    SwipeRefreshLayout swipeRefreshLayout

    RecyclerView recyclerView

    AgendaFilterUseCase agendaFilterUseCase

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        recyclerView = (RecyclerView)findViewById(R.id.reyclerView)

        agendaFilterUseCase = new AgendaFilterUseCase(this)

        adapter = new AgendaFilterAdapter(getApplicationContext(),this)
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext())
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapter)

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        showProgress()
        refresh()
    }

    @OnBackground
    void refresh() {

        List<IAgendaFilter> agendaFilters = agendaFilterUseCase.fetchAgendaFilters()
        agendaFiltersFetched(agendaFilters)
        hideProgress()

    }

    @OnUIThread
    void agendaFiltersFetched(List<IAgendaFilter> agendaFilters) {
        adapter?.agendaFilters = agendaFilters
    }

    @OnUIThread
    void showProgress() {
        swipeRefreshLayout.setRefreshing(true)
    }

    @OnUIThread
    void hideProgress() {
        swipeRefreshLayout.setRefreshing(false)
    }

    @Override
    void onRefresh() {
        showProgress()
        refresh()
    }

    void enableRefresh() {
        swipeRefreshLayout.setEnabled(true)
    }

    void disableRefresh() {
        swipeRefreshLayout.setEnabled(false)
    }

    @Override
    void agendaFilterClicked(IAgendaFilter agendaFilter) {
    }

    String getAgendaFilterHeaderCongressString() {
        getString(R.string.agenda_filter_congress)
    }

    String getAgendaFilterHeaderTrackString() {
        getString(R.string.agenda_filter_track)
    }

    String getAgendaFilterHeaderTagString() {
        getString(R.string.agenda_filter_tag)
    }
}
