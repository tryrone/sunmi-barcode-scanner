
import React from 'react';
import { NativeModules, Button } from "react-native";

const { CalendarModule } = NativeModules;

export default function App() {
 const onPress = async () => {
    await CalendarModule.createCalendarEvent("testName", "testLocation");
    console.log("Event created");
 };

 return (
   <Button
     title="Click to invoke your native module!"
     color="#841584"
     onPress={onPress}
   />
 );
}


