package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPIAttachmentMeta {
    Integer width
    Integer height
    String file
    Map sizes
    WPAPIImageMeta imageMeta
}