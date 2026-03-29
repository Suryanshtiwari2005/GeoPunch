import React, { useEffect, useState, useCallback } from "react";
import { View, Text, PermissionsAndroid } from "react-native";
import Geolocation from "react-native-geolocation-service";

const App = () => {

  const [latitude, setLatitude] = useState<number | null>(null);
  const [longitude, setLongitude] = useState<number | null>(null);

  const getLocation = useCallback(() => {
    console.log("📡 Starting location watch...");
    Geolocation.watchPosition(
      (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;

          console.log("📍 Latitude:", lat);
          console.log("📍 Longitude:", lon);

          setLatitude(lat);
          setLongitude(lon);
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

      {latitude && longitude && (
        <>
         <Text>Latitude: {latitude}</Text>
         <Text>Longitude: {longitude}</Text>
        </>
      )}
    </View>
  );
};

export default App;