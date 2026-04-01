import axios from 'axios';
import React, { useEffect, useCallback } from 'react';
import { View, Text, PermissionsAndroid } from 'react-native';
import Geolocation from 'react-native-geolocation-service';
import BackgroundService from 'react-native-background-actions';

const sleep = (time: number): Promise<void> =>
  new Promise(resolve => setTimeout(resolve, time));

const options = {
  taskName: 'GeoPunch Tracking',
  taskTitle: 'GeoPunch Running',
  taskDesc: 'Tracking your attendance location',
  taskIcon: {
    name: 'ic_launcher',
    type: 'mipmap',
  },
  parameters: {
    delay: 15000,
  },
};


const backgroundTask = async (taskData: any) => {
  const { delay } = taskData;

  while (BackgroundService.isRunning()) {
    try {
      console.log("🔄 BG loop");

      await new Promise<void>((resolve) => {
        try {
          Geolocation.getCurrentPosition(
            async (position) => {
              try {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                console.log("📡 BG Location:", lat, lon);

                await axios.post(
                  "http://192.168.1.5:8080/api/attendance/location-check",
                  {
                    userId: "7732cea5-fa86-410c-b496-078ca4203719",
                    latitude: lat,
                    longitude: lon,
                  }
                );

                console.log("✅ API success");
              } catch (err) {
                console.log("❌ API error:", err);
              }

              resolve();
            },
            (error) => {
              console.log("❌ Location error:", error);
              resolve();
            },
            {
              enableHighAccuracy: true,
              timeout: 15000,
              maximumAge: 10000,
            }
          );
        } catch (err) {
          console.log("❌ getCurrentPosition crash:", err);
          resolve();
        }
      });

    } catch (e) {
      console.log("❌ BG loop crash:", e);
    }

    await sleep(delay);
  }
};

const App = () => {
  const requestPermission = useCallback(async () => {
    console.log('📢 Asking Permission...');

    const fineLocation = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    );

    const backgroundLocation = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION,
    );

    const notificationPermission = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
    );

    if (
      fineLocation === PermissionsAndroid.RESULTS.GRANTED &&
      backgroundLocation === PermissionsAndroid.RESULTS.GRANTED &&
      notificationPermission === PermissionsAndroid.RESULTS.GRANTED
    ) {
      console.log('✅ All permissions granted');
    } else {
      console.log('❌ Permission denied');
    }
  }, []);

  useEffect(() => {
    const start = async () => {
      console.log('🚀 Starting App');

      const fineLocation = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      );

      const backgroundLocation = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION,
      );

      const notificationPermission = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
      );

      const locationGranted =
        fineLocation === PermissionsAndroid.RESULTS.GRANTED &&
        backgroundLocation === PermissionsAndroid.RESULTS.GRANTED;

      if (!locationGranted) {
        console.log('❌ Location permissions not granted → STOP');
        return;
      }

      console.log('✅ Location permissions granted');

      // 🔥 notification is OPTIONAL
      if (notificationPermission !== PermissionsAndroid.RESULTS.GRANTED) {
        console.log('⚠️ Notification permission not granted (non-blocking)');
      }

      setTimeout(async () => {
        try {
          await BackgroundService.start(backgroundTask, options);
          console.log('✅ Background service started');
        } catch (e) {
          console.log('❌ Background start error:', e);
        }
      }, 2000);
    };

    start();

    return () => {
      BackgroundService.stop();
    };
  }, [requestPermission]);

  return (
    <View style={{ marginTop: 50 }}>
      <Text>Tracking Location...</Text>
    </View>
  );
};

export default App;
