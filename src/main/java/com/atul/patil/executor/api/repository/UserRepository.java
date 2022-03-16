package com.atul.patil.executor.api.repository;

import com.atul.patil.executor.api.entiry.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
