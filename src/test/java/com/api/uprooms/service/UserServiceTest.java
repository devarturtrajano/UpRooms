package com.api.uprooms.service;

import com.api.uprooms.dto.UserRequestDTO;
import com.api.uprooms.dto.UserResponseDTO;
import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserService mockUserService;

    private User sampleUser;
    private User sampleAdminUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "John Doe", "john@email.com", "password", EnumUserRole.UserRole.COLLABORATOR, new ArrayList<>());
        sampleAdminUser = new User(2L, "Admin Name", "admin@email.com", "adminPassword", EnumUserRole.UserRole.ADMIN, new ArrayList<>());
    }

    @Test
    @DisplayName("Should create a user successfully")
    void Should_Create_User_Successfully() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(sampleUser.getName(), sampleUser.getEmail(), sampleUser.getPassword(), sampleUser.getRole());

        Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenReturn(sampleUser);

        UserResponseDTO response = mockUserService.createUser(userRequestDTO);

        Mockito.verify(mockUserRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Assertions.assertEquals(sampleUser.getName(), response.name());
        Assertions.assertEquals(sampleUser.getEmail(), response.email());
        Assertions.assertEquals(sampleUser.getRole(), response.role());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when user email already exists")
    void ShouldThrowIllegalArgumentException_When_User_Email_Already_Exists() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(sampleUser.getName(), sampleUser.getEmail(), sampleUser.getPassword(), sampleUser.getRole());

        Mockito.when(mockUserRepository.existsByEmail(sampleUser.getEmail())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockUserService.createUser(userRequestDTO));
    }

    @Test
    @DisplayName("Should find a user by id successfully")
    void Should_Find_User_By_Id_Successfully() {
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.of(sampleUser));

        User user = mockUserService.findEntityById(sampleUser.getId());

        Assertions.assertEquals(sampleUser, user);
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
        Mockito.verify(mockUserRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should return a Illegal Argument Exception when a user is not found")
    void Should_Return_Illegal_Argument_Exception_When_User_Not_Found() {
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockUserService.findEntityById(sampleUser.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
        Mockito.verify(mockUserRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should return a list of users")
    void Should_Return_List_Of_Users() {
        Mockito.when(mockUserRepository.findById(sampleAdminUser.getId())).thenReturn(java.util.Optional.of(sampleAdminUser));
        Mockito.when(mockUserRepository.findAll()).thenReturn(java.util.List.of(sampleAdminUser));

        java.util.List<UserResponseDTO> response = mockUserService.findAll(sampleAdminUser.getId());

        Assertions.assertEquals(1, response.size());
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleAdminUser.getId());
        Mockito.verify(mockUserRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should return Illegal Argument Exception when tries to find all users on a not found user parameter.")
    void Should_Return_Illegal_Argument_Exception_When_Tries_To_Find_All_Users_On_Not_Found_User_Parameter() {
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockUserService.findAll(sampleUser.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
        Mockito.verify(mockUserRepository, Mockito.never()).findAll();
    }

    @Test
    @DisplayName("Should Return Security exception when a non admin user tries to find all user registered")
    void Should_Return_Security_Exception_When_A_Non_Admin_User_Tries_To_Find_All_User_Registered() {
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.of(sampleUser));

        Assertions.assertThrows(SecurityException.class, () -> mockUserService.findAll(sampleUser.getId()));

    }


}
