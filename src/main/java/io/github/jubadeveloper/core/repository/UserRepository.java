package io.github.jubadeveloper.core.repository;

import io.github.jubadeveloper.core.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from `User` u where u.email = ?1")
    User findByEmail (String email);
}
