package com.sunmibarcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class CameraScanner extends ReactContextBaseJavaModule {
    private final int SCAN_CODE = 999;
    private Promise promise;

    private final ActivityEventListener eventListener = new ActivityEventListener() {
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
                    Iterator<HashMap> it = result.iterator();
                    while (it.hasNext()) {
                        HashMap hashMap = it.next();
                        Log.i("scanner result type", (String) hashMap.get("TYPE"));
                        Log.i("scanner result type", (String) hashMap.get("VALUE"));
                    }
                }
            }
        }

        @Override
        public void onNewIntent(Intent intent) {

        }
    };

    CameraScanner(ReactApplicationContext reactContext) {
        super(reactContext);
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