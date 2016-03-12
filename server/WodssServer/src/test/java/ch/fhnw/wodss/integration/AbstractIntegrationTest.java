package ch.fhnw.wodss.integration;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.WodssServer;
import ch.fhnw.wodss.security.Token;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WodssServer.class)
@WebIntegrationTest
public abstract class AbstractIntegrationTest {

	private ObjectMapper objectMapper = new ObjectMapper();

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
