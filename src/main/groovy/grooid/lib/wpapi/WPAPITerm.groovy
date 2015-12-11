package grooid.lib.wpapi;

import groovy.transform.CompileStatic

@CompileStatic
class WPAPITerm {

    Long identifier
    String name
    String slug
    String description
    String taxonomy
    int count
    String link
    WPAPITerm parent
    WPCategoryMeta meta


}