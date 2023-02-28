package com.tecside.appEvent.repositories;

import com.tecside.appEvent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    public User findByEmail(String email);


}
