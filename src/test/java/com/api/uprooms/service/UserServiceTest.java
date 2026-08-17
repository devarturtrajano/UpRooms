package com.api.uprooms.service;

import com.api.uprooms.dto.UserRequestDTO;
import com.api.uprooms.dto.UserResponseDTO;
import com.api.uprooms.exceptions.BusinessException;
import com.api.uprooms.exceptions.ResourceNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

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

        Mockito.when(mockPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenReturn(sampleUser);

        UserResponseDTO response = mockUserService.createUser(userRequestDTO);

        Mockito.verify(mockUserRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Assertions.assertEquals(sampleUser.getName(), response.name());
        Assertions.assertEquals(sampleUser.getEmail(), response.email());
        Assertions.assertEquals(sampleUser.getRole(), response.role());
    }

    @Test
    @DisplayName("Should throw BusinessException when user email already exists")
    void ShouldThrowBusinessException_When_User_Email_Already_Exists() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(sampleUser.getName(), sampleUser.getEmail(), sampleUser.getPassword(), sampleUser.getRole());

        Mockito.when(mockUserRepository.existsByEmail(sampleUser.getEmail())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, () -> mockUserService.createUser(userRequestDTO));
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
    @DisplayName("Should return a ResourceNotFoundException when a user is not found")
    void Should_Return_Resource_Not_Found_Exception_When_User_Not_Found() {
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> mockUserService.findEntityById(sampleUser.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
        Mockito.verify(mockUserRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should return a list of users")
    void Should_Return_List_Of_Users() {
        Mockito.when(mockUserRepository.findAll()).thenReturn(java.util.List.of(sampleAdminUser));

        java.util.List<UserResponseDTO> response = mockUserService.findAll();

        Assertions.assertEquals(1, response.size());
        Mockito.verify(mockUserRepository, Mockito.times(1)).findAll();
    }


}
