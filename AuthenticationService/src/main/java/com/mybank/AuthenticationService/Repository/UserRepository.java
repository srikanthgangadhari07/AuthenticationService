package com.mybank.AuthenticationService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mybank.AuthenticationService.Model.User;
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {
   Optional<User> findByuserName( String userName);

}
