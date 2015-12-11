package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPIAttachment extends WPAPIElement {
    String source
    Boolean isImage
    WPAPIAttachmentMeta attachmentMeta
}