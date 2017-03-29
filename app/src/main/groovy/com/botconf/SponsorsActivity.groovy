package com.botconf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.botconf.android.adapters.SponsorsAdapter
import com.botconf.android.adapters.TalkAdapter
import com.botconf.entities.Sponsor
import com.botconf.entities.interfaces.ITalk
import com.botconf.usecases.SponsorsUseCase
import groovy.transform.CompileStatic

@CompileStatic
class SponsorsActivity extends AppCompatActivity {

    RecyclerView recyclerView

    SponsorsAdapter adapter
    SponsorsUseCase sponsorsUseCase

    ITalk talk

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sponsors)

        recyclerView = (RecyclerView)findViewById(R.id.reyclerView)

        sponsorsUseCase = new SponsorsUseCase(getApplicationContext())
        adapter = new SponsorsAdapter(this)

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext())
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapter)

        def sponsors = sponsorsUseCase.sponsors()
        adapter.setSponsorsAndCategories(categoriesForSponsors(sponsors),sponsorsUseCase.sponsors())
    }

    static List<String> categoriesForSponsors(List<Sponsor> sponsors) {
        List<String> categories = sponsors.collect { it.category }.unique()
        categories = categories.sort { a, b ->
            SponsorsUseCase.sponsorInt(b) <=> SponsorsUseCase.sponsorInt(a)
        }
        categories
    }

}