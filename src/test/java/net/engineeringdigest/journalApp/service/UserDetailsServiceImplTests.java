package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;

import static  org.mockito.Mockito.*;


public class UserDetailsServiceImplTests {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void loadUserByUsernameTest() {
        // Mocking the repository to return a UserEntity
        User mockUserEntity = new User("Ram","jdjchudbc");
        mockUserEntity.setRoles(new ArrayList<>());

        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(mockUserEntity);

        UserDetails user = userDetailsService.loadUserByUsername("Ram");

        Assertions.assertNotNull(user);
    }

}