package com.renate.shop.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;
import com.renate.shop.generator.CategoryGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryTest {

	@LocalServerPort
	private Integer port;
	private String baseUrl = "http://localhost:";
	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();

	@Before
	public void setup() {
		baseUrl += port;
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(Authenticate.getAccessToken(baseUrl));
		baseUrl += "/api/v1/categories";
	}

	@Test
	public void testCreateCategories() {
		Category category = CategoryGenerator.generateCategory();
		HttpEntity entity =  new HttpEntity(JSONConvertor.toJSON(category), headers);

		ResponseEntity<String> response = this.restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class );

		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(JsonPath.parse(response.getBody()).read("name")
				.toString().contentEquals(category.getName()));
	}

	@Test
	public void testGetCategories() {
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(baseUrl, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.getBody().contains("pageable"));
	}

	@Test
	public void testGetCategoriesWithValidPageParams() {
		String url = baseUrl + "?page=0&size=2";
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.getBody().contains("pageable"));
	}

	@Test
	public void testGetCategoriesWithInValidPageParams() {
		String url = baseUrl + "?page=-2";
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful());
	}

	@Test
	public void testGetCategory() {
		HttpEntity entity = new HttpEntity<>(null, headers);
		Category category = CategoryGenerator.generateCategory();
		HttpEntity newEntity =  new HttpEntity(JSONConvertor.toJSON(category), headers);

		ResponseEntity<String> response = this.restTemplate.exchange(baseUrl, HttpMethod.POST, newEntity, String.class );
		String id = JsonPath.parse(response.getBody()).read("id").toString();
		String url = baseUrl + "/" + id;
		response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class );

		System.out.println(response.getBody());
	}

}
