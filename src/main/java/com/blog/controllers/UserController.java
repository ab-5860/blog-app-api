package com.blog.controllers;

import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDTO;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    //POST - create user
    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto)
    {
        UserDTO createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto,HttpStatus.CREATED);
    }

    //PUT - update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Integer userId)
    {
        UserDTO updatUserDTO = this.userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(updatUserDTO);
    }

    //DELETE - delete user
    // @DeleteMapping("/{userId}")
    // public ResponseEntity<?> deleteUser(@PathVariable("user") Integer userId)
    // {
    //         this.userService.deleteUser(userId);
    //         return new ResponseEntity(Map.of("message","User deleted Successfully"), HttpStatus.OK);
    // }
    
    //ADMIN - 
    //delete user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId)
    {
            this.userService.deleteUser(userId);
            return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully",true), HttpStatus.OK);
    }
    

    //GET - user get
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers()
    {
        List<UserDTO> allUsers = this.userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //GET - user get --> get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId)
    {
        UserDTO user = this.userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
