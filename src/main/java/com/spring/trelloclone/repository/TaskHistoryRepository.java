package com.spring.trelloclone.repository;


import com.spring.trelloclone.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {


}
