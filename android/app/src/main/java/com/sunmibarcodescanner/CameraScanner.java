package com.sunmibarcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.ArrayList;
import java.util.HashMap;

class CameraScanner extends ReactContextBaseJavaModule {
    private final int SCAN_CODE = 999;
    private Promise promise;

    CameraScanner(ReactApplicationContext reactContext) {
        super(reactContext);
        ActivityEventListener eventListener = new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent intent) {
                if (requestCode == SCAN_CODE) {
                    if (promise != null && resultCode == Activity.RESULT_OK) {
                        if (intent == null) {
                            promise.reject("Error occurred", "No data");
                            return;
                        }

                        Bundle bundle = intent.getExtras();
                        ArrayList result = (ArrayList) bundle.getSerializable("data");
                        for (HashMap hashMap : (Iterable<HashMap>) result) {
                            String scannedValue = (String) hashMap.get("VALUE");
                            promise.resolve(scannedValue);
                        }
                    }
                }
            }

            @Override
            public void onNewIntent(Intent intent) {

            }
        };
        reactContext.addActivityEventListener(eventListener);
    }

    @ReactMethod
    void launchScanner(Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject("Error occurred", "No activity to support this request");
            return;
        }

        this.promise = promise;
        Intent intent = new Intent("com.summi.scan");
        intent.setPackage("com.sunmi.sunmiqrcodescanner");
        currentActivity.startActivityForResult(intent, SCAN_CODE);
    }

    @NonNull
    @Override
    public String getName() {
        return "CameraScanner";
    }
}