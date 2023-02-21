package com.tecside.appEvent.repositories;

import com.tecside.appEvent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //@Query(value = "select * from public.users u where email = :email and password = :password")
    public User findByEmailAndPassword(String email, String password);

}
