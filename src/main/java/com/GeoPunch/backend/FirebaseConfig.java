package com.GeoPunch.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


//    @PostConstruct
//    public void init() throws IOException{
//        //------------------------------------------------------------
//        if (!FirebaseApp.getApps().isEmpty()) {
//            System.out.println("🔥 Firebase already initialized, skipping.");
//            return; // ← prevents duplicate initialization
//        }
//
//        //------------------------------------------------------------
//
//
//        System.out.println("🔥 Firebase initialized");
//        InputStream serviceAccount =
//                getClass().getClassLoader()
//                        .getResourceAsStream("firebase-service-account.json");
//
//        if(serviceAccount == null){
//            throw new RuntimeException("Firebase JSON file not found");
//        }
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        FirebaseApp.initializeApp(options);
//
//    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Guard against duplicate initialization
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("firebase-service-account.json");

        if (serviceAccount == null) {
            throw new RuntimeException("firebase-service-account.json not found in resources");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        System.out.println("🔥 Firebase initialized successfully");
        return FirebaseApp.initializeApp(options);  // ✅ returns the app as a bean
    }
    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp); // ✅ instance-based, not static
    }
}
