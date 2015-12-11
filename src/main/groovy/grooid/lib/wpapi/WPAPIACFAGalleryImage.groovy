package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPIACFAGalleryImage {
    String identifer
    String alt
    String title
    String caption
    String descr
    String mimeType
    String url
    String width
    String height
    WPAPIACFAGalleryImageSizes sizes
}