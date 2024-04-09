package com.spring.trelloclone.repository;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.model.Col;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColRepository extends JpaRepository<Col, Long> {

    public List<Col> findByBoard(Board board);

}
