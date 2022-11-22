import React, { useState } from "react";
import { NativeModules, Button, Text } from "react-native";

const { CalendarModule, CameraScanner } = NativeModules;

export default function App() {
  const [result, setResult] = useState("");
  const onPress = async () => {
    try {
      const resultResp = await CameraScanner.launchScanner();
      console.log("RES::", resultResp);
      setResult(resultResp);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <Button
        title="Click to invoke your native module!"
        color="#841584"
        onPress={onPress}
      />

      <Text>result: {result}</Text>
    </>
  );
}
