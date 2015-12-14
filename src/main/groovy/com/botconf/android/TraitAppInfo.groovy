package com.botconf.android

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import com.botconf.entities.AppInfo
import com.botconf.entities.DeviceInfo
import groovy.transform.CompileStatic

@CompileStatic
trait TraitAppInfo {

    abstract PackageManager getPackageManager()
    abstract String getPackageName()
    abstract Object getSystemService(String name)

    String appAndDeviceHtml() {
        DeviceInfo deviceInfo = deviceInfo(getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
        AppInfo appInfo = appInfo(getPackageManager(),getPackageName())
        appAndDeviceInfoHtml(appInfo, deviceInfo)
    }

    private String appAndDeviceInfoHtml(AppInfo appInfo, DeviceInfo deviceInfo){
        """
        Android Version Int: ${appInfo.getPlatformVersion()} <br />
        App Version: VC ${appInfo.getVersionCode()} (${appInfo.getVersionName()})<br />
        Device: ${deviceInfo.getModel()}, ${deviceInfo.getDevice()}, ${deviceInfo.getBrand()}, ${deviceInfo.getCarrier()} <br />
        """
    }

    DeviceInfo deviceInfo(TelephonyManager telephonyManager) {
        return new DeviceInfo(model: Build.MODEL, device: Build.DEVICE, brand: Build.BRAND, carrier: telephonyManager.getNetworkOperatorName())
    }

    AppInfo appInfo(PackageManager packageManager, String packageName) {
        try {
            PackageInfo appInfo = packageManager?.getPackageInfo(packageName, 0)
            new AppInfo(versionCode: String.valueOf(appInfo.versionCode),
                    versionName: appInfo.versionName,
                    platformVersion: (android.os.Build.VERSION.SDK_INT as String))

        } catch (PackageManager.NameNotFoundException e) {
            new AppInfo(versionCode: "--", versionName: "--", platformVersion: String.valueOf(android.os.Build.VERSION.SDK_INT))
        }
    }
}