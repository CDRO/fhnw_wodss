package ch.fhnw.wodss.integration;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.WodssServer;
import ch.fhnw.wodss.domain.LoginData;
import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.domain.UserFactory;
import ch.fhnw.wodss.security.Password;
import ch.fhnw.wodss.security.Token;
import ch.fhnw.wodss.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WodssServer.class)
@WebIntegrationTest
public abstract class AbstractIntegrationTest {

	protected ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	@Before
	public void saveAUser(){
		user = UserFactory.getInstance().createUser("Hans Muster", "email@fhnw.ch");
		Password pass = new Password("password".toCharArray());
		LoginData loginData = new LoginData();
		loginData.setPassword(pass.getHash());
		loginData.setSalt(pass.getSalt());
		user.setLoginData(loginData);
		user = userService.saveUser(user);
	}
	
	@After
	public void deleteAUser(){
		userService.deleteUser(user.getId());
	}

	protected <T> T doPost(String url, Token token, Object obj, Class<T> type, Object... urlParameters)
			throws Exception {
		String formattedUrl = MessageFormat.format(url, urlParameters);
		URL u = new URL(formattedUrl);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setRequestMethod("POST");
		con.setDoOutput(true);

		con.setRequestProperty("Content-Type", "application/json");
		if (token != null) {
			con.setRequestProperty("x-session-token", token.getId());
		}

		con.connect();

		OutputStream outputStream = con.getOutputStream();
		outputStream.write(objectMapper.writeValueAsBytes(obj));
		outputStream.flush();

		InputStream is = con.getInputStream();

		T readValue = objectMapper.readValue(is, type);

		return readValue;

	}

	protected <T> T doPut(String url, Token token, Object obj, Class<T> type, Object... urlParameters)
			throws Exception {
		String formattedUrl = MessageFormat.format(url, urlParameters);
		URL u = new URL(formattedUrl);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setRequestMethod("PUT");
		con.setDoOutput(true);

		con.setRequestProperty("Content-Type", "application/json");
		if (token != null) {
			con.setRequestProperty("x-session-token", token.getId());
		}

		con.connect();

		OutputStream outputStream = con.getOutputStream();
		outputStream.write(objectMapper.writeValueAsBytes(obj));
		outputStream.flush();

		InputStream is = con.getInputStream();

		T readValue = objectMapper.readValue(is, type);

		return readValue;

	}

	protected <T> T doGet(String url, Token token, Class<T> type, Object... urlParameters) throws Exception {
		String formattedUrl = MessageFormat.format(url, urlParameters);
		URL u = new URL(formattedUrl);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setRequestMethod("GET");

		if (token != null) {
			con.setRequestProperty("x-session-token", token.getId());
		}

		con.connect();

		InputStream is = con.getInputStream();

		T readValue = objectMapper.readValue(is, type);

		return readValue;

	}

	protected <T> T doDelete(String url, Token token, Class<T> type, Object... urlParameters) throws Exception {
		String formattedUrl = MessageFormat.format(url, urlParameters);
		URL u = new URL(formattedUrl);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setRequestMethod("DELETE");
		con.setDoOutput(true);

		if (token != null) {
			con.setRequestProperty("x-session-token", token.getId());
		}

		InputStream is = con.getInputStream();

		T readValue = objectMapper.readValue(is, type);

		return readValue;

	}

}
