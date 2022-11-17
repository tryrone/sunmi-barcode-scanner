import React from "react";
import { NativeModules, Button } from "react-native";

const { CalendarModule, CameraScanner } = NativeModules;

export default function App() {
  const onPress = async () => {
    CameraScanner.launchScanner();
  };

  return (
    <Button
      title="Click to invoke your native module!"
      color="#841584"
      onPress={onPress}
    />
  );
}
