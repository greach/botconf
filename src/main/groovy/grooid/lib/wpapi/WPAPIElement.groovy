package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPIElement {
    Integer identifier
    String title
    String status
    String type
    WPAPIAuthor author
    String content
    Integer parent
    String link
    Date date
    Date modified
    String format
    String slug
    String guid
    String excerpt
    Integer menuOrder
    String commentStatus
    String pingStatus
    Boolean sticky
    WPAPITerms terms
}