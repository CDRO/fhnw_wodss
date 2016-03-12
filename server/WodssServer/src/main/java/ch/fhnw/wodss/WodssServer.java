package ch.fhnw.wodss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WodssServer {

	public static void main(String[] args) {
		SpringApplication.run(WodssServer.class, args);
	}

}
