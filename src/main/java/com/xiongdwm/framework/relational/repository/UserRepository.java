package com.xiongdwm.framework.relational.repository;

import com.xiongdwm.framework.relational.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
