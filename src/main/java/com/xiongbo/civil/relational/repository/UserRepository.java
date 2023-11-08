package com.xiongbo.civil.relational.repository;

import com.xiongbo.civil.relational.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
