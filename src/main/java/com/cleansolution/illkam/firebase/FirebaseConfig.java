package com.cleansolution.illkam.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        try {
            InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();
            System.out.println("Service account file loaded successfully");
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            System.out.println("Credentials created successfully");
            
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            
            FirebaseApp app = FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully");
            return app;
            
        } catch (Exception e) {
            System.err.println("Firebase initialization error: " + e.getMessage());
            throw e;
        }
    }
}
