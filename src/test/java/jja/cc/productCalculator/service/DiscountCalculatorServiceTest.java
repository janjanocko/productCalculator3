package jja.cc.productCalculator.service;

import jja.cc.productCalculator.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

class DiscountCalculatorServiceTest {

    @Test
    void calculateSimple() {

        Customer customer = new Customer();
        VolumeDiscount volumeDiscount = new VolumeDiscount();
        volumeDiscount.setCustomer(customer);
        volumeDiscount.setMinAmount(50.0);
        volumeDiscount.setMaxAmount(500.0);
        volumeDiscount.setDiscount(10.0);

        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setCustomer(customer);
        timeDiscount.setDiscount(5.0);
        timeDiscount.setFromDate(Instant.now().minus(1, ChronoUnit.DAYS));
        timeDiscount.setToDate(Instant.now().plus(1, ChronoUnit.DAYS));


        Product product1 = new Product();
        product1.setPrice(100.0);

        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setCustomer(customer);
        productDiscount.setProduct(product1);
        productDiscount.setDiscount(10.0);


        customer.getDiscounts().add(volumeDiscount);
        customer.getDiscounts().add(timeDiscount);
        customer.getDiscounts().add(productDiscount);

        DiscountCalculatorService discountCalculatorService = new DiscountCalculatorService();
        Map<Product, Double> products = new HashMap<>();
        products.put(product1, 2.0);
        Double result = discountCalculatorService.calculate(customer, products);

        //discount 25%, before discounts 200, after 150

        Assertions.assertEquals(150.0, result);
    }
}