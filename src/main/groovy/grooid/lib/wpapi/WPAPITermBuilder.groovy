package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPITermBuilder {
    private static final String TAG = WPAPITermBuilder.class.getSimpleName()

    static String kIdentifier = "ID"
    static String kName = "name"
    static String kSlug = "slug"
    static String kDescription = "description"
    static String kTaxonomy = "taxonomy"
    static String kParent = "parent"
    static String kCount = "count"
    static String kLink = "link"
    static String kMeta = "meta"

    static String kTermsPostTag = "post_tag"
    static String kCategory = "category"

    static WPAPITerms termsWithMap(Map dictionary) {

        WPAPITerms terms = new WPAPITerms()

        terms.postTags = []
        if(dictionary[kTermsPostTag] in List) {
            for(Object obj in (List)dictionary[kTermsPostTag]) {
                if(obj in Map) {
                    WPAPITerm tag = termWithMap((Map)obj)
                    terms.postTags << tag
                }
            }
        }

        if(dictionary[kCategory] in List) {
            for(Object obj in (List)dictionary[kCategory]) {
                if (obj in Map) {
                    terms.category = termWithMap(dictionary[kCategory])
                }
            }
        }

        terms
    }

    static WPAPITerm termWithMap(dictionary) {

        WPAPITerm term = new WPAPITerm()

        if(dictionary[kIdentifier] in String) {
            term.identifier = Long.valueOf((String)dictionary[kIdentifier])
        } else if(dictionary[kIdentifier] in Long) {
            term.identifier = (Long) dictionary[kIdentifier]
        }

        term.name = dictionary[kName]

        if(term.name) {
            term.name = term.name.replace("[", "")
            term.name = term.name.replace("]", "")
        }

        term.slug = dictionary[kSlug]
        term.description = dictionary[kDescription]
        term.taxonomy = dictionary[kTaxonomy]
        if(dictionary[kParent] in Map) {
            term.parent = termWithMap((Map) dictionary[kParent])
        }
        if(dictionary[kCount] in Integer) {
            term.count = (Integer)dictionary[kCount]
        } else if(dictionary[kCount] in String) {
            term.count = Integer.valueOf((String)dictionary[kCount])
        }
        term.link = dictionary[kLink]

        if(dictionary[kMeta] in Map) {
            term.meta = WPCategoryMetaBuilder.categoryMetaFromMap((Map)dictionary[kMeta])
        }

        term
    }
}