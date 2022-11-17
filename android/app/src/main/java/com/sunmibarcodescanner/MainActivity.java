package com.sunmibarcodescanner;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import aidl.IScanInterface;
import expo.modules.ReactActivityDelegateWrapper;

public class MainActivity extends ReactActivity {
    private static final String ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
    private static final String DATA = "data";
    private static final String SOURCE = "source_byte";
    private final int START_SCAN = 0;

    private IScanInterface scanInterface;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            scanInterface = IScanInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            scanInterface = null;
        }
    };

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            byte[] arr = intent.getByteArrayExtra(SOURCE);
            if (code != null && !code.isEmpty()) {
                Log.d("TAG", "onReceive: CODE IS:::" + code);
                Log.d("TAG", "onReceive: ARR IS:::" + Arrays.toString(arr));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the theme to AppTheme BEFORE onCreate to support
        // coloring the background, status bar, and navigation bar.
        // This is required for expo-splash-screen.
        setTheme(R.style.AppTheme);
        super.onCreate(null);
    }

    private void scanWithCamera() {
        Intent intent = new Intent("com.summi.scan");
        intent.setPackage("com.sunmi.sunmiqrcodescanner");
        startActivityForResult(intent, START_SCAN);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DATA_CODE_RECEIVED);
        registerReceiver(receiver, filter);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.scanner");
        intent.setAction("com.sunmi.scanner.IScanInterface");
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList result = (ArrayList) bundle.getSerializable("data");
            Iterator<HashMap> it = result.iterator();
            while (it.hasNext()) {
                HashMap hashMap = it.next();
                Log.i("sunmi", (String) hashMap.get("TYPE"));
                Log.i("sunmi", (String) hashMap.get("VALUE"));
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            try {
                scanInterface.sendKeyEvent(event);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
        super.onStop();
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "main";
    }

    /**
     * Returns the instance of the {@link ReactActivityDelegate}. There the RootView is created and
     * you can specify the renderer you wish to use - the new renderer (Fabric) or the old renderer
     * (Paper).
     */
    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegateWrapper(this, BuildConfig.IS_NEW_ARCHITECTURE_ENABLED,
                new MainActivityDelegate(this, getMainComponentName())
        );
    }

    /**
     * Align the back button behavior with Android S
     * where moving root activities to background instead of finishing activities.
     *
     * @see <a href="https://developer.android.com/reference/android/app/Activity#onBackPressed()">onBackPressed</a>
     */
    @Override
    public void invokeDefaultOnBackPressed() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (!moveTaskToBack(false)) {
                // For non-root activities, use the default implementation to finish them.
                super.invokeDefaultOnBackPressed();
            }
            return;
        }

        // Use the default back button implementation on Android S
        // because it's doing more than {@link Activity#moveTaskToBack} in fact.
        super.invokeDefaultOnBackPressed();
    }

    public static class MainActivityDelegate extends ReactActivityDelegate {
        public MainActivityDelegate(ReactActivity activity, String mainComponentName) {
            super(activity, mainComponentName);
        }

        @Override
        protected ReactRootView createRootView() {
            ReactRootView reactRootView = new ReactRootView(getContext());
            // If you opted-in for the New Architecture, we enable the Fabric Renderer.
            reactRootView.setIsFabric(BuildConfig.IS_NEW_ARCHITECTURE_ENABLED);
            return reactRootView;
        }

        @Override
        protected boolean isConcurrentRootEnabled() {
            // If you opted-in for the New Architecture, we enable Concurrent Root (i.e. React 18).
            // More on this on https://reactjs.org/blog/2022/03/29/react-v18.html
            return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
        }
    }
}
