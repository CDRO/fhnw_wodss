package ch.fhnw.wodss.domain;

import java.util.UUID;

import ch.fhnw.wodss.security.Password;

public class LoginDataFactory {

	private static LoginDataFactory instance;
	
	private LoginDataFactory(){
		super();
	}
	
	public static LoginDataFactory getInstance(){
		if(instance == null){
			instance = new LoginDataFactory();
		}
		return instance;
	}
	
	public LoginData createLoginData(String password){
		LoginData loginData = new LoginData();
		Password pass = new Password(password.toCharArray());
		loginData.setPassword(pass.getHash());
		loginData.setSalt(pass.getSalt());
		loginData.setValidated(false);
		loginData.setValidationCode(UUID.randomUUID().toString());
		return loginData;
	}
	
}
