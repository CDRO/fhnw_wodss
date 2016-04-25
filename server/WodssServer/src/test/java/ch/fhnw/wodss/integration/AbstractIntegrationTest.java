package ch.fhnw.wodss.integration;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.wodss.WodssServer;
import ch.fhnw.wodss.domain.Task;
import ch.fhnw.wodss.security.Token;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WodssServer.class)
@WebIntegrationTest
public abstract class AbstractIntegrationTest {

	protected ObjectMapper objectMapper = new ObjectMapper();

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

	protected <T> T doGet(String url, Token token, Object obj, Class<T> type, Object... urlParameters) throws Exception {
		String formattedUrl = MessageFormat.format(url, urlParameters);
		URL u = new URL(formattedUrl);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setRequestMethod("GET");

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

	protected Task doMulitPartPostTask(String url, Token token, JSONObject json, List<File> files,
			Object... urlParameters) throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();
		mpBuilder.addTextBody("info", json.toJSONString(), ContentType.APPLICATION_JSON);
		if (files != null) {
			for (File file : files) {
				mpBuilder.addBinaryBody("file", file);
			}
		}
		HttpEntity entity = mpBuilder.build();

		String formattedUrl = MessageFormat.format(url, urlParameters);

		HttpPost httpPost = new HttpPost(formattedUrl);
		httpPost.setHeader("x-session-token", token.getId());
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity result = response.getEntity();

		InputStream is = result.getContent();

		Task readValue = objectMapper.readValue(is, Task.class);
		return readValue;

	}

	protected Task doMulitPartPutTask(String url, Token token, JSONObject json, File file, Object... urlParameters)
			throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();
		mpBuilder.addTextBody("info", json.toJSONString(), ContentType.APPLICATION_JSON);
		if (file != null) {
			mpBuilder.addBinaryBody("file", file);
		}
		HttpEntity entity = mpBuilder.build();

		String formattedUrl = MessageFormat.format(url, urlParameters);

		HttpPut httpPut = new HttpPut(formattedUrl);
		httpPut.setHeader("x-session-token", token.getId());
		httpPut.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPut);
		HttpEntity result = response.getEntity();

		InputStream is = result.getContent();
		Task readValue = objectMapper.readValue(is, Task.class);
		return readValue;

	}
}
