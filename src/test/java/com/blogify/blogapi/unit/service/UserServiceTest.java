package com.blogify.blogapi.unit.service;

import static com.blogify.blogapi.integration.conf.MockData.UserMockData.clientEntity1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogify.blogapi.model.validator.UserValidator;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserValidator userValidator;

  @Test
  void updateUserPhotoKey_whenUserExists_shouldUpdatePhotoKey() {
// Given
    String userId = "CLIENT1_ID";
    User client1 = clientEntity1(); // Mock user instance
    client1.setProfileBannerKey(null); // Reset profile banner key to null for testing
    User client1Updated = clientEntity1().toBuilder().photoKey("user/" + userId + "/profile").build(); // Expected updated user
    when(userRepository.findById(userId)).thenReturn(Optional.of(client1)); // Mock repository behavior
    doNothing().when(userValidator).accept(client1); // Mock user validator
    when(userRepository.save(client1)).thenReturn(client1Updated); // Mock userRepository.save()

// When
    User result = userService.updateUserPhotoKey(client1);

// Then
    assertEquals(client1Updated.getPhotoKey(), result.getPhotoKey()); // Check if the returned user is updated as expected
    verify(userRepository, times(1)).findById(userId); // Verify that findById was called once with the correct userId
    verify(userRepository, times(1)).save(client1); // Verify that save was called once with the updated user
  }


  @Test
  void updateUserBannerKey_whenUserExists_shouldUpdateBannerKey() {
// Given
    String userId = "CLIENT1_ID";
    User client1 = clientEntity1(); // Mock user instance
    client1.setProfileBannerKey(null); // Reset profile banner key to null for testing
    User client1Updated = clientEntity1().toBuilder().profileBannerKey("user/" + userId + "/banner").build(); // Expected updated user
    when(userRepository.findById(userId)).thenReturn(Optional.of(client1)); // Mock repository behavior
    doNothing().when(userValidator).accept(client1); // Mock user validator
    when(userRepository.save(client1)).thenReturn(client1Updated); // Mock userRepository.save()

// When
    User result = userService.updateUserBannerKey(client1);

// Then
    assertEquals(client1Updated.getProfileBannerKey(), result.getProfileBannerKey()); // Check if the returned user is updated as expected
    verify(userRepository, times(1)).findById(userId); // Verify that findById was called once with the correct userId
    verify(userRepository, times(1)).save(client1); // Verify that save was called once with the updated user
  }
}

