package com.spring.trelloclone.repository;

import com.spring.trelloclone.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    public Board findBoardByTitle(String title);

}
