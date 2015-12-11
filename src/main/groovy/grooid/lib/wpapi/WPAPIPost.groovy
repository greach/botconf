package grooid.lib.wpapi

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class WPAPIPost extends WPAPIElement {

    String standard
    Object custom
    WPAPIAttachment featuredImage



}