package ch.fhnw.wodss.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.fhnw.wodss.domain.Board;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.repository.BoardRepository;
import ch.fhnw.wodss.repository.TaskRepository;

@Component
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	public BoardService(){
		super();
	}

	@Transactional
	public Board saveBoard(Board board){
		Set<User> tempSet = new HashSet<User>();
		for(User member : board.getUsers()){
			User savedMember = userService.getByEmail(member.getEmail());
			if(savedMember == null){
				savedMember = UserFactory.getInstance().createUser(member.getEmail(), member.getEmail());
				savedMember = userService.saveUser(savedMember);
			}
			tempSet.add(savedMember);
		}
		board.getUsers().clear();
		board.getUsers().addAll(tempSet);
		return boardRepository.save(board);
	}
	
	@Transactional
	public void deleteBoard(Board board){
		boardRepository.delete(board);
		List<Task> tasks = taskRepository.findByBoard(board);
		for(Task task : tasks){
			taskRepository.delete(task.getId());
		}
	}
	
	@Transactional
	public void deleteBoard(Integer id){
		boardRepository.delete(id);
	}
	
	@Transactional(readOnly=true)
	public List<Board> getAll(){
		return boardRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Board getById(Integer id){
		return boardRepository.findOne(id);
	}
	
	
}
