package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.UserDTO;
import com.blog.repositories.UserRepo;
import com.blog.repositories.UserRoleRepo;
import com.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRoleRepo userRoleRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = this.dtoToUser(userDTO);

        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException(
                    "User"," Id ",userId
        ));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());


        User updatedUser = this.userRepo.save(user);

        UserDTO userDto1 = this.userToDto(updatedUser);
        return userDto1;
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        
        User user = this.userRepo.findById(userId)
                    .orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
        return this.userToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        
        List<User> users = this.userRepo.findAll();
        
        List<UserDTO> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
        
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
        
        this.userRepo.delete(user);
    }
    
    // convert from DTO Object to entity object
    // private User dtoToUser(UserDTO userDTO)
    // {
    //     User user = new User();

        
    //     user.setId(userDTO.getId());
    //     user.setName(userDTO.getName());
    //     user.setEmail(userDTO.getEmail());
    //     user.setPassword(userDTO.getPassword());
    //     user.setAbout(userDTO.getAbout());

    //     return user;
    // }

    private User dtoToUser(UserDTO userDTO)
    {
        User user = this.modelMapper.map(userDTO, User.class);

        return user;
    }

    // convert from entity object to DTO object

    // private UserDTO userToDto(User user)
    // {
    //     UserDTO userDTO = new UserDTO();
    //     userDTO.setId(user.getId());
    //     userDTO.setName(user.getName());
    //     userDTO.setEmail(user.getEmail());
    //     userDTO.setPassword(user.getPassword());
    //     userDTO.setAbout(user.getAbout());

    //     return userDTO;
    // }


    private UserDTO userToDto(User user)
    {
        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

        return userDTO;
    }

    @Override
    public UserDTO registerNewUser(UserDTO userDTO) {
        
        User user = this.modelMapper.map(userDTO, User.class);

        //encoded the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));


        // roles to new user
        Role role = this.userRoleRepo.findById(AppConstants.NORMAL_USER).get();

        user.getRoles().add(role);

        User newUser = this.userRepo.save(user);

        return this.modelMapper.map(newUser, UserDTO.class);
    }
}
