import React, { useEffect } from "react";
import {
  NativeModules,
  Button,
  NativeEventEmitter,
  DeviceEventEmitter,
} from "react-native";

const { CalendarModule, CameraScanner } = NativeModules;

export default function App() {
  useEffect(() => {
    DeviceEventEmitter.addListener("INFRARED_SCAN", onInfraRedScan);
  });

  const onInfraRedScan = (data: string) => {
    console.log("INFRA_RED_SCAN", data);
  };

  const onPress = async () => {
    try {
      const result = await CameraScanner.launchScanner();
      console.log("RES::", result);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Button
      title="Click to invoke your native module!"
      color="#841584"
      onPress={onPress}
    />
  );
}
