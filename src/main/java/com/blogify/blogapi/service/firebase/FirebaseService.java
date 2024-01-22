package com.blogify.blogapi.service.firebase;

import static com.blogify.blogapi.model.exception.ApiException.ExceptionType.SERVER_EXCEPTION;

import com.blogify.blogapi.model.exception.ApiException;
import com.blogify.blogapi.repository.model.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
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
  private FirebaseAuth auth() {
    FirebaseOptions options =
        new FirebaseOptions.Builder().setCredentials(getCredentials()).build();

    var app = FirebaseApp.initializeApp(options);
    return FirebaseAuth.getInstance(app);
  }

  @SneakyThrows
  private GoogleCredentials getCredentials() {
    var stream = new ByteArrayInputStream(privateKey.getBytes());
    return GoogleCredentials.fromStream(stream);
  }

  public FirebaseUser getUserByBearer(String bearer) {
    try {
      FirebaseToken token = auth().verifyIdToken(bearer);
      return token == null
          ? null
          : FirebaseUser.builder().id(token.getUid()).email(token.getEmail()).build();
    } catch (FirebaseAuthException e) {
      throw new ApiException(SERVER_EXCEPTION, e.getMessage());
    }
  }

  public User createUser(User user, String password) {
    UserRecord.CreateRequest request = new UserRecord.CreateRequest();
    request.setEmail(user.getMail());
    request.setPassword(password);
    request.setDisplayName(user.getFirstname() + " " + user.getLastname());
    request.setUid(user.getId());
    try {
      UserRecord record = auth().createUser(request);
      return user.toBuilder().id(record.getUid()).build();
    } catch (FirebaseAuthException e) {
      throw new ApiException(SERVER_EXCEPTION, e.getMessage());
    }
  }
}
