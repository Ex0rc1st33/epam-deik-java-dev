package com.epam.training.ticketservice.backend.user;

import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.backend.user.service.UserService;
import com.epam.training.ticketservice.backend.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService underTest = new UserServiceImpl(userRepository);

    @Test
    public void testSignInPrivilegedShouldCallUserRepositoryAndReturnCorrectMessageWhenGivenUsernameAndPasswordValid() {
        //Given
        User user = new User("admin", "admin", User.Role.ADMIN);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole())).thenReturn(Optional.of(user));
        UserDto userDto = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        String expected = "Signed in: " + userDto + ", you have access to admin commands";

        //When
        String actual = underTest.signInPrivileged("admin", "admin");

        //Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    @Test
    public void testSignInPrivilegedShouldCallUserRepositoryAndReturnErrorMessageWhenCredentialsInvalid() {
        //Given
        User user = new User("admin", "", User.Role.ADMIN);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole())).thenReturn(Optional.empty());
        String expected = "Login failed due to incorrect credentials";

        //When
        String actual = underTest.signInPrivileged("admin", "");

        //Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    @Test
    public void testSignUpShouldCallUserRepositoryAndReturnCorrectMessageWhenGivenUserDoesNotExistYet() {
        //Given
        User user = new User("szia", "lajos", User.Role.USER);
        when(userRepository.save(user)).thenReturn(user);
        String expected = "Signed up new user: " + user;

        //When
        String actual = underTest.signUp(user.getUsername(), user.getPassword());

        //Then
        assertEquals(expected, actual);
        verify(userRepository).save(user);
    }

    @Test
    public void testSignInShouldCallUserRepositoryAndReturnCorrectMessageWhenGivenUsernameAndPasswordValid() {
        //Given
        User user = new User("szia", "lajos", User.Role.USER);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole()))
                .thenReturn(Optional.of(user));
        UserDto userDto = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        String expected = "Signed in: " + userDto;

        //When
        String actual = underTest.signIn(user.getUsername(), user.getPassword());

        //Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    @Test
    public void testSignInShouldCallUserRepositoryAndReturnErrorMessageWhenCredentialsInvalid() {
        //Given
        User user = new User("szia", "lajos", User.Role.USER);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole()))
                .thenReturn(Optional.empty());
        String expected = "Login failed due to incorrect credentials";

        //When
        String actual = underTest.signIn(user.getUsername(), user.getPassword());

        //Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    @Test
    public void testSignOutShouldCallUserRepositoryAndReturnCorrectMessageWhenUserSignedIn() {
        //Given
        User user = new User("szia", "lajos", User.Role.USER);
        UserDto userDto = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole()))
                .thenReturn(Optional.of(user));
        underTest.signUp(user.getUsername(), user.getPassword());
        underTest.signIn(user.getUsername(), user.getPassword());
        String expected = "Signed out: " + userDto;

        //When
        String actual = underTest.signOut();

        //Then
        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    @Test
    public void testSignOutShouldCallUserRepositoryAndReturnErrorMessageWhenUserSignedOut() {
        //Given
        String expected = "You're currently signed out, you need to sign in first";

        //When
        String actual = underTest.signOut();

        //Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLoggedInUserShouldReturnCorrectOptional() {
        //Given
        User user = new User("szia", "lajos", User.Role.USER);
        UserDto userDto = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole()))
                .thenReturn(Optional.of(user));
        underTest.signUp(user.getUsername(), user.getPassword());
        underTest.signIn(user.getUsername(), user.getPassword());
        Optional<UserDto> expected = Optional.of(userDto);

        //When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        //Then
        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(userRepository).findByUsernameAndPasswordAndRole(user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

}
