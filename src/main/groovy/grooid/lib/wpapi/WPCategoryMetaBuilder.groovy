package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPCategoryMetaBuilder {


    static final String kLinks = 'links'
    static final String kCollection = 'collection'
    static final String kSelf = 'self'

    static WPCategoryMeta categoryMetaFromMap(Map dictionary) {

        WPCategoryMeta categoryMeta = new WPCategoryMeta()
        if(dictionary[kLinks] in Map) {
            WPCategoryLink link = new WPCategoryLink()
            link.collection = dictionary[kLinks][kCollection]
            link.self = dictionary[kLinks][kSelf]
            categoryMeta.links = link
        }

        categoryMeta
    }
}