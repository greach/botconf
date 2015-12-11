package grooid.lib.wpapi

import groovy.transform.CompileStatic

@CompileStatic
class WPAPIACFGalleryParser {

    static final String kSizes = "sizes"

    static List extractGallery(Map acfDict, String galleryKey) {
        List mutableAttArr = [];

        if(acfDict[galleryKey] instanceof Map) {

            mutableAttArr << buildGalleryImageFromDict( (Map) acfDict[galleryKey])

        } else if(acfDict[galleryKey] in List) {
            for(Object galeryObj : acfDict[galleryKey]) {

                if(galeryObj in Map) {
                    mutableAttArr << buildGalleryImageFromDict((Map)galeryObj)
                }
            }
        }
        mutableAttArr
    }


    static WPAPIACFAGalleryImage buildGalleryImageFromDict(Map galeryDict) {

        WPAPIACFAGalleryImage att = new WPAPIACFAGalleryImage()
        att.identifer   = galeryDict["id"]
        att.alt         = galeryDict["alt"]
        att.title       = galeryDict["title"]
        att.caption     = galeryDict["caption"]
        att.descr       = galeryDict["description"]
        att.mimeType    = galeryDict["mime_tye"]
        att.url         = galeryDict["url"]
        att.width       = galeryDict["width"]
        att.height      = galeryDict["height"]

        WPAPIACFAGalleryImageSizes sizes = new WPAPIACFAGalleryImageSizes()

        sizes.thumbnail             = galeryDict[kSizes]["thumbnail"]
        sizes.thumbnailWidth        = (Integer) galeryDict[kSizes]["thumbnail-width"]
        sizes.thumbnailHeight       = (Integer) galeryDict[kSizes]["thumbnail-height"]

        sizes.medium                = galeryDict[kSizes]["medium"]
        sizes.mediumWidth           = (Integer) galeryDict[kSizes]["medium-width"]
        sizes.mediumHeight          = (Integer) galeryDict[kSizes]["medium-height"]

        sizes.large                 = galeryDict[kSizes]["large"]
        sizes.largeWidth            = (Integer) galeryDict[kSizes]["large-width"]
        sizes.largeHeight           = (Integer) galeryDict[kSizes]["large-height"]

        sizes.postThumbnail         = galeryDict[kSizes]["post-thumbnail"]
        sizes.postThumbnailWidth    = (Integer) galeryDict[kSizes]["post-thumbnail-width"]
        sizes.postThumbnailHeight   = (Integer) galeryDict[kSizes]["post-thumbnail-height"]

        att.sizes = sizes;

        att;
    }
}