package com.sunmibarcodescanner

import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class CameraScanner(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    companion object {
        const val SCAN_CODE = 999
    }

    @ReactMethod
    fun launchScanner() {
        Intent("com.summi.scan").apply {
            setPackage("com.sunmi.sunmiqrcodescanner")
        }.also {
            currentActivity?.startActivityForResult(it, SCAN_CODE)
        }
    }

    override fun getName(): String {
        return "CameraScanner"
    }
}