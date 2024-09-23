//package com.example.calculator.services;
//
//import com.example.calculator.models.User;
//import com.example.calculator.repositories.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @MockBean
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Test
//    public void testRegisterUser() {
//        // Mock password encoding
//        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
//
//        // Continue with your test
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPasswordHash("password");
//
//        userService.registerUser(user);
//
//        // Verify that the password was encoded correctly
//        verify(passwordEncoder).encode(any(CharSequence.class));
//    }
//}
