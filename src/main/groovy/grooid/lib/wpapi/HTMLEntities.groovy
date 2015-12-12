package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class HTMLEntities {

        static final Map htmlEntities = [
                "&#32;": "!",
                "&#33;": "\"",
                "&#34;":"#",
                "&#35;": "#",
                "&#36;": "%",
                "&#37;":"&",
                "&#038;":"&",
                "&#38;":"'",
                "&#39;": "(",
                "&#40;": ")",
                "&#41;": "*",
                "&#42;": "+",
                "&#43;": ",",
                "&#44;": "-",
                "&#45;": ".",
                "&#46;": "/",
                "&#47;": "",
                "&#8211;": "–",
                "&#8212;": "—",
                "&#8216;": "‘",
                "&#8217;": "’",
                "&#8218;": "‚",
                "&#8220;": "“",
                "&#8221;": "”",
                "&#8222;": "„",
                "&#8224;": "†",
                "&#8225;": "‡",
                "&#8226;": "•",
                "&#8230;": "…",
                "&#8240;": "‰",
                "&#8364;": "€",
                "&#8482;":"™"]

        static String replaceHTMLEntities(String title) {
                if(title) {
                        htmlEntities.each { k, v ->
                                title = title.replace(k as String,v as String)
                        }
                }

                title
        }
}