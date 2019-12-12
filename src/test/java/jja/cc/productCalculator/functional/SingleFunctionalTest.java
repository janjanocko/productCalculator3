package jja.cc.productCalculator.functional;

import jja.cc.productCalculator.ProductCalculatorApplication;
import jja.cc.productCalculator.model.Customer;
import jja.cc.productCalculator.model.Product;
import jja.cc.productCalculator.model.TimeDiscount;
import jja.cc.productCalculator.model.VolumeDiscount;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ProductCalculatorApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleFunctionalTest {

    private static final String BASE_URL = "http://localhost:";
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void testCreateProduct() {
        Product product = new Product();
        product.setPrice(100.0);
        product.setName("My product");
        product.setProductId("123");
        ResponseEntity<Product> productCreated =
                restTemplate.postForEntity(BASE_URL + port + "/products", product, Product.class);
        assertNotNull(productCreated.getBody());
        assertEquals(HttpStatus.OK, productCreated.getStatusCode());
    }

    @Test
    @Order(2)
    public void testPutProduct() {
        Product product = new Product();
        product.setPrice(100.0);
        product.setProductId("1234");
        product.setName("My product with a new Name");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> requestUpdate = new HttpEntity<>(product, headers);
        ResponseEntity<Product> response = restTemplate.exchange(
                BASE_URL + port + "/products/1", HttpMethod.PUT, requestUpdate, Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("My product with a new Name", response.getBody().getName());
    }

    @Test
    @Order(3)
    public void testGetProduct() {
        ResponseEntity<Product> product = restTemplate.getForEntity(BASE_URL + port + "/products/1", Product.class);
        assertEquals(HttpStatus.OK, product.getStatusCode());
        assertEquals("My product with a new Name", product.getBody().getName());
    }

    @Test
    @Order(100)
    public void testDeleteProduct() {
        restTemplate.delete(BASE_URL + port + "/products/1", new HashMap<>());
    }

    @Test
    @Order(5)
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setRegistrationNumber(123L);
        customer.setName("My customer");
        ResponseEntity<Customer> customerCreated =
                restTemplate.postForEntity(BASE_URL + port + "/customers", customer, Customer.class);
        assertNotNull(customerCreated.getBody());
        assertEquals(HttpStatus.OK, customerCreated.getStatusCode());
    }

    @Test
    @Order(6)
    public void testPutCustomer() {
        Customer customer = new Customer();
        customer.setRegistrationNumber(456L);
        customer.setName("My customer with a new Name");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestUpdate = new HttpEntity<>(customer, headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                BASE_URL + port + "/customers/2", HttpMethod.PUT, requestUpdate, Customer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("My customer with a new Name", response.getBody().getName());
    }

    @Test
    @Order(7)
    public void testGetCustomer() {
        ResponseEntity<Customer> customer = restTemplate.getForEntity(BASE_URL + port + "/customers/2",
                Customer.class);
        assertEquals(HttpStatus.OK, customer.getStatusCode());
        assertEquals("My customer with a new Name", customer.getBody().getName());
    }

    @Test
    @Order(101)
    public void testDeleteCustomer() {
        restTemplate.delete(BASE_URL + port + "/customers/2", new HashMap<>());
    }

    @Test
    @Order(10)
    public void testAddVolumeDiscount() {
        VolumeDiscount volumeDiscount = new VolumeDiscount();
        volumeDiscount.setDiscount(10.0);
        volumeDiscount.setMinAmount(1_000.0);
        volumeDiscount.setMaxAmount(10_000.0);
        ResponseEntity<VolumeDiscount> discount =
                restTemplate.postForEntity(BASE_URL + port + "/customers/2/volumeDiscounts", volumeDiscount,
                        VolumeDiscount.class);
        assertEquals(HttpStatus.OK, discount.getStatusCode());
    }

    @Test
    @Order(11)
    public void testAddTimeDiscount() {
        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setDiscount(10.0);
        timeDiscount.setFromDate(Instant.now());
        timeDiscount.setToDate(Instant.now().plus(1, ChronoUnit.HOURS));
        ResponseEntity<TimeDiscount> productCreated =
                restTemplate.postForEntity(BASE_URL + port + "/customers/2/timeDiscounts", timeDiscount,
                        TimeDiscount.class);
        assertEquals(HttpStatus.OK, productCreated.getStatusCode());
    }
//TODO resolve problem with hibernate object deserialization!
//    @Test
//    @Order(12)
//    public void testAddProductDiscount() {
//        Product product = new Product();
//        product.setId(1L);
//        ProductDiscount productDiscount = new ProductDiscount();
//        productDiscount.setDiscount(10.0);
//        productDiscount.setProduct(product);
//        ResponseEntity<ProductDiscount> productCreated =
//                restTemplate.postForEntity(BASE_URL + port + "/customers/2/productDiscounts", productDiscount,
//                        ProductDiscount.class);
//        assertEquals(HttpStatus.OK, productCreated.getStatusCode());
//    }

    @Test
    @Order(14)
    public void testGetCustomerDiscounts() {
        Object[] response = restTemplate.getForObject(BASE_URL + port + "/customers/2/discounts",
                Object[].class);
        assertNotNull(response);
    }


}
