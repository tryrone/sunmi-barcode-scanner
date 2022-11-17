package com.sunmibarcodescanner;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

class CameraScanner extends ReactContextBaseJavaModule {
    CameraScanner(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private final int SCAN_CODE = 999;

    @ReactMethod
    void launchScanner() {
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent("com.summi.scan");
        intent.setPackage("com.sunmi.sunmiqrcodescanner");
        if (currentActivity != null) currentActivity.startActivityForResult(intent, SCAN_CODE);
    }

    @NonNull
    @Override
    public String getName() {
        return "CameraScanner";
    }
}