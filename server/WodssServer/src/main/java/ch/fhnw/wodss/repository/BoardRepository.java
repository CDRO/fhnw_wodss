package ch.fhnw.wodss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{


}