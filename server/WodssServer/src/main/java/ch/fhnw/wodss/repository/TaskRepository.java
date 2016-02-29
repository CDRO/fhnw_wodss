package ch.fhnw.wodss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

}
