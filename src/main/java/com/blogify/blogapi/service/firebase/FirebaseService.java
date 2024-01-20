package com.blogify.blogapi.service.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import java.io.ByteArrayInputStream;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FirebaseService {
  public final String privateKey;

  public FirebaseService(@Value("${firebase.private.key}") String privateKey) {
    this.privateKey = privateKey;
  }

  @SneakyThrows
  private FirebaseApp app() {
    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(getCredentials())
        .build();

    return FirebaseApp.initializeApp(options);
  }

  @SneakyThrows
  private GoogleCredentials getCredentials() {
    var stream = new ByteArrayInputStream(privateKey.getBytes());
    return GoogleCredentials.fromStream(stream);
  }

  @SneakyThrows
  public FirebaseToken getUserByBearer(String bearer) {
    return FirebaseAuth.getInstance(app()).verifyIdToken(bearer);
  }

}
