package com.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.Role;

public interface UserRoleRepo extends JpaRepository<Role, Integer>{
    
}
