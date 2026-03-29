import React, { useEffect, useCallback } from "react";
import { View, Text, PermissionsAndroid } from "react-native";
import Geolocation from "react-native-geolocation-service";

const App = () => {

  const getLocation = useCallback(() => {
    console.log("📡 Starting location watch...");
    Geolocation.watchPosition(
      (position) => {
        console.log("📍 Latitude:", position.coords.latitude);
        console.log("📍 Longitude:", position.coords.longitude);
      },
      (error) => {
        console.log("❌ Error:", error);
      },
      {
        enableHighAccuracy: true,
        distanceFilter: 0,
        interval: 5000,
        fastestInterval: 2000,
        showLocationDialog: true,
        forceRequestLocation: true,
      }
    );
  }, []);

  const requestPermission = useCallback(async () => {
  console.log("📢 Asking Permission...");

  const granted = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
  );

  console.log("📢 Permission result:", granted);

  if (granted === PermissionsAndroid.RESULTS.GRANTED) {
    console.log("✅ Permission granted");
    getLocation();
  } else {
    console.log("❌ Permission denied");
  }
}, [getLocation]);

  useEffect(() => {
    console.log("App Started");
    requestPermission();
  }, [requestPermission]);

  return (
    <View style={{ marginTop: 50 }}>
      <Text>Getting Location...</Text>
    </View>
  );
};

export default App;