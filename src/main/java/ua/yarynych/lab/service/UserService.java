package ua.yarynych.lab.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.yarynych.lab.dto.UserRegistrationDto;
import ua.yarynych.lab.model.User;


public interface UserService extends UserDetailsService{
    User save(UserRegistrationDto registrationDto);
}