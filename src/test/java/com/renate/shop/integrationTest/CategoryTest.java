package com.renate.shop.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.renate.shop.generator.CategoryGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Category;
import org.apache.commons.lang3.RandomStringUtils;
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

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(JsonPath.parse(response.getBody()).read("name")
				.toString()).isEqualTo(category.getName());
	}

	@Test
	public void testUpdateCategories() {
		Category category = CategoryGenerator.generateCategory();
		HttpEntity entity = new HttpEntity(JSONConvertor.toJSON(category), headers);

		ResponseEntity<String> postResponse = this.restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
		String categoryId = JsonPath.parse(postResponse.getBody()).read("id").toString();
		String url = baseUrl + "/" + categoryId;
		category.setName(RandomStringUtils.random(10, true, false));
		category.setId(new Long(categoryId));
		HttpEntity updatedEntity = new HttpEntity(JSONConvertor.toJSON(category), headers);
		ResponseEntity<String> updateResponse = this.restTemplate.exchange(url, HttpMethod.PUT, updatedEntity, String.class);

		assertThat(updateResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		DocumentContext jsonObj = JsonPath.parse(updateResponse.getBody());
		assertThat(jsonObj.read("name").toString()).isEqualTo(category.getName());
		assertThat(jsonObj.read("id").toString()).isEqualTo(category.getId().toString());
	}

	@Test
	public void testUpdateNonExistingCategories() {
		Category category = CategoryGenerator.generateCategory();
		HttpEntity entity = new HttpEntity(JSONConvertor.toJSON(category), headers);

		String url = baseUrl + "/" + category.getId();
		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testGetCategories() {
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(baseUrl, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(JsonPath.parse(response.getBody()).read("pageable.pageNumber").toString()).isEqualTo("0");
	}

	@Test
	public void testGetCategoriesWithValidPageParams() {
		String url = baseUrl + "?page=0&size=2";
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(JsonPath.parse(response.getBody()).read("pageable.pageNumber").toString()).isEqualTo("0");
	}

	@Test
	public void testGetCategoriesWithInValidPageParams() {
		String url = baseUrl + "?page=-2";
		HttpEntity httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
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

		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(JsonPath.parse(response.getBody()).read("name").toString()).isEqualTo(category.getName());
	}

	@Test
	public void testGetNonExistingCategory() {
		HttpEntity entity = new HttpEntity(null, headers);
		String url = baseUrl + "/" + new Random().nextInt();

		ResponseEntity<String>  response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

}
