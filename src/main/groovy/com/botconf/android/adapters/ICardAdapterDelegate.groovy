package com.botconf.android.adapters

import com.botconf.entities.interfaces.ITalkCard
import groovy.transform.CompileStatic

@CompileStatic
interface ICardAdapterDelegate {

    void cardClicked(ITalkCard card)
}