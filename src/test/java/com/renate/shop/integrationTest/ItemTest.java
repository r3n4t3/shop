package com.renate.shop.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.renate.shop.generator.ItemGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Item;
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
public class ItemTest {

	@LocalServerPort
	private Integer port;
	private String baseUrl = "http://localhost:";
	private HttpHeaders headers = new HttpHeaders();
	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Before
	public void setup() {
		baseUrl += port;
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(Authenticate.getAccessToken(baseUrl));
		baseUrl += "/api/v1/items";
	}

	@Test
	public void testCreateItem() {

		Item item = ItemGenerator.generateItem();
		ResponseEntity<String> response = this.createItem(item);

		DocumentContext jsonResponse = JsonPath.parse(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		assertThat(jsonResponse.read("name").toString()).isEqualTo(item.getName());
		assertThat(jsonResponse.read("price").toString()).isEqualTo(item.getPrice().toString());
		assertThat(jsonResponse.read("quantity").toString()).isEqualTo(item.getQuantity().toString());

	}

	private ResponseEntity<String> createItem(Item item) {
		String categoryUrl = "http://localhost:" + port + "/api/v1/categories";
		HttpEntity categoryEntity = new HttpEntity(JSONConvertor.toJSON(item.getCategory()), headers);

		ResponseEntity<String> categoryResponse = this.restTemplate.exchange(categoryUrl, HttpMethod.POST, categoryEntity, String.class);
		item.getCategory().setId(new Long(JsonPath.parse(categoryResponse.getBody()).read("id").toString()));
		HttpEntity entity = new HttpEntity(JSONConvertor.toJSON(item), headers);
		return this.restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
	}

	@Test
	public void testUpdateItem() {
		Item item = ItemGenerator.generateItem();
		ResponseEntity<String> createItemResponse = this.createItem(item);

		String id = JsonPath.parse(createItemResponse.getBody()).read("id").toString();
		String updatedUrl = baseUrl + "/" + id;
		String categoryId = JsonPath.parse(createItemResponse.getBody()).read("category.id").toString();
		item.setId(new Long(id));
		item.getCategory().setId(new Long(categoryId));
		item.setName(RandomStringUtils.random(10, true, false));

		HttpEntity entity = new HttpEntity(JSONConvertor.toJSON(item), headers);
		ResponseEntity<String> response = this.restTemplate.exchange(updatedUrl, HttpMethod.PUT, entity, String.class);

		DocumentContext jsonResponse = JsonPath.parse(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(jsonResponse.read("name").toString()).isEqualTo(item.getName());
		assertThat(jsonResponse.read("price").toString()).isEqualTo(item.getPrice().toString());
		assertThat(jsonResponse.read("quantity").toString()).isEqualTo(item.getQuantity().toString());
	}

	@Test
	public void testUpdateNonExsitingItem() {
		Item item = ItemGenerator.generateItem();
		String url = baseUrl + "/" + item.getId();
		HttpEntity entity = new HttpEntity(JSONConvertor.toJSON(item), headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testGetItems() {
		HttpEntity entity = new HttpEntity(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(JsonPath.parse(response.getBody()).read("pageable.pageNumber").toString()).isEqualTo("0");
	}

	@Test
	public void testGetItemsWithPageParams() {
		String url = baseUrl + "?page=2&sie=10";
		HttpEntity entity = new HttpEntity(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(JsonPath.parse(response.getBody()).read("pageable.pageNumber").toString()).isEqualTo("2");
	}

	@Test
	public void testGetItemsWithInValidPageParams() {
		String url = baseUrl + "?page=-2";
		HttpEntity entity = new HttpEntity(null, headers);

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testGetItem() {
		Item item = ItemGenerator.generateItem();
		ResponseEntity<String> createdItemResponse = this.createItem(item);
		String id = JsonPath.parse(createdItemResponse.getBody()).read("id").toString();
		HttpEntity entity = new HttpEntity(null, headers);
		String url = baseUrl + "/" + id;

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		DocumentContext jsonResponse = JsonPath.parse(response.getBody());

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(jsonResponse.read("name").toString()).isEqualTo(item.getName());
		assertThat(jsonResponse.read("quantity").toString()).isEqualTo(item.getQuantity().toString());
	}

	@Test
	public void testGetNonExistingItem() {
		HttpEntity entity = new HttpEntity(null, headers);
		String url = baseUrl + "/" + new Random().nextInt();

		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}
}
