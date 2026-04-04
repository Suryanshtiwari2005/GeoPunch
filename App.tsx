import axios from 'axios';
import { loginUser, getAuthToken } from './android/app/src/services/authService';
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
//
const backgroundTask = async (taskData: any) => {
  const { delay } = taskData;
  let lastLat = 0;
  let lastLon = 0;

  while (BackgroundService.isRunning()) {
    try {
      console.log('🔄 BG loop');

      await new Promise<void>(resolve => {
        try {
          Geolocation.getCurrentPosition(
            async position => {
              try {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                console.log('📡 BG Location:', lat, lon);

                const moved =
                Math.abs(lat - lastLat) > 0.0001 ||
                Math.abs(lon - lastLon) > 0.0001;

                if (moved) {
                  lastLat = lat;
                  lastLon = lon;

                  try {
                    // 🔐 GET TOKEN
                    const token = await getAuthToken();

                    if (!token) {
                      console.log('❌ No token → skipping API');
                      resolve();
                      return;
                    }

                    // 📦 REQUEST BODY
                    const body = {
                      latitude: lat,
                      longitude: lon,
                    };

                    // 🔐 SECURE API CALL
                    await axios.post(
                      'http://192.168.1.5:8080/api/attendance/location-check',
                      body,
                      {
                        headers: {
                          Authorization: `Bearer ${token}`,
                        },
                      },
                    );

                    console.log('🔐 Secure API call success');
                  } catch (err: any) {
                    if (err.response) {
                      console.log('⚠️ Backend:', err.response.data.message);
                    } else {
                      console.log('❌ Network error:', err.message);
                    }
                  }
                } else {
                  console.log('⏸️ Skipped (no movement)');
                }
              } catch (err) {
                console.log('❌ Processing error:', err);
              }

              resolve();
            },
            error => {
              console.log('❌ Location error:', error);
              resolve();
            },
            {
              enableHighAccuracy: true,
              timeout: 15000,
              maximumAge: 10000,
            },
          );
        } catch (err) {
          console.log('❌ getCurrentPosition crash:', err);
          resolve();
        }
      });
    } catch (e) {
      console.log('❌ BG loop crash:', e);
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

      const res = await loginUser('test@gmail.com', '123456');

      if (!res.success) {
        console.log('❌ Login error:', res);
        return;
      }

      console.log('✅ Login success');

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
