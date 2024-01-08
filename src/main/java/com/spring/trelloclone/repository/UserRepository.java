package com.spring.trelloclone.repository;

import com.spring.trelloclone.model.Long;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Long, java.lang.Long> {


}
