package ch.fhnw.wodss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByBoard(Board board);
	
}
