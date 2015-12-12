package com.botconf.entities

import groovy.transform.CompileStatic
import groovy.transform.Immutable;

@Immutable
@CompileStatic
class DeviceInfo {
    String model
    String device
    String brand
    String carrier
}