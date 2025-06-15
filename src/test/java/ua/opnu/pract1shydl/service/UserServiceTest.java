package ua.opnu.pract1shydl.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.opnu.pract1shydl.dto.UserDTO;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.UserRepository;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setUsername("john_doe");
        userDTO.setEmail("john.doe@example.com");

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    public void testCreateUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals("john_doe", createdUser.getUsername());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUser(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    public void testGetUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        User foundUser = userService.getUser(1L);

        // Assert
        assertNull(foundUser);
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("johnny_doe");
        updatedUser.setEmail("johnny.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("johnny_doe", result.getUsername());
        assertEquals("johnny.doe@example.com", result.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("johnny_doe");
        updatedUser.setEmail("johnny.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteUser() {
        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}