package com.botconf.usecases

import com.botconf.R
import com.botconf.entities.Sponsor
import groovy.transform.CompileStatic

@CompileStatic
class SponsorsUseCase {

    List<String> sponsorsCategories() {
        ['Diamond Sponsor','Platinum Sponsor','Gold Sponsor','Bronze Sponsor', 'Collaborators']
    }

    List<Sponsor> sponsors() {
        [
                new Sponsor(category:'Diamond Sponsor', imageRes: R.drawable.oci),
                new Sponsor(category:'Platinum Sponsor', imageRes: R.drawable.kaleidos),
                new Sponsor(category:'Platinum Sponsor', imageRes: R.drawable.osoco),
                new Sponsor(category:'Platinum Sponsor', imageRes: R.drawable.salenda),
                new Sponsor(category:'Platinum Sponsor', imageRes: R.drawable.infortelecom),
                new Sponsor(category:'Gold Sponsor', imageRes: R.drawable.virtual),
                new Sponsor(category:'Gold Sponsor', imageRes: R.drawable.paradigma),
                new Sponsor(category:'Gold Sponsor', imageRes: R.drawable.autentia),
                new Sponsor(category:'Bronze Sponsor', imageRes: R.drawable.diputaciotarragona),
                new Sponsor(category:'Bronze Sponsor', imageRes: R.drawable.pronoide),
                new Sponsor(category:'Bronze Sponsor', imageRes: R.drawable.puravida),
                new Sponsor(category:'Bronze Sponsor', imageRes: R.drawable.aluman),
                new Sponsor(category:'Bronze Sponsor', imageRes: R.drawable.ticketbis),
                new Sponsor(category:'Collaborators', imageRes: R.drawable.madridgug),
                new Sponsor(category:'Collaborators', imageRes: R.drawable.eventbis),
                new Sponsor(category:'Collaborators', imageRes: R.drawable.pragmatic),
                new Sponsor(category:'Collaborators', imageRes: R.drawable.jetbrains),
                new Sponsor(category:'Collaborators', imageRes: R.drawable.groovycalamari),
        ]
    }
}