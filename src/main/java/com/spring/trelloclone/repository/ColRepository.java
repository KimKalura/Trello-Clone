package com.spring.trelloclone.repository;

import com.spring.trelloclone.model.Col;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColRepository extends JpaRepository<Col, Long> {

}
