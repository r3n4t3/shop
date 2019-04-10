package com.renate.shop.integrationTest;

import java.util.HashMap;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.sun.deploy.net.HttpResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Authenticate {

	private static TestRestTemplate restTemplate = new TestRestTemplate();
	private static HttpHeaders headers = new HttpHeaders();

	public static String getAccessToken(String baseUrl) {
		String url = baseUrl + "/oauth/token?grant_type={password}&username=jane&password=password";
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic ZGVmYXVsdDpzZWNyZXQ=");
		HttpEntity httpEntity = new HttpEntity<>(null, headers);
		Map<String, String> params = getRequestParam();

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				String.class, params) ;

		String accessToken = JsonPath.parse(response.getBody()).read("access_token").toString();
		return accessToken;
	}

	private static Map<String, String> getRequestParam() {
		Map<String, String> params = new HashMap<>();
		params.put("grant_type", "password");
		params.put("username", "jane");
		params.put("password", "password");
		return  params;
	}
}
