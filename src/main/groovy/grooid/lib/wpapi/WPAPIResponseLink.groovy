package grooid.lib.wpapi

import groovy.transform.CompileStatic

import java.util.regex.Matcher;

@CompileStatic
class WPAPIResponseLink {

    static String kRelNext = "next"
    static String kRelPrev = "prev"
    static String kRelItem = "prev"
    static String kRegex = '^<(.*)>; rel=\"(item|next)\"(; title=\"(.*)\")?$'

    String path
    WPAPIResponseLinkRel rel
    String title

    static enum WPAPIResponseLinkRel {next, prev, item}

    static String nextPathForResponseLinks(List arr) {
        for(Object obj : arr) {
            if(obj instanceof WPAPIResponseLink) {
                WPAPIResponseLink responseLink = (WPAPIResponseLink)obj
                if(responseLink.rel == WPAPIResponseLinkRel.next) {
                    return responseLink.path
                }
            }
        }
    }


    static List<WPAPIResponseLink> extractResponseLinksFromString(String str) {

        def responseLinks = []

        if(str!=null) {
            def components = str.split(', ')
            def regex = /^<(.*)>; rel="(item|next)"(; title="(.*)")?$/
            components.each {
                if(it ==~ regex) {
                    Matcher matcher = (it =~ regex)
                    if(matcher.size() == 1 && ((ArrayList)matcher[0]).size() == 5) {
                        WPAPIResponseLink responseLink = new WPAPIResponseLink()
                        responseLink.path = ((ArrayList)matcher[0])[1]
                        def value = ((ArrayList)matcher[0])[2]
                        if(value == kRelNext) {
                            responseLink.rel = WPAPIResponseLinkRel.next;

                        } else if(value == kRelItem) {

                            responseLink.rel = WPAPIResponseLinkRel.item;

                        } else if(value == kRelPrev) {

                            responseLink.rel = WPAPIResponseLinkRel.prev;
                        }
                        responseLink.title =  ((ArrayList)matcher[0])[4]
                        responseLinks << responseLink
                    }
                }
            }
        }
        responseLinks
    }
}