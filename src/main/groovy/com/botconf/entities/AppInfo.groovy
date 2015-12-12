package com.botconf.entities

import groovy.transform.CompileStatic
import groovy.transform.Immutable;

@Immutable
@CompileStatic
class AppInfo {
    String versionCode
    String versionName
    String platformVersion
}